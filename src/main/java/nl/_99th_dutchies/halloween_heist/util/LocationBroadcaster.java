package nl._99th_dutchies.halloween_heist.util;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;

public class LocationBroadcaster implements Runnable {
    private final HalloweenHeistPlugin plugin;

    public LocationBroadcaster(HalloweenHeistPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        LocalDateTime start = LocalDateTime.parse(this.plugin.config.getString("gameStart", "2021-10-31 00:00:00"));
        LocalDateTime time = LocalDateTime.now(ZoneId.of(this.plugin.config.getString("timezone", "UTC")));
        String lastBroadcastTimeString = this.plugin.heistState.getString("lastBroadcast.time");
        LocalDateTime lastBroadcastTime = null;
        if(lastBroadcastTimeString != null) {
            lastBroadcastTime = LocalDateTime.parse(lastBroadcastTimeString);
        }

        if(time.getSecond() == 0 && time.getMinute() == 0 && (time.isEqual(start) || time.isAfter(start)) &&
                (lastBroadcastTime == null || time.isAfter(lastBroadcastTime))) {
            this.plugin.heistState.set("lastBroadcast.time", time.toString());

            Location broadcastLocation = this.calcLocation();
            this.plugin.heistState.set("lastBroadcast.location", broadcastLocation);

            if(broadcastLocation == null) {
                Bukkit.broadcastMessage("The " + this.plugin.season.getHeistObjectName() + " is very well hidden and could not be traced");
            } else {
                Bukkit.broadcastMessage("The " + this.plugin.season.getHeistObjectName() + " can be found somewhere around [X:" + ((int) broadcastLocation.getX()) + ",Z:" + ((int) broadcastLocation.getZ()) + "]");
            }

            if(time.getHour() == 13) {
                for(Player p : this.plugin.getServer().getOnlinePlayers()) {
                    p.playSound(p.getLocation(), "heist.its_heist_time", 1, 1);
                }
            } else {
                for(Player p : this.plugin.getServer().getOnlinePlayers()) {
                    p.playSound(p.getLocation(), "heist.halloweeeen", 1, 1);
                }
            }
        }
    }

    private Location calcLocation() {
        Random rand = new Random();
        Location broadcastLocation = new Location(this.plugin.mainWorld, 0, 0, 0);
        Location currentLocation = this.plugin.heistObjectLocation.getLocation();

        if(currentLocation == null) return null;

        int randX = (rand.nextInt(10)) * (rand.nextBoolean() ? 1 : -1);
        int randZ = (rand.nextInt(10)) * (rand.nextBoolean() ? 1 : -1);
        broadcastLocation.setX(currentLocation.getX() + randX);
        broadcastLocation.setZ(currentLocation.getZ() + randZ);

        return broadcastLocation;
    }
}
