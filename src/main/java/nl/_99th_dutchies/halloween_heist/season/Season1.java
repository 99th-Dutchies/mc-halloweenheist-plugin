package nl._99th_dutchies.halloween_heist.season;

import nl._99th_dutchies.halloween_heist.HalloweenHeist;
import nl._99th_dutchies.halloween_heist.util.HeistObjectContainer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.MessageFormat;
import java.util.Random;

public class Season1 implements ISeason {
    private HalloweenHeist plugin;

    public Season1(HalloweenHeist plugin) {
        this.plugin = plugin;
    }

    @Override
    public Material getHeistObjectMaterial() {
        return Material.TOTEM_OF_UNDYING;
    }

    @Override
    public String getHeistObjectName() {
        return MessageFormat.format("{0}{1}Medal of Valor", ChatColor.DARK_GREEN, ChatColor.BOLD);
    }

    @Override
    public void sendWinnerMessage(String username) {
        Bukkit.broadcastMessage(MessageFormat.format("{0}{1}ATTENTION, EVERYONE!", ChatColor.BLUE, ChatColor.BOLD));
        Bukkit.broadcastMessage(MessageFormat.format("{0}{1}{2} is an amazing player / genius.", ChatColor.BLUE, ChatColor.BOLD, username));
    }

    @Override
    public void spawnHeistObject() {
        int itemOffset = this.plugin.config.getInt("itemOffset");
        int worldDimensions = this.plugin.config.getInt("worldDimensions");

        World world = this.plugin.mainWorld;
        Random rand = new Random();

        int dropX = (rand.nextInt(worldDimensions - itemOffset) + itemOffset) * (rand.nextBoolean() ? 1 : -1);
        int dropZ = (rand.nextInt(worldDimensions - itemOffset) + itemOffset) * (rand.nextBoolean() ? 1 : -1);
        int dropY = rand.nextInt(world.getHighestBlockYAt(dropX, dropZ) - 4) + 5;
        Location dropLocation = new Location(world, dropX, dropY, dropZ);
        Block dropBlock = dropLocation.getBlock();
        dropBlock.setType(Material.CHEST);
        Chest dropChest = (Chest)dropBlock.getState();
        Inventory dropChestInventory = dropChest.getBlockInventory();

        ItemStack dropItemStack = new ItemStack(this.getHeistObjectMaterial(), 1);
        ItemMeta dropItemMeta = dropItemStack.getItemMeta();
        dropItemMeta.setDisplayName(this.getHeistObjectName());
        dropItemStack.setItemMeta(dropItemMeta);

        dropChestInventory.setItem(13, dropItemStack);
        this.plugin.heistObjectLocation.update(dropLocation, HeistObjectContainer.STORAGE_BLOCK);

        this.plugin.config.set("itemLoaded", true);
        this.plugin.saveConfig();

        System.out.println("Dropped " + this.getHeistObjectName() + " at [" + dropX + "," + dropY + "," + dropZ + "]");
    }
}
