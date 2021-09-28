package nl._99th_dutchies.halloween_heist.listener;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import nl._99th_dutchies.halloween_heist.util.HeistObjectContainer;
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
        if(event.getItem().getItemStack().equals(new ItemStack(this.plugin.season.getHeistObjectMaterial(), 1))) {
            if(event.getEntityType() == EntityType.PLAYER) {
                this.plugin.heistObjectLocation.update(event.getEntity().getLocation(), HeistObjectContainer.PLAYER, (Player) event.getEntity());
            }
        }
    }

    @EventHandler
    public void onInventoryPickupItem(InventoryPickupItemEvent event) {
        if(event.getItem().getItemStack().equals(new ItemStack(this.plugin.season.getHeistObjectMaterial(), 1))) {
            this.plugin.heistObjectLocation.update(event.getInventory().getLocation(), HeistObjectContainer.STORAGE_BLOCK);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        for(ItemStack drop : event.getDrops()) {
            if(drop.isSimilar(new ItemStack(this.plugin.season.getHeistObjectMaterial(), 1))) {
                drop.setAmount(0);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if(event.getItemDrop().getItemStack().isSimilar(new ItemStack(this.plugin.season.getHeistObjectMaterial(), 1))) {
            if(event.getPlayer().isDead()) {
                this.plugin.heistObjectLocation.update(event.getItemDrop().getLocation(), HeistObjectContainer.DROPPED, null);
            } else {
                this.plugin.heistObjectLocation.update(event.getItemDrop().getLocation(), HeistObjectContainer.DROPPED, event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        if(event.getItem().isSimilar(new ItemStack(this.plugin.season.getHeistObjectMaterial(), 1))) {
            this.plugin.heistObjectLocation.update(event.getDestination().getLocation(), HeistObjectContainer.STORAGE_BLOCK);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!event.getInventory().getHolder().equals(event.getPlayer())) {
            ItemStack dummy = new ItemStack(this.plugin.season.getHeistObjectMaterial(), 1);

            for(ItemStack invItemStack : event.getInventory()) {
                if(invItemStack != null && invItemStack.isSimilar(dummy)) {
                    if(event.getInventory().getType() == InventoryType.PLAYER) {
                        this.plugin.heistObjectLocation.update(event.getInventory().getLocation(), HeistObjectContainer.PLAYER, (Player) event.getPlayer());
                    } else {
                        this.plugin.heistObjectLocation.update(event.getInventory().getLocation(), HeistObjectContainer.STORAGE_BLOCK, (Player) event.getPlayer());
                    }
                    return;
                }
            }
            for(ItemStack invItemStack : event.getPlayer().getInventory()) {
                if(invItemStack != null && invItemStack.isSimilar(dummy)) {
                    if(event.getInventory().getType() == InventoryType.PLAYER) {
                        this.plugin.heistObjectLocation.update(event.getInventory().getLocation(), HeistObjectContainer.PLAYER, (Player) event.getPlayer());
                    }
                    return;
                }
            }
        }
    }
}