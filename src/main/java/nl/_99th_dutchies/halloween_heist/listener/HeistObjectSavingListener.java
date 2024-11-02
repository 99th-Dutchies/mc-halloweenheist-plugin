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
import org.bukkit.event.player.PlayerItemDamageEvent;
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
        System.out.println("Player quit, dropping HeistObject");
    }

    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent event) {
        event.setCancelled(true);
        System.out.println("Prevented EntityResurrect of " + event.getEntityType());
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
                this.plugin.season.isHeistObject(((Item)event.getEntity()).getItemStack())) {
            event.setCancelled(true);
            System.out.println("Prevented EntityDamage to HeistObject");
        }
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {
        if(event.getEntity().getType().equals(EntityType.DROPPED_ITEM) &&
                this.plugin.season.isHeistObject(((Item)event.getEntity()).getItemStack())) {
            event.setCancelled(true);
            System.out.println("Prevented EntityCombust to HeistObject");
        }
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event){
        if(event.getEntity().getType().equals(EntityType.DROPPED_ITEM) &&
                this.plugin.season.isHeistObject(event.getEntity().getItemStack())) {
            event.setCancelled(true);
            System.out.println("Prevented ItemDespawn to HeistObject");
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (this.plugin.season.isHeistObject(event.getItem().getItemStack()) &&
                !event.getEntityType().equals(EntityType.PLAYER)) {
            event.setCancelled(true);
            System.out.println("Prevented EntityPickup to HeistObject by a " + event.getEntityType().name());
        }
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        if (this.plugin.season.isHeistObject(event.getItem())) {
            event.setCancelled(true);
            System.out.println("Prevented PlayerItemDamage to HeistObject by " + event.getPlayer().getDisplayName());
        }
    }

    private void doDropHeistObject(Player p) {
        PlayerInventory pi = p.getInventory();

        if (this.plugin.season.isHeistObject(pi.getItemInMainHand())) {
            p.dropItem(false);
        }
        if (this.plugin.season.isHeistObject(pi.getItemInOffHand())) {
            p.getWorld().dropItem(p.getLocation(), pi.getItemInOffHand());
            pi.setItemInOffHand(null);
        }
        if (pi.getHelmet() != null && this.plugin.season.isHeistObject(pi.getHelmet())) {
            p.getWorld().dropItem(p.getLocation(), pi.getHelmet());
            pi.setHelmet(null);
        }
        if (pi.getChestplate() != null && this.plugin.season.isHeistObject(pi.getChestplate())) {
            p.getWorld().dropItem(p.getLocation(), pi.getChestplate());
            pi.setChestplate(null);
        }
        if (pi.getLeggings() != null && this.plugin.season.isHeistObject(pi.getLeggings())) {
            p.getWorld().dropItem(p.getLocation(), pi.getLeggings());
            pi.setLeggings(null);
        }
        if (pi.getBoots() != null && this.plugin.season.isHeistObject(pi.getBoots())) {
            p.getWorld().dropItem(p.getLocation(), pi.getBoots());
            pi.setBoots(null);
        }
        for(int i = 0; i < 36; i++) {
            ItemStack piItem = pi.getItem(i);
            if(piItem != null && this.plugin.season.isHeistObject(piItem)) {
                p.getWorld().dropItem(p.getLocation(), piItem);
                pi.remove(piItem);
            }
        }
    }
}