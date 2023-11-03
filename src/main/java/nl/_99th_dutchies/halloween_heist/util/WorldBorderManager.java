package nl._99th_dutchies.halloween_heist.util;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.text.MessageFormat;

public class WorldBorderManager {
    private final HalloweenHeistPlugin plugin;
    private boolean shrinking = false;

    public WorldBorderManager(HalloweenHeistPlugin plugin) {
        this.plugin = plugin;
    }

    private void updateSize(int newSize, int seconds) {
        this.plugin.mainWorld.getWorldBorder().setSize(newSize, seconds);
        this.plugin.heistState.set("worldBorder.size", newSize);
    }

    public void create() {
        this.plugin.mainWorld.getWorldBorder().setCenter(0, 0);
        if(this.plugin.heistState.getInt("worldBorder.size") <= 0) {
            int setSize = this.plugin.config.getInt("worldDimensions") * 2;
            this.updateSize(setSize, 0);
        }
    }

    public void startShrinking() {
        if(!this.plugin.heistState.getBoolean("worldBorder.shrinking")) {
            this.plugin.heistState.set("worldBorder.shrinking", true);
            Bukkit.broadcastMessage(MessageFormat.format("{0}{1}The world border has started shrinking!", ChatColor.RED, ChatColor.BOLD));
        }

        if(!this.shrinking) {
            this.shrinking = true;
            System.out.println("Start shrinker");
            Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {
                if (this.plugin.getTimeTillEnd() < 5 * 60 * 60 && this.plugin.getTimeTillEnd() > 0) {
                    this.shrink(2, 60);
                }
            }, 0L, 60*20L);
        }
    }

    public void shrink(int shrinkSize, int seconds) {
        int curSize = this.plugin.heistState.getInt("worldBorder.size");
        int newSize = curSize - shrinkSize;

        this.updateSize(newSize, seconds);

        this.checkHeistItem();
    }

    private void checkHeistItem() {
        Location location = this.plugin.heistObjectLocation.getLocation();
        if(location != null && !this.plugin.mainWorld.getWorldBorder().isInside(location)) {
            System.out.println("HeistObject outside of worldBorder");
            this.plugin.respawnHeistObject();
        }
    }
}
