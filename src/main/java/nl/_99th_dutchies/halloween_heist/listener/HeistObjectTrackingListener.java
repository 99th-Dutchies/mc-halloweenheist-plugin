package nl._99th_dutchies.halloween_heist.listener;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
        if(event.getItem().getItemStack().getType().equals(this.plugin.season.getHeistObjectMaterial())) {
            if(event.getEntityType() == EntityType.PLAYER) {
                this.plugin.heistObjectLocation.updatePlayer(event.getEntity().getLocation(), (Player) event.getEntity());
            }
        }
    }

    @EventHandler
    public void onInventoryPickupItem(InventoryPickupItemEvent event) {
        if(event.getItem().getItemStack().getType().equals(this.plugin.season.getHeistObjectMaterial())) {
            this.plugin.heistObjectLocation.updateInventoryHolder(event.getInventory().getHolder());
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if(event.getItemDrop().getItemStack().getType().equals(this.plugin.season.getHeistObjectMaterial())) {
            this.plugin.heistObjectLocation.updateDropped(event.getItemDrop().getLocation());
        }
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        if(event.getItem().getType().equals(this.plugin.season.getHeistObjectMaterial())) {
            this.plugin.heistObjectLocation.updateInventoryHolder(event.getDestination().getHolder());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!event.getInventory().getHolder().equals(event.getPlayer())) {
            Material heistObject = this.plugin.season.getHeistObjectMaterial();

            for(ItemStack invItemStack : event.getInventory()) {
                if(invItemStack != null && invItemStack.getType().equals(heistObject)) {
                    if(event.getInventory().getType() == InventoryType.PLAYER) {
                        this.plugin.heistObjectLocation.updatePlayer(event.getPlayer().getLocation(), (Player) event.getPlayer());
                    } else {
                        this.plugin.heistObjectLocation.updateInventoryHolder(event.getInventory().getHolder());
                    }
                    return;
                }
            }
            for(ItemStack invItemStack : event.getPlayer().getInventory()) {
                if(invItemStack != null && invItemStack.getType().equals(heistObject)) {
                    this.plugin.heistObjectLocation.updatePlayer(event.getPlayer().getLocation(), (Player) event.getPlayer());
                    return;
                }
            }
        }
    }
}