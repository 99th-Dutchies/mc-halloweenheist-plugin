package nl._99th_dutchies.halloween_heist.util;

import org.bukkit.Material;

public class ItemTrade {
    private final String name;
    private final Material sourceMaterial;
    private final int sourceCount;
    private final Material targetMaterial;
    private final int targetCount;

    public ItemTrade(String name, Material sourceMaterial, int sourceCount, Material targetMaterial, int targetCount) {
        this.name = name;
        this.sourceMaterial = sourceMaterial;
        this.sourceCount = sourceCount;
        this.targetMaterial = targetMaterial;
        this.targetCount = targetCount;
    }

    public String getName() {
        return name;
    }

    public Material getSourceMaterial() {
        return sourceMaterial;
    }

    public int getSourceCount() {
        return sourceCount;
    }

    public Material getTargetMaterial() {
        return targetMaterial;
    }

    public int getTargetCount() {
        return targetCount;
    }
}
