package nl._99th_dutchies.halloween_heist;

import nl._99th_dutchies.halloween_heist.command.CommandKit;
import nl._99th_dutchies.halloween_heist.command.CommandTrade;
import nl._99th_dutchies.halloween_heist.listener.AntiGriefingListener;
import nl._99th_dutchies.halloween_heist.listener.HeistObjectSavingListener;
import nl._99th_dutchies.halloween_heist.listener.HeistObjectTrackingListener;
import nl._99th_dutchies.halloween_heist.scoreboard.ScoreboardManager;
import nl._99th_dutchies.halloween_heist.season.ISeason;
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

public class HalloweenHeistPlugin extends JavaPlugin implements Listener {
    private static ScoreboardManager scoreboardManager;

    public final FileConfiguration config = getConfig();
    public HeistState heistState;
    public HeistObjectLocation heistObjectLocation;
    public World mainWorld;
    public ISeason season;

    @Override
    public void onEnable() {
        config.options().copyDefaults(true);
        saveConfig();

        this.mainWorld = this.getServer().getWorld(this.config.getString("worldName"));
        this.setSeason();

        this.heistState = new HeistState(this);
        this.heistObjectLocation = new HeistObjectLocation(this);

        this.scoreboardManager = new ScoreboardManager();
        this.startUpdateTask();

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new HeistObjectTrackingListener(this), this);
        Bukkit.getPluginManager().registerEvents(new HeistObjectSavingListener(this), this);
        Bukkit.getPluginManager().registerEvents(new AntiGriefingListener(this), this);

        this.getCommand("kit").setExecutor(new CommandKit(this));
        this.getCommand("trade").setExecutor(new CommandTrade(this));

        if(!heistState.getBoolean("itemLoaded")) {
            this.season.spawnHeistObject();
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new LocationBroadcaster(this), 0L, 20L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new WinnerAnnouncer(this), 0L, 20L);

        if(config.getBoolean("realTimeCycle.active")) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new RealTimeCycle(this), 0, 20L);
        }
    }

    public void setSeason() {
        switch(this.config.getInt("season")) {
            default:
            case 1:
                this.season = new Season1(this);
                break;
        }
    }

    private void startUpdateTask() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            scoreboardManager.getPlayerScoreboards().values().forEach((scoreboard) -> scoreboard.update());
        }, 0L, 1L); // Very fast, every tick.
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