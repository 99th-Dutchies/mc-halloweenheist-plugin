package nl._99th_dutchies.halloween_heist;

import de.beproud.scoreboard.PlayerScoreboard;
import nl._99th_dutchies.halloween_heist.command.*;
import nl._99th_dutchies.halloween_heist.listener.*;
import nl._99th_dutchies.halloween_heist.scoreboard.ScoreboardManager;
import nl._99th_dutchies.halloween_heist.season.*;
import nl._99th_dutchies.halloween_heist.team.Team;
import nl._99th_dutchies.halloween_heist.team.TeamManager;
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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;

public class HalloweenHeistPlugin extends JavaPlugin implements Listener {
    private ScoreboardManager scoreboardManager;
    private PlayerManager playerManager;
    private WorldBorderManager worldBorderManager;
    public TeamManager teamManager;

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
        this.mainWorld = this.getServer().getWorld(this.config.getString("worldName", "world"));
        this.setSeason();

        // Load managers
        this.playerManager = new PlayerManager(this);
        this.worldBorderManager = new WorldBorderManager(this);
        this.scoreboardManager = new ScoreboardManager();
        this.teamManager = new TeamManager(this);

        // Init events
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new HeistObjectTrackingListener(this), this);
        Bukkit.getPluginManager().registerEvents(new HeistObjectSavingListener(this), this);
        Bukkit.getPluginManager().registerEvents(new AntiGriefingListener(this), this);

        // Init recipes
        this.removeRecipe(this.season.getHeistObjectMaterial());

        // Init commands
        this.getCommand("kit").setExecutor(new CommandKit(this));
        this.getCommand("location").setExecutor(new CommandLocation(this));
        this.getCommand("trade").setExecutor(new CommandTrade(this));
        this.getCommand("team").setExecutor(new CommandTeam(this));

        // Set heist data
        this.heistState = new HeistState(this);
        this.heistObjectLocation = new HeistObjectLocation(this);
        if(!heistState.getBoolean("itemLoaded")) {
            this.season.spawnHeistObject();
        }
        worldBorderManager.create();
        if(this.config.getBoolean("teams.enabled")) {
            this.teamManager.load();
        }

        // Start timers
        this.startTimedTasks();
    }

    public void setSeason() {
        switch(this.config.getInt("season")) {
            default:
            case 1:
                this.season = new Season1(this);
                break;
            case 2:
                this.season = new Season2(this);
                break;
            case 3:
                this.season = new Season3(this);
                break;
        }
    }

    public LocalDateTime getLocalNow() {
        return LocalDateTime.now(ZoneId.of(this.config.getString("timezone", "UTC")));
    }

    public LocalDateTime getGameStart() {
        int year = this.getLocalNow().getYear();
        return LocalDateTime.parse(this.config.getString("gameStart", year + "-10-31T00:00:00"));
    }

    public LocalDateTime getGameEnd() {
        int year = this.getLocalNow().getYear();
        return LocalDateTime.parse(this.config.getString("gameEnd", year + "-11-01T00:00:00"));
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
        switch(this.heistObjectLocation.container) {
            case DROPPED:
                for(Entity entity : this.mainWorld.getNearbyEntities(this.heistObjectLocation.location, 2, 2, 2)) {
                    if(entity.getType().equals(EntityType.DROPPED_ITEM) && this.season.isHeistObject(((Item) entity).getItemStack())) {
                        entity.remove();
                    }
                }
                return;
            case STORAGE_BLOCK:
                inv = ((BlockInventoryHolder) this.heistObjectLocation.location.getBlock().getState()).getInventory();
                break;
            case STORAGE_ENTITY:
                inv = ((InventoryHolder) this.heistObjectLocation.storingEntity).getInventory();
                break;
            case PLAYER:
                Player p = this.heistObjectLocation.lastPlayer;
                PlayerInventory pi = p.getInventory();
                if (this.season.isHeistObject(pi.getItemInMainHand())) {
                    pi.setItemInMainHand(null);
                }
                if (this.season.isHeistObject(pi.getItemInOffHand())) {
                    pi.setItemInOffHand(null);
                }
                inv = pi;
                break;
        }
        if(inv != null) {
            for(int i = 0; i < inv.getSize(); i++) {
                ItemStack invItem = inv.getItem(i);
                if (invItem != null && this.season.isHeistObject(invItem)) {
                    inv.removeItem(invItem);
                }
            }
        }
    }

    private void removeRecipe(Material material) {
        Iterator<Recipe> recipeIterator = this.getServer().recipeIterator();
        while (recipeIterator.hasNext()) {
            Recipe r = recipeIterator.next();
            if (r.getResult().isSimilar(new ItemStack(material))) {
                recipeIterator.remove();
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
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if(event.getMessage().startsWith("!")) {
            event.setCancelled(true);

            Team team = this.teamManager.getTeamForPlayer(event.getPlayer());
            if (team == null) {
                return;
            }
            String message = ChatColor.GOLD + "[TEAM] " + ChatColor.RESET + event.getPlayer().getDisplayName() + ": " + event.getMessage().substring(1);
            for (Player player : team.getPlayers()) {
                player.sendMessage(message);
            }
        }
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) {
            return;
        }

        Player attacker = (Player) event.getDamager();
        Player defender = (Player) event.getEntity();
        Team defenderTeam = this.teamManager.getTeamForPlayer(defender);
        if (defenderTeam.hasPlayer(attacker)) {
            event.setCancelled(true);
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
        teamManager.reloadPlayer(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        scoreboardManager.removeFromPlayerCache(p);
    }
}