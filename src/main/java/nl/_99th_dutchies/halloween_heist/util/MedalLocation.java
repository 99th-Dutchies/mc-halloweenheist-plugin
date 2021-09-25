package nl._99th_dutchies.halloween_heist.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MedalLocation {
    public Location location;
    public MedalContainer container;
    private final World world;

    public MedalLocation(World w, Location l, MedalContainer c) {
        this.world = w;
        this.location = l;
        this.container = c;
    }

    public void update(Location l, MedalContainer c) {
        this.location = l;
        this.container = c;
    }

    public void findMedal() {
        ItemStack dummyStack = new ItemStack(Material.TOTEM_OF_UNDYING, 1);

        if(this.container == null) {
            System.out.println("Medal not found");
            return;
        }

        switch (this.container) {
            case PLAYER:
                for(Entity worldEntity : world.getEntities()) {
                    if (worldEntity instanceof Player) {
                        for (ItemStack invItemStack : ((Player) worldEntity).getInventory()) {
                            if (invItemStack != null && invItemStack.isSimilar(dummyStack)) {
                                System.out.println("Medal found with player " + ((Player) worldEntity).getDisplayName());
                                this.update(worldEntity.getLocation(), MedalContainer.PLAYER);
                                return;
                            }
                        }
                    }
                }
                break;
            case STORAGE_BLOCK:
                System.out.println("Medal found with block at [" + this.location.getX() + "," + this.location.getY() + "," + this.location.getZ() +"]");
                return;
            case DROPPED:
                System.out.println("Medal dropped at [" + this.location.getX() + "," + this.location.getY() + "," + this.location.getZ() +"]");
                return;
            default:
                System.out.println("Medal not found");
                return;
        }

        System.out.println("Medal not found");
        this.update(null, null);
    }
}
