package nl._99th_dutchies.halloween_heist.command;

import nl._99th_dutchies.halloween_heist.HalloweenHeist;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class CommandKit implements CommandExecutor {
    HalloweenHeist plugin = HalloweenHeist.getPlugin(HalloweenHeist.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command");
            return false;
        }

        Player p = (Player) sender;

        if (p.hasMetadata("nl._99th_dutchies.halloween_heist.hasUsedKit")) {
            sender.sendMessage(ChatColor.RED + "You already obtained your kit");
        } else {
            p.getInventory().addItem(new ItemStack(Material.COOKIE, 1));

            p.setMetadata("nl._99th_dutchies.halloween_heist.hasUsedKit", new FixedMetadataValue(plugin, true));
        }

        return true;
    }
}
