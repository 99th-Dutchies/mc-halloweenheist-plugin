package nl._99th_dutchies.halloween_heist.scoreboard;

import java.util.HashMap;
import java.util.List;

import nl._99th_dutchies.halloween_heist.scoreboard.type.HeistProvider;
import org.bukkit.entity.Player;

import de.beproud.scoreboard.ScoreboardProvider;
import de.beproud.scoreboard.ScoreboardText;

public class DefaultProvider extends ScoreboardProvider {
    private HashMap<ProviderType, ScoreboardProvider> defaultProvider;

    public DefaultProvider() {
        this.defaultProvider = new HashMap<>();

        this.defaultProvider.put(ProviderType.HEIST, new HeistProvider());
    }

    @Override
    public String getTitle(Player p) {
        return this.defaultProvider.get(ProviderType.HEIST).getTitle(p);
    }

    @Override
    public List<ScoreboardText> getLines(Player p) {
        return this.defaultProvider.get(ProviderType.HEIST).getLines(p);
    }

    public enum ProviderType {
        HEIST;
    }

}
