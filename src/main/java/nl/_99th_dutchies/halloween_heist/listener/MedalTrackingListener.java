package nl._99th_dutchies.halloween_heist.listener;

import nl._99th_dutchies.halloween_heist.HalloweenHeist;
import nl._99th_dutchies.halloween_heist.util.MedalContainer;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

/*
 * Medal moving events:
 * - drop (PlayerDropItemEvent)
 * - pickup (PlayerPickupItemEvent)
 * - place / take (InventoryMoveItemEvent)
 */

public class MedalTrackingListener implements Listener {
    private HalloweenHeist plugin;

    public MedalTrackingListener(HalloweenHeist plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if(event.getItem().getItemStack().equals(new ItemStack(Material.TOTEM_OF_UNDYING, 1))) {
            if(event.getEntityType() == EntityType.PLAYER) {
                this.plugin.medalLocation.update(event.getEntity().getLocation(), MedalContainer.PLAYER);
            }
        }
    }

    @EventHandler
    public void onInventoryPickupItem(InventoryPickupItemEvent event) {
        if(event.getItem().getItemStack().equals(new ItemStack(Material.TOTEM_OF_UNDYING, 1))) {
            this.plugin.medalLocation.update(event.getInventory().getLocation(), MedalContainer.STORAGE_BLOCK);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        for(ItemStack drop : event.getDrops()) {
            if(drop.isSimilar(new ItemStack(Material.TOTEM_OF_UNDYING, 1))) {
                drop.setAmount(0);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if(event.getItemDrop().getItemStack().isSimilar(new ItemStack(Material.TOTEM_OF_UNDYING, 1))) {
            this.plugin.medalLocation.update(event.getItemDrop().getLocation(), MedalContainer.DROPPED);
        }
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        if(event.getItem().isSimilar(new ItemStack(Material.TOTEM_OF_UNDYING, 1))) {
            this.plugin.medalLocation.update(event.getDestination().getLocation(), MedalContainer.STORAGE_BLOCK);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!event.getInventory().getHolder().equals(event.getPlayer())) {
            ItemStack dummy = new ItemStack(Material.TOTEM_OF_UNDYING, 1);

            for(ItemStack invItemStack : event.getInventory()) {
                if(invItemStack != null && invItemStack.isSimilar(dummy)) {
                    if(event.getInventory().getType() == InventoryType.PLAYER) {
                        this.plugin.medalLocation.update(event.getInventory().getLocation(), MedalContainer.PLAYER);
                    } else {
                        this.plugin.medalLocation.update(event.getInventory().getLocation(), MedalContainer.STORAGE_BLOCK);
                    }
                    return;
                }
            }
            for(ItemStack invItemStack : event.getPlayer().getInventory()) {
                if(invItemStack != null && invItemStack.isSimilar(dummy)) {
                    if(event.getInventory().getType() == InventoryType.PLAYER) {
                        this.plugin.medalLocation.update(event.getInventory().getLocation(), MedalContainer.PLAYER);
                    }
                    return;
                }
            }
        }
    }
}