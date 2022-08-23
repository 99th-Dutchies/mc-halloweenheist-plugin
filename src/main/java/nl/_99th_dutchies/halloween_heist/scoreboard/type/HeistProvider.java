package nl._99th_dutchies.halloween_heist.scoreboard.type;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.apache.commons.lang.StringUtils;
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
        HalloweenHeistPlugin plugin = HalloweenHeistPlugin.getPlugin(HalloweenHeistPlugin.class);

        List<ScoreboardText> lines = new ArrayList<>();

        long timeTillEnd = plugin.getTimeTillEnd();

        if(timeTillEnd >= 0) {
            lines.add(new ScoreboardText(""));
            lines.add(new ScoreboardText(MessageFormat.format("{0}{1}Time left:", ChatColor.RED, ChatColor.BOLD)));
            lines.add(new ScoreboardText(
                    MessageFormat.format(ChatColor.YELLOW + "{0}:{1}:{2}",
                            StringUtils.leftPad((int) Math.floor(timeTillEnd / 3600.0F) + "", 2, "0"),
                            StringUtils.leftPad((int) Math.floor((timeTillEnd % 3600.0F) / 60) + "", 2, "0"),
                            StringUtils.leftPad((int) Math.floor(timeTillEnd % 60) + "", 2, "0"))));
        } else {
            lines.add(new ScoreboardText(""));
            lines.add(new ScoreboardText(MessageFormat.format("{0}{1}Time left:", ChatColor.RED, ChatColor.BOLD)));
            lines.add(new ScoreboardText(ChatColor.YELLOW + "Game Over!"));
        }

        lines.add(new ScoreboardText(""));
        lines.add(new ScoreboardText(MessageFormat.format("{0}{1}Last location:", ChatColor.RED, ChatColor.BOLD)));
        boolean hasLocation = p.hasMetadata("nl._99th_dutchies.halloween_heist.location.time");
        
        if(hasLocation) {
            lines.add(new ScoreboardText(ChatColor.YELLOW + "Unknown"));
        } else {
            int lastLocationX = p.getMetadata("nl._99th_dutchies.halloween_heist.location.x").get(0).asInt();
            int lastLocationY = p.getMetadata("nl._99th_dutchies.halloween_heist.location.y").get(0).asInt();
            int lastLocationZ = p.getMetadata("nl._99th_dutchies.halloween_heist.location.z").get(0).asInt();

            lines.add(new ScoreboardText(ChatColor.YELLOW + "X:" + lastLocationX));
            lines.add(new ScoreboardText(ChatColor.YELLOW + "Y:" + lastLocationY));
            lines.add(new ScoreboardText(ChatColor.YELLOW + "Z:" + lastLocationZ));
        }

        lines.add(new ScoreboardText(""));
        lines.add(new ScoreboardText(MessageFormat.format("{0}99th-dutchies.nl", ChatColor.GOLD)));

        return lines;
    }
}
