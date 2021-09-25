package nl._99th_dutchies.halloween_heist;

import nl._99th_dutchies.halloween_heist.command.CommandKit;
import nl._99th_dutchies.halloween_heist.listener.MedalSavingListener;
import nl._99th_dutchies.halloween_heist.listener.MedalTrackingListener;
import nl._99th_dutchies.halloween_heist.util.MedalContainer;
import nl._99th_dutchies.halloween_heist.util.MedalLocation;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.MessageFormat;
import java.util.Random;

public class HalloweenHeist extends JavaPlugin implements Listener {
    public FileConfiguration config = getConfig();
    public MedalLocation medalLocation = new MedalLocation(this.getServer().getWorld(this.config.getString("worldName")), null,null);

    @Override
    public void onEnable() {
        config.options().copyDefaults(true);
        saveConfig();

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new MedalTrackingListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MedalSavingListener(this), this);

        this.getCommand("kit").setExecutor(new CommandKit());

        if(!config.getBoolean("itemLoaded")) {
            spawnMedal();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getEntity().removeMetadata("nl._99th_dutchies.halloween_heist.hasUsedKit", this);
        this.medalLocation.findMedal();
    }

    private void spawnMedal() {
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
        this.medalLocation.update(dropLocation, MedalContainer.STORAGE_BLOCK);

        config.set("itemLoaded", true);
        saveConfig();

        System.out.println("Dropped item at [" + dropX + "," + dropY + "," + dropZ + "]");
    }
}