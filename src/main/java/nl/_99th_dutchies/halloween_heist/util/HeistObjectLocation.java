package nl._99th_dutchies.halloween_heist.util;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HeistObjectLocation {
    private final HalloweenHeistPlugin plugin;
    public Location location;
    public HeistObjectContainer container;
    public Player lastPlayer = null;

    public HeistObjectLocation(HalloweenHeistPlugin plugin) {
        this.plugin = plugin;
        this.load();
    }

    private void load() {
        this.location = this.plugin.heistState.getLocation("heistObject.location");

        switch(this.plugin.heistState.getString("heistObject.container")) {
            case "PLAYER":
                this.container = HeistObjectContainer.PLAYER;
                break;
            case "STORAGE_BLOCK":
                this.container = HeistObjectContainer.STORAGE_BLOCK;
                break;
            default:
            case "DROPPED":
                this.container = HeistObjectContainer.DROPPED;
                break;
        }

        String lp = this.plugin.heistState.getString("heistObject.lastPlayer");
        if(StringUtils.isEmpty(lp)) {
            this.lastPlayer = null;
        } else {
            this.lastPlayer = Bukkit.getPlayer(lp);
        }
    }

    public void update(Location l, HeistObjectContainer c) {
        this.location = l;
        this.container = c;

        this.plugin.heistState.set("heistObject.location", l);
        this.plugin.heistState.set("heistObject.container", c.name());
    }

    public void update(Location l, HeistObjectContainer c, Player p) {
        this.location = l;
        this.container = c;
        this.lastPlayer = p;

        this.plugin.heistState.set("heistObject.location", l);
        this.plugin.heistState.set("heistObject.container", c == null ? null : c.name());
        this.plugin.heistState.set("heistObject.lastPlayer", p == null ? null : p.getUniqueId().toString());
    }

    public void find(Material heistObject) {
        if(this.container == null) {
            System.out.println("Heist Object not found");
            return;
        }

        switch (this.container) {
            case PLAYER:
                for(Entity worldEntity : this.plugin.mainWorld.getEntities()) {
                    if (worldEntity instanceof Player) {
                        for (ItemStack invItemStack : ((Player) worldEntity).getInventory()) {
                            if (invItemStack != null && invItemStack.getType().equals(heistObject)) {
                                System.out.println("Heist Object found with player " + ((Player) worldEntity).getDisplayName() + " at [" + this.location.getX() + "," + this.location.getY() + "," + this.location.getZ() + "]");
                                this.update(worldEntity.getLocation(), HeistObjectContainer.PLAYER);
                                return;
                            }
                        }
                    }
                }
                break;
            case STORAGE_BLOCK:
                System.out.println("Heist Object found with block at [" + this.location.getX() + "," + this.location.getY() + "," + this.location.getZ() +"]");
                return;
            case DROPPED:
                System.out.println("Heist Object dropped at [" + this.location.getX() + "," + this.location.getY() + "," + this.location.getZ() +"]");
                return;
            default:
                System.out.println("Heist Object not found");
                return;
        }

        System.out.println("Heist Object not found");
        this.update(null, null);
    }
}
