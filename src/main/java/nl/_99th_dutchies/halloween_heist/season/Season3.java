package nl._99th_dutchies.halloween_heist.season;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.MessageFormat;

public class Season3 extends ASeason {
    public Season3(HalloweenHeistPlugin plugin) {
        super(plugin);
    }

    @Override
    public Material getHeistObjectSpawncontainer() {
        return Material.TRAPPED_CHEST;
    }

    @Override
    public Material getHeistObjectMaterial() {
        return Material.NETHERITE_HELMET;
    }

    @Override
    public String getHeistObjectName() {
        return MessageFormat.format("{0}{1}The Crown{2}", ChatColor.GOLD, ChatColor.BOLD, ChatColor.RESET);
    }

    @Override
    public void spawnHeistObject() {
        Location dropLocation = new Location(this.plugin.mainWorld, -14, 76, 0);

        // Get container
        Block dropBlock = dropLocation.getBlock();
        Chest dropChest = (Chest)dropBlock.getState();
        dropChest.setCustomName("The Briefcase");
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
