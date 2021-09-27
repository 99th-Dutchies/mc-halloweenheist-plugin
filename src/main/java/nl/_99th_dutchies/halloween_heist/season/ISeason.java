package nl._99th_dutchies.halloween_heist.season;

import org.bukkit.Material;

public interface ISeason {
    Material getHeistObjectMaterial();
    String getHeistObjectName();
    void spawnHeistObject();
    void sendWinnerMessage(String username);
}
