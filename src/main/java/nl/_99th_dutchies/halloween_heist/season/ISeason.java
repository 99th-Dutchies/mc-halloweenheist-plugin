package nl._99th_dutchies.halloween_heist.season;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface ISeason {
    Material getHeistObjectMaterial();
    String getHeistObjectName();
    void spawnHeistObject();
    void sendWinnerMessage(Player winner);
}
