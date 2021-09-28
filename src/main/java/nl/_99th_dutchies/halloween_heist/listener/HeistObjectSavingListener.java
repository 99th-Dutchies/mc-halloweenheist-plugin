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
    private HalloweenHeistPlugin plugin;

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
                ((Item)event.getEntity()).getItemStack().equals(new ItemStack(this.plugin.season.getHeistObjectMaterial(), 1))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {
        if(event.getEntity().getType().equals(EntityType.DROPPED_ITEM) &&
                ((Item)event.getEntity()).getItemStack().equals(new ItemStack(this.plugin.season.getHeistObjectMaterial(), 1))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event){
        if(event.getEntity().getType().equals(EntityType.DROPPED_ITEM) &&
                event.getEntity().getItemStack().equals(new ItemStack(this.plugin.season.getHeistObjectMaterial(), 1))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if(event.getItem().getItemStack().equals(new ItemStack(this.plugin.season.getHeistObjectMaterial(), 1)) &&
                event.getEntityType() != EntityType.PLAYER) {
            event.setCancelled(true);
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

    private void doDropHeistObject(Player p) {
        PlayerInventory pi = p.getInventory();
        ItemStack is = new ItemStack(this.plugin.season.getHeistObjectMaterial(), 1);

        if(pi.getItemInMainHand() != null && pi.getItemInMainHand().equals(is)) {
            p.dropItem(false);
        }
        if(pi.getItemInOffHand() != null && pi.getItemInOffHand().equals(is)) {
            p.getWorld().dropItem(p.getLocation(), pi.getItemInOffHand());
            pi.setItemInOffHand(null);
        }
        for(int i = 0; i < 36; i++) {
            if(pi.getItem(i) != null && pi.getItem(i).equals(is)) {
                p.getWorld().dropItem(p.getLocation(), pi.getItem(i));
                pi.remove(pi.getItem(i));
            }
        }
    }
}