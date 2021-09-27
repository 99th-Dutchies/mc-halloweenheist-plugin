package nl._99th_dutchies.halloween_heist.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HeistObjectLocation {
    public Location location;
    public HeistObjectContainer container;
    public Player lastPlayer = null;
    private final World world;

    public HeistObjectLocation(World w, Location l, HeistObjectContainer c) {
        this.world = w;
        this.location = l;
        this.container = c;
    }

    public void update(Location l, HeistObjectContainer c) {
        this.location = l;
        this.container = c;
    }

    public void update(Location l, HeistObjectContainer c, Player p) {
        this.location = l;
        this.container = c;
        this.lastPlayer = p;
    }

    public void find(Material heistObject) {
        ItemStack dummyStack = new ItemStack(heistObject, 1);

        if(this.container == null) {
            System.out.println("Heist Object not found");
            return;
        }

        switch (this.container) {
            case PLAYER:
                for(Entity worldEntity : world.getEntities()) {
                    if (worldEntity instanceof Player) {
                        for (ItemStack invItemStack : ((Player) worldEntity).getInventory()) {
                            if (invItemStack != null && invItemStack.isSimilar(dummyStack)) {
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
