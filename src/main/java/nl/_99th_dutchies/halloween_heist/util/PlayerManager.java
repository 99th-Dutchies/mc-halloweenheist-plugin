package nl._99th_dutchies.halloween_heist.util;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerManager {
    private final HalloweenHeistPlugin plugin;

    public PlayerManager(HalloweenHeistPlugin plugin) {
        this.plugin = plugin;
    }

    public void highlightAll() {
        for(Player player : plugin.mainWorld.getPlayers()){
            player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, Integer.MAX_VALUE, true, true, true));
        }
    }
}
