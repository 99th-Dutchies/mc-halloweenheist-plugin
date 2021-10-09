package nl._99th_dutchies.halloween_heist.listener;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class AntiGriefingListener implements Listener {
    private final HalloweenHeistPlugin plugin;

    public AntiGriefingListener(HalloweenHeistPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(this.isProtected(this.plugin.config.getInt("antiGriefing", 0), event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(this.isProtected(this.plugin.config.getInt("antiGriefing", 0), event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if(this.isProtected(this.plugin.config.getInt("antiGriefing", 0), event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        if(this.isProtected(this.plugin.config.getInt("antiGriefing", 0), event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        if(this.isProtected(this.plugin.config.getInt("antiGriefing", 0), event.getEntity().getLocation())) {
            event.setCancelled(true);
        }
    }

    private boolean isProtected(int antiGriefing, Location location) {
        if(location.getX() > antiGriefing * -1 && location.getX() < antiGriefing &&
                location.getZ() > antiGriefing * -1 && location.getZ() < antiGriefing) {
            return true;
        }

        return false;
    }
}