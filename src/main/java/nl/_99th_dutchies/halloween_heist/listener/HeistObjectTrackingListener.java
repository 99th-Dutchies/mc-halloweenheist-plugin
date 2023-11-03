package nl._99th_dutchies.halloween_heist.listener;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

/*
 * Heist Object moving events:
 * - drop (PlayerDropItemEvent)
 * - pickup (PlayerPickupItemEvent)
 * - place / take (InventoryMoveItemEvent)
 */

public class HeistObjectTrackingListener implements Listener {
    private final HalloweenHeistPlugin plugin;

    public HeistObjectTrackingListener(HalloweenHeistPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        System.out.println("Entity (" + event.getEntityType() + ") picked up an object: " + event.getItem().getItemStack().getType());
        if (this.plugin.season.isHeistObject(event.getItem().getItemStack())) {
            if (event.getEntityType().equals(EntityType.PLAYER)) {
                this.plugin.heistObjectLocation.updatePlayer(event.getEntity().getLocation(), (Player) event.getEntity());
                System.out.println("Player picked up HeistObject");
            }
        }
    }

    @EventHandler
    public void onInventoryPickupItem(InventoryPickupItemEvent event) {
        if (this.plugin.season.isHeistObject(event.getItem().getItemStack())) {
            this.plugin.heistObjectLocation.updateInventoryHolder(event.getInventory().getHolder());
            System.out.println("Inventory picked up HeistObject");
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        System.out.println("Player dropped an object: " + event.getItemDrop().getItemStack().getType());

        if (this.plugin.season.isHeistObject(event.getItemDrop().getItemStack())) {
            this.plugin.heistObjectLocation.updateDropped(event.getItemDrop().getLocation(), event.getItemDrop());
            System.out.println("Player dropped HeistObject");
        }
    }

    @EventHandler
    public void onEntityDropItem(EntityDropItemEvent event) {
        System.out.println("Entity (" + event.getEntityType() + ") dropped an object: " + event.getItemDrop().getItemStack().getType());

        if (this.plugin.season.isHeistObject(event.getItemDrop().getItemStack())) {
            this.plugin.heistObjectLocation.updateDropped(event.getItemDrop().getLocation(), event.getItemDrop());
            System.out.println("Entity dropped HeistObject");
        }
    }

    @EventHandler
    public void onBlockDropItem(BlockDropItemEvent event) {
        System.out.println("Block (" + event.getBlockState().getType() + ") dropped object(s): " + event.getItems());

        for (Item droppedItem : event.getItems()) {
            if (this.plugin.season.isHeistObject(droppedItem.getItemStack())) {
                this.plugin.heistObjectLocation.updateDropped(droppedItem.getLocation(), droppedItem);
                System.out.println("Block dropped HeistObject");
            }
        }
    }

    @EventHandler
    public void onItemSpawnEvent(ItemSpawnEvent event) {
        System.out.println("Item spawned into world: " + event.getEntityType());

        if (this.plugin.season.isHeistObject(event.getEntity().getItemStack())) {
            this.plugin.heistObjectLocation.updateDropped(event.getEntity().getLocation(), event.getEntity());
            System.out.println("Spawned HeistObject into world");
        }
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        if (this.plugin.season.isHeistObject(event.getItem())) {
            this.plugin.heistObjectLocation.updateInventoryHolder(event.getDestination().getHolder());
            System.out.println("HeistObject moved in inventory");
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!event.getInventory().getHolder().equals(event.getPlayer())) {
            for(ItemStack invItemStack : event.getInventory()) {
                if(invItemStack != null && this.plugin.season.isHeistObject(invItemStack)) {
                    if(event.getInventory().getType().equals(InventoryType.PLAYER)) {
                        this.plugin.heistObjectLocation.updatePlayer(event.getPlayer().getLocation(), (Player) event.getPlayer());
                        System.out.println("HeistObject moved into inventory of player");
                    } else {
                        this.plugin.heistObjectLocation.updateInventoryHolder(event.getInventory().getHolder());
                        System.out.println("HeistObject moved into InventoryHolder");
                    }
                    return;
                }
            }
            for(ItemStack invItemStack : event.getPlayer().getInventory()) {
                if(invItemStack != null && this.plugin.season.isHeistObject(invItemStack)) {
                    this.plugin.heistObjectLocation.updatePlayer(event.getPlayer().getLocation(), (Player) event.getPlayer());
                    System.out.println("HeistObject moved into player inventory");
                    return;
                }
            }
        }
    }
}