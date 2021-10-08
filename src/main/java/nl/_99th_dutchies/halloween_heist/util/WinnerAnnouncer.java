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
        LocalDateTime time = LocalDateTime.now(ZoneId.of(this.plugin.config.getString("timezone")));

        if(time.getSecond() == 0 && time.getMinute() == 0 && time.getHour() == 0) {
            this.plugin.season.sendWinnerMessage(this.plugin.heistObjectLocation.lastPlayer);

            for(Player p : this.plugin.getServer().getOnlinePlayers()) {
                p.playSound(p.getLocation(), "heist.heists_are_dumb", 1, 1);
                p.setGameMode(GameMode.CREATIVE);
            }
        }
    }
}
