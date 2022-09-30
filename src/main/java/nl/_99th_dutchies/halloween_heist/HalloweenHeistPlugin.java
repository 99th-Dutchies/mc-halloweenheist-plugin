package nl._99th_dutchies.halloween_heist;

import de.beproud.scoreboard.PlayerScoreboard;
import nl._99th_dutchies.halloween_heist.command.*;
import nl._99th_dutchies.halloween_heist.listener.*;
import nl._99th_dutchies.halloween_heist.scoreboard.ScoreboardManager;
import nl._99th_dutchies.halloween_heist.season.*;
import nl._99th_dutchies.halloween_heist.util.*;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class HalloweenHeistPlugin extends JavaPlugin implements Listener {
    private ScoreboardManager scoreboardManager;
    private PlayerManager playerManager;
    private WorldBorderManager worldBorderManager;

    public final FileConfiguration config = getConfig();
    public HeistState heistState;
    public HeistObjectLocation heistObjectLocation;
    public World mainWorld;
    public ASeason season;

    @Override
    public void onEnable() {
        // Load config
        config.options().copyDefaults(true);
        saveConfig();

        // Load world
        this.mainWorld = this.getServer().getWorld(this.config.getString("worldName"));
        this.setSeason();

        // Load managers
        this.playerManager = new PlayerManager(this);
        this.worldBorderManager = new WorldBorderManager(this);
        this.scoreboardManager = new ScoreboardManager();

        // Init events
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new HeistObjectTrackingListener(this), this);
        Bukkit.getPluginManager().registerEvents(new HeistObjectSavingListener(this), this);
        Bukkit.getPluginManager().registerEvents(new AntiGriefingListener(this), this);

        // Init commands
        this.getCommand("kit").setExecutor(new CommandKit(this));
        this.getCommand("location").setExecutor(new CommandLocation(this));
        this.getCommand("trade").setExecutor(new CommandTrade(this));

        // Set heist data
        this.heistState = new HeistState(this);
        this.heistObjectLocation = new HeistObjectLocation(this);
        if(!heistState.getBoolean("itemLoaded")) {
            this.season.spawnHeistObject();
        }
        worldBorderManager.create();

        // Start timers
        this.startTimedTasks();
    }

    public void setSeason() {
        switch(this.config.getInt("season")) {
            default:
            case 1:
                this.season = new Season1(this);
                break;
        }
    }

    public LocalDateTime getLocalNow() {
        return LocalDateTime.now(ZoneId.of(this.config.getString("timezone", "UTC")));
    }

    public LocalDateTime getGameStart() {
        int year = this.getLocalNow().getYear();
        return LocalDateTime.parse(this.config.getString("gameStart", year + "-10-31 00:00:00"));
    }

    public LocalDateTime getGameEnd() {
        int year = this.getLocalNow().getYear();
        return LocalDateTime.parse(this.config.getString("gameEnd", year + "-11-01 00:00:00"));
    }

    public long getTimeTillEnd() {
        return this.getLocalNow().until(this.getGameEnd(), ChronoUnit.SECONDS);
    }

    public void respawnHeistObject() {
        this.destroyHeistObject();
        this.season.spawnHeistObject();
        this.playerManager.resetLocationTimers();
    }

    public void destroyHeistObject() {
        Inventory inv = null;
        Material heistObjectMaterial = this.season.getHeistObjectMaterial();
        switch(this.heistObjectLocation.container) {
            case DROPPED:
                for(Entity entity : this.mainWorld.getNearbyEntities(this.heistObjectLocation.location, 0.5, 0.5, 0.5)) {
                    if(entity.getType().equals(EntityType.DROPPED_ITEM) && ((Item) entity).getItemStack().getType().equals(heistObjectMaterial)) {
                        entity.remove();
                    }
                }
                return;
            case STORAGE_BLOCK:
                inv = ((BlockInventoryHolder) this.heistObjectLocation.location.getBlock().getState()).getInventory();
                break;
            case STORAGE_ENTITY:
                inv = ((BlockInventoryHolder) this.heistObjectLocation.storingEntity).getInventory();
                break;
            case PLAYER:
                Player p = this.heistObjectLocation.lastPlayer;
                PlayerInventory pi = p.getInventory();
                if(pi.getItemInMainHand().getType().equals(heistObjectMaterial)) {
                    pi.setItemInMainHand(null);
                }
                if(pi.getItemInOffHand().getType().equals(heistObjectMaterial)) {
                    pi.setItemInOffHand(null);
                }
                inv = pi;
                break;
        }
        if(inv != null) {
            for(int i = 0; i < inv.getSize(); i++) {
                ItemStack invItem = inv.getItem(i);
                if(invItem != null && invItem.getType().equals(heistObjectMaterial)) {
                    inv.remove(invItem);
                }
            }
        }
    }

    private void startTimedTasks() {
        Bukkit.getScheduler().runTaskTimer(this, () -> scoreboardManager.getPlayerScoreboards().values().forEach(PlayerScoreboard::update), 0L, 1L); // Very fast, every tick.

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if(this.getTimeTillEnd() < 60*60) {
                playerManager.highlightAll();
            }
        }, 0L, 20L);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if(this.getTimeTillEnd() <= 5*60*60) {
                worldBorderManager.startShrinking();
            }
        }, 0L, 20L);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new WinnerAnnouncer(this), 0L, 20L);

        if(config.getBoolean("realTimeCycle.active")) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new RealTimeCycle(this), 0, 20L);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getEntity().removeMetadata("nl._99th_dutchies.halloween_heist.hasUsedKit", this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        scoreboardManager.addToPlayerCache(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        scoreboardManager.removeFromPlayerCache(p);
    }
}