package nl._99th_dutchies.halloween_heist.listener;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class HeistObjectSavingListener implements Listener {
    private final HalloweenHeistPlugin plugin;

    public HeistObjectSavingListener(HalloweenHeistPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.doDropHeistObject(event.getPlayer());
    }

    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!this.plugin.config.getBoolean("allowEnderchest") && event.getBlockPlaced().getType() == Material.ENDER_CHEST) {
            event.getBlockPlaced().getLocation().getBlock().setType(Material.CHEST);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity().getType().equals(EntityType.DROPPED_ITEM) &&
                ((Item)event.getEntity()).getItemStack().getType().equals(this.plugin.season.getHeistObjectMaterial())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {
        if(event.getEntity().getType().equals(EntityType.DROPPED_ITEM) &&
                ((Item)event.getEntity()).getItemStack().getType().equals(this.plugin.season.getHeistObjectMaterial())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event){
        if(event.getEntity().getType().equals(EntityType.DROPPED_ITEM) &&
                event.getEntity().getItemStack().getType().equals(this.plugin.season.getHeistObjectMaterial())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if(event.getItem().getItemStack().getType().equals(this.plugin.season.getHeistObjectMaterial()) &&
                event.getEntityType() != EntityType.PLAYER) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        if(event.getEntity() instanceof Player) return;

        for(ItemStack drop : event.getDrops()) {
            if(drop.getType().equals(this.plugin.season.getHeistObjectMaterial())) {
                drop.setAmount(0);
            }
        }
    }

    private void doDropHeistObject(Player p) {
        PlayerInventory pi = p.getInventory();
        Material heistObject = this.plugin.season.getHeistObjectMaterial();

        if(pi.getItemInMainHand().getType().equals(heistObject)) {
            p.dropItem(false);
        }
        if(pi.getItemInOffHand().getType().equals(heistObject)) {
            p.getWorld().dropItem(p.getLocation(), pi.getItemInOffHand());
            pi.setItemInOffHand(null);
        }
        for(int i = 0; i < 36; i++) {
            ItemStack piItem = pi.getItem(i);
            if(piItem != null && piItem.getType().equals(heistObject)) {
                p.getWorld().dropItem(p.getLocation(), piItem);
                pi.remove(piItem);
            }
        }
    }
}