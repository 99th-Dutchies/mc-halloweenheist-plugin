package nl._99th_dutchies.halloween_heist.command;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import nl._99th_dutchies.halloween_heist.util.InventoryItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class CommandKit implements CommandExecutor {
    private final HalloweenHeistPlugin plugin;

    public CommandKit(HalloweenHeistPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command");
            return false;
        }

        if (p.hasMetadata("nl._99th_dutchies.halloween_heist.hasUsedKit")) {
            sender.sendMessage(ChatColor.RED + "You already obtained your kit");
        } else {
            ArrayList<InventoryItem> items = new ArrayList<>();
            items.add(new InventoryItem(Material.CHAINMAIL_BOOTS, 1, "Tactical gear"));
            items.add(new InventoryItem(Material.CHAINMAIL_CHESTPLATE, 1, "Tactical gear"));
            items.add(new InventoryItem(Material.CHAINMAIL_HELMET, 1, "Tactical gear"));
            items.add(new InventoryItem(Material.CHAINMAIL_LEGGINGS, 1, "Tactical gear"));
            items.add(new InventoryItem(Material.IRON_SWORD, 1, "Zorton's Sword"));
            items.add(new InventoryItem(Material.IRON_PICKAXE, 1));
            items.add(new InventoryItem(Material.IRON_AXE, 1, "Rosa's Battle Axe"));
            items.add(new InventoryItem(Material.IRON_SHOVEL, 1));
            items.add(new InventoryItem(Material.IRON_HOE, 1));
            items.add(new InventoryItem(Material.TORCH, 16));
            items.add(new InventoryItem(Material.GOLDEN_APPLE, 1, "Breakfast burrito"));
            items.add(new InventoryItem(Material.COOKED_CHICKEN, 8, "Wing Slut Chicken"));
            items.add(new InventoryItem(Material.COOKED_BEEF, 8, "Steak Buds Steak"));
            items.add(new InventoryItem(Material.MUSHROOM_STEW, 8, "Terry's Yoghurt"));
            items.add(new InventoryItem(Material.COOKIE, 8, "Marshed Mallow"));
            items.add(new InventoryItem(Material.BREAD, 8, "Mother Dough Bread"));
            items.add(new InventoryItem(Material.OAK_LOG, 16));

            for(InventoryItem item : items) {
                ItemStack is = new ItemStack(item.material, item.count);

                if(item.name != null) {
                    ItemMeta ism = is.getItemMeta();
                    ism.setDisplayName(item.name);
                    is.setItemMeta(ism);
                }

                p.getInventory().addItem(is);
            }

            p.setMetadata("nl._99th_dutchies.halloween_heist.hasUsedKit", new FixedMetadataValue(plugin, true));
        }

        return true;
    }
}
