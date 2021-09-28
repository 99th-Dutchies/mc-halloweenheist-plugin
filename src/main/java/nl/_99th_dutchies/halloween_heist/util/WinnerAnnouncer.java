package nl._99th_dutchies.halloween_heist.util;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;

public class WinnerAnnouncer implements Runnable {
    private HalloweenHeistPlugin plugin;

    public WinnerAnnouncer(HalloweenHeistPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        LocalDateTime time = LocalDateTime.now();

        if(time.getMinute() == 0 && time.getHour() == 0) {
            this.plugin.season.sendWinnerMessage(this.plugin.heistObjectLocation.lastPlayer);

            for(Player p : this.plugin.getServer().getOnlinePlayers()) {
                p.setGameMode(GameMode.CREATIVE);
            }
        }
    }
}
