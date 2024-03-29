package nl._99th_dutchies.halloween_heist.util;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class WinnerAnnouncer implements Runnable {
    private final HalloweenHeistPlugin plugin;

    public WinnerAnnouncer(HalloweenHeistPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        LocalDateTime end = this.plugin.getGameEnd();
        LocalDateTime time = LocalDateTime.now(ZoneId.of(this.plugin.config.getString("timezone", "UTC")));

        if(!this.plugin.heistState.getBoolean("announced", false) && (time.isEqual(end) || time.isAfter(end))) {
            this.plugin.season.sendWinnerMessage(this.plugin.heistObjectLocation.lastPlayer);

            for(Player p : this.plugin.getServer().getOnlinePlayers()) {
                p.playSound(p.getLocation(), "heist.heists_are_dumb", 1, 1);
                p.setGameMode(GameMode.CREATIVE);
            }

            this.plugin.heistState.set("announced", true);
        }
    }
}
