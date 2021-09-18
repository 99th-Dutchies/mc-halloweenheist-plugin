package nl._99th_dutchies.halloween_heist;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.MessageFormat;
import java.util.Random;

public class HalloweenHeist extends JavaPlugin implements Listener {
    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        config.options().copyDefaults(true);
        saveConfig();

        Bukkit.getPluginManager().registerEvents(this, this);

        if(!config.getBoolean("itemLoaded")) {
            loadMedal();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.doDropMedal(event.getPlayer());
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
        if(event.getEntity().getType().equals(EntityType.DROPPED_ITEM) &&
                (event.getEntity()).getItemStack().equals(new ItemStack(Material.TOTEM_OF_UNDYING, 1))) {
            event.setCancelled(true);
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

    private void doDropMedal(Player p) {
        PlayerInventory pi = p.getInventory();
        ItemStack is = new ItemStack(Material.TOTEM_OF_UNDYING, 1);

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

    private void loadMedal() {
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
        ItemMeta dropItemMeta = dropItemStack.getItemMeta();
        dropItemMeta.setDisplayName(MessageFormat.format("{0}{1}Medal of Valor", ChatColor.BLUE, ChatColor.BOLD));
        dropItemStack.setItemMeta(dropItemMeta);

        dropChestInventory.setItem(13, dropItemStack);

        config.set("itemLoaded", true);
        saveConfig();

        System.out.println("Dropped item at [" + dropX + "," + dropY + "," + dropZ + "]");
    }
}
