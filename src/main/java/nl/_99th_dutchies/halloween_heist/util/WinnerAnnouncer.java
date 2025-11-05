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
        boolean forceEnd = this.plugin.heistState.getBoolean("forceEnd", true);

        if(!this.plugin.heistState.getBoolean("announced", false) && ((time.isEqual(end) || time.isAfter(end)) || forceEnd)) {
            if (!forceEnd) {
                this.plugin.season.sendWinnerMessage(this.plugin.heistObjectLocation.lastPlayer);
            } else {
                this.plugin.season.sendWinnerMessage(this.plugin.teamManager.getLastTeam().getPlayers().get(0));
            }

            for(Player p : this.plugin.getServer().getOnlinePlayers()) {
                p.playSound(p.getLocation(), "heist.heists_are_dumb", 1, 1);
                p.setGameMode(GameMode.CREATIVE);
            }

            this.plugin.heistState.set("announced", true);
        }
    }
}
