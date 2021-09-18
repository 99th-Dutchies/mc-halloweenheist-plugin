package nl._99th_dutchies.halloween_heist;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class HalloweenHeist extends JavaPlugin implements Listener {
    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        config.options().copyDefaults(true);
        saveConfig();

        Bukkit.getPluginManager().registerEvents(this, this);

        if(!config.getBoolean("itemLoaded")) {
            loadItem();
        }
    }

    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity().getType().equals(EntityType.DROPPED_ITEM) &&
                ((Item)event.getEntity()).getItemStack().equals(new ItemStack(Material.TOTEM_OF_UNDYING, 1))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {
        if(event.getEntity().getType().equals(EntityType.DROPPED_ITEM) &&
                ((Item)event.getEntity()).getItemStack().equals(new ItemStack(Material.TOTEM_OF_UNDYING, 1))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event){
        if(event.getEntity().getItemStack().equals(new ItemStack(Material.TOTEM_OF_UNDYING, 1))) {
            event.setCancelled(true);
        }
    }

    private void loadItem() {
        int itemOffset = this.config.getInt("itemOffset");
        int worldDimensions = this.config.getInt("worldDimensions");

        World world = this.getServer().getWorld(this.config.getString("worldName"));
        Random rand = new Random();

        int dropX = (rand.nextInt(worldDimensions - itemOffset) + itemOffset) * (rand.nextBoolean() ? 1 : -1);
        int dropZ = (rand.nextInt(worldDimensions - itemOffset) + itemOffset) * (rand.nextBoolean() ? 1 : -1);
        int dropY = rand.nextInt(world.getHighestBlockYAt(dropX, dropZ) - 4) + 5;
        Location dropLocation = new Location(world, dropX, dropY, dropZ);
        Block dropBlock = dropLocation.getBlock();
        dropBlock.setType(Material.CHEST);
        Chest dropChest = (Chest)dropBlock.getState();
        Inventory dropChestInventory = dropChest.getBlockInventory();

        ItemStack dropItemStack = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
        dropChestInventory.setItem(13, dropItemStack);

        config.set("itemLoaded", true);
        saveConfig();

        System.out.println("Dropped item at [" + dropX + "," + dropY + "," + dropZ + "]");
    }
}
