package nl._99th_dutchies.halloween_heist.util;

import org.bukkit.Material;

public class InventoryItem {
    public final Material material;
    public final int count;
    public final String name;

    public InventoryItem(Material material, int count) {
        this.material = material;
        this.count = count;
        this.name = null;
    }

    public InventoryItem(Material material, int count, String name) {
        this.material = material;
        this.count = count;
        this.name = name;
    }
}
