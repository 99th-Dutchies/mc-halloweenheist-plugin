package nl._99th_dutchies.halloween_heist;

import nl._99th_dutchies.halloween_heist.command.CommandKit;
import nl._99th_dutchies.halloween_heist.command.CommandTrade;
import nl._99th_dutchies.halloween_heist.listener.AntiGriefingListener;
import nl._99th_dutchies.halloween_heist.listener.HeistObjectSavingListener;
import nl._99th_dutchies.halloween_heist.listener.HeistObjectTrackingListener;
import nl._99th_dutchies.halloween_heist.scoreboard.ScoreboardManager;
import nl._99th_dutchies.halloween_heist.season.ASeason;
import nl._99th_dutchies.halloween_heist.season.Season1;
import nl._99th_dutchies.halloween_heist.util.*;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class HalloweenHeistPlugin extends JavaPlugin implements Listener {
    private static ScoreboardManager scoreboardManager;
    private static PlayerManager playerManager;
    private static WorldBorderManager worldBorderManager;

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

    private void startTimedTasks() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            scoreboardManager.getPlayerScoreboards().values().forEach((scoreboard) -> scoreboard.update());
        }, 0L, 1L); // Very fast, every tick.

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if(this.getTimeTillEnd() < 60*60) {
                playerManager.highlightAll();
            }
        }, 0L, 20L);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if(this.getTimeTillEnd() == 5*60*60) {
                worldBorderManager.startShrinking();
            }
        }, 0L, 20L);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new LocationBroadcaster(this), 0L, 20L);

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