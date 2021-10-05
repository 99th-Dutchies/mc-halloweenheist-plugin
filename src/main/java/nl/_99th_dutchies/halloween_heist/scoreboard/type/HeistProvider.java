package nl._99th_dutchies.halloween_heist.scoreboard.type;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.beproud.scoreboard.ScoreboardProvider;
import de.beproud.scoreboard.ScoreboardText;

public class HeistProvider extends ScoreboardProvider {

    @Override
    public String getTitle(Player p) {
        return MessageFormat.format("{0}{1}Halloween Heist", ChatColor.GOLD, ChatColor.BOLD);
    }

    /**
     * You can use up to 15 Scoreboard lines.
     * Max-length of your text is 32.
     */
    @Override
    public List<ScoreboardText> getLines(Player p) {
        List<ScoreboardText> lines = new ArrayList<>();

        LocalDateTime end = LocalDateTime.parse("2021-10-31T00:00:00");
        long timeTillEnd = LocalDateTime.now().until(end, ChronoUnit.SECONDS);

        lines.add(new ScoreboardText(""));
        lines.add(new ScoreboardText(MessageFormat.format("{0}{1}Time left:", ChatColor.RED, ChatColor.BOLD)));
        lines.add(new ScoreboardText(
                MessageFormat.format(ChatColor.YELLOW + "{0}:{1}:{2}",
                        StringUtils.leftPad((int) Math.floor(timeTillEnd / 3600) + "", 2, "0"),
                        StringUtils.leftPad((int) Math.floor((timeTillEnd % 3600) / 60) + "", 2, "0"),
                        StringUtils.leftPad((int) Math.floor(timeTillEnd % 60) + "", 2, "0"))));

        HalloweenHeistPlugin plugin = HalloweenHeistPlugin.getPlugin(HalloweenHeistPlugin.class);

        lines.add(new ScoreboardText(""));
        lines.add(new ScoreboardText(MessageFormat.format("{0}{1}Last location:", ChatColor.RED, ChatColor.BOLD)));
        if(plugin.heistState.getInt("lastBroadcast.hour", -1) < 0) {
            lines.add(new ScoreboardText(ChatColor.YELLOW + "Unknown"));
        } else {
            lines.add(new ScoreboardText(ChatColor.YELLOW + "X:" + ((int) plugin.heistState.getInt("lastBroadcast.location.x"))));
            lines.add(new ScoreboardText(ChatColor.YELLOW + "Z:" + ((int) plugin.heistState.getInt("lastBroadcast.location.Z"))));
        }

        lines.add(new ScoreboardText(""));
        lines.add(new ScoreboardText(MessageFormat.format("{0}99th-dutchies.nl", ChatColor.GOLD)));

        return lines;
    }
}
