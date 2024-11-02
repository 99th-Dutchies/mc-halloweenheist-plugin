package nl._99th_dutchies.halloween_heist.season;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.MessageFormat;

public class Season1 extends ASeason {
    public Season1(HalloweenHeistPlugin plugin) {
        super(plugin);
    }

    @Override
    public Material getHeistObjectSpawncontainer() {
        return Material.TRAPPED_CHEST;
    }

    @Override
    public Material getHeistObjectMaterial() {
        return Material.TOTEM_OF_UNDYING;
    }

    @Override
    public String getHeistObjectName() {
        return MessageFormat.format("{0}{1}Medal of Valor{2}", ChatColor.DARK_GREEN, ChatColor.BOLD, ChatColor.RESET);
    }

    @Override
    public void spawnHeistObject(boolean isRespawn) {
        Location dropLocation = this.generateLocation();

        // Generate container
        Block dropBlock = dropLocation.getBlock();
        dropBlock.setType(this.getHeistObjectSpawncontainer());
        Chest dropChest = (Chest)dropBlock.getState();
        Inventory dropChestInventory = dropChest.getBlockInventory();

        // Generate item stack
        ItemStack dropItemStack = new ItemStack(this.getHeistObjectMaterial(), 1);
        ItemMeta dropItemMeta = dropItemStack.getItemMeta();
        dropItemMeta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "isHeistObject"),
                PersistentDataType.INTEGER,
                1
        );
        dropItemMeta.setDisplayName(this.getHeistObjectName());
        dropItemStack.setItemMeta(dropItemMeta);

        // Place item stack in container
        dropChestInventory.setItem(13, dropItemStack);

        // Update heistState
        this.heistObjectSpawned(dropChest.getBlockInventory().getHolder(), dropLocation);
    }
}
