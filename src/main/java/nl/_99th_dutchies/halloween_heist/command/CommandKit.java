package nl._99th_dutchies.halloween_heist.command;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import nl._99th_dutchies.halloween_heist.util.InventoryItem;
import nl._99th_dutchies.halloween_heist.util.TimeHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class CommandKit extends ACommand {
    public CommandKit(HalloweenHeistPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!this.plugin.config.getBoolean("kit.enabled")) {
            sender.sendMessage(ChatColor.RED + "This command has not been enabled for your game");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command");
            return false;
        }

        Player p = (Player) sender;

        if (p.hasMetadata("nl._99th_dutchies.halloween_heist.hasUsedKit")) {
            sender.sendMessage(ChatColor.RED + "You already obtained your kit");
        } else if(p.hasMetadata("nl._99th_dutchies.halloween_heist.hasUsedKitTime") &&
                p.getMetadata("nl._99th_dutchies.halloween_heist.hasUsedKitTime").get(0).asInt() + this.plugin.config.getInt("kit.cooldown") >= (System.currentTimeMillis() / 1000L)) {
            int seconds = (int) (p.getMetadata("nl._99th_dutchies.halloween_heist.hasUsedKitTime").get(0).asInt() + this.plugin.config.getInt("kit.cooldown") - (System.currentTimeMillis() / 1000L));
            sender.sendMessage(ChatColor.RED + "You have to wait " + TimeHelper.secondsToTime(seconds) + " before using /kit.");
        } else {
            ArrayList<InventoryItem> items = new ArrayList<>();

            if (plugin.getTimeTillEnd() > 60*60) {
                items.add(new InventoryItem(Material.CHAINMAIL_BOOTS, 1, "Tactical gear"));
                items.add(new InventoryItem(Material.CHAINMAIL_CHESTPLATE, 1, "Tactical gear"));
                items.add(new InventoryItem(Material.CHAINMAIL_HELMET, 1, "Tactical gear"));
                items.add(new InventoryItem(Material.CHAINMAIL_LEGGINGS, 1, "Tactical gear"));
                items.add(new InventoryItem(Material.IRON_SWORD, 1, "Zorton's Sword"));
                items.add(new InventoryItem(Material.IRON_PICKAXE, 1));
                items.add(new InventoryItem(Material.IRON_AXE, 1, "Rosa's Battle Axe"));
                items.add(new InventoryItem(Material.IRON_SHOVEL, 1));
                items.add(new InventoryItem(Material.IRON_HOE, 1));
            } else {
                items.add(new InventoryItem(Material.IRON_BOOTS, 1, "Tactical gear"));
                items.add(new InventoryItem(Material.IRON_CHESTPLATE, 1, "Tactical gear"));
                items.add(new InventoryItem(Material.IRON_HELMET, 1, "Tactical gear"));
                items.add(new InventoryItem(Material.IRON_LEGGINGS, 1, "Tactical gear"));
                items.add(new InventoryItem(Material.DIAMOND_SWORD, 1, "Zorton's Sword"));
                items.add(new InventoryItem(Material.DIAMOND_PICKAXE, 1));
                items.add(new InventoryItem(Material.DIAMOND_AXE, 1, "Rosa's Battle Axe"));
                items.add(new InventoryItem(Material.DIAMOND_SHOVEL, 1));
                items.add(new InventoryItem(Material.DIAMOND_HOE, 1));
            }
            items.add(new InventoryItem(Material.TORCH, 16));
            if (this.plugin.getTimeTillEnd() > 5 * 60 * 60) {
                items.add(new InventoryItem(Material.GOLDEN_APPLE, 1, "Breakfast burrito"));
            }
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
            p.setMetadata("nl._99th_dutchies.halloween_heist.hasUsedKitTime", new FixedMetadataValue(plugin, (System.currentTimeMillis() / 1000L)));
        }

        return true;
    }
}
