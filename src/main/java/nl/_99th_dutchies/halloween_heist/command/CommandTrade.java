package nl._99th_dutchies.halloween_heist.command;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandTrade implements CommandExecutor {
    private final HalloweenHeistPlugin plugin;

    public CommandTrade(HalloweenHeistPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command");
            return false;
        }

        int available = 0;
        int amount = 1;

        try {
            amount = Integer.parseInt(args[0]);
        } catch(Exception ignored) {}

        for(ItemStack invItemStack : p.getInventory()) {
            if(invItemStack != null && invItemStack.isSimilar(new ItemStack(Material.REDSTONE, 1))) {
                available += invItemStack.getAmount();
            }
        }

        if(available >= amount * 3) {
            int traded = 0;

            for(ItemStack invItemStack : p.getInventory()) {
                if(invItemStack != null && invItemStack.isSimilar(new ItemStack(Material.REDSTONE, 1))) {
                    int thisStack = invItemStack.getAmount();
                    int tradeThisStack = Math.min(thisStack, amount * 3 - traded);

                    invItemStack.setAmount(thisStack - tradeThisStack);
                    traded += tradeThisStack;

                    if(traded >= amount * 3) {
                        break;
                    }
                }
            }
            p.getInventory().addItem(new ItemStack(Material.GLOWSTONE_DUST, amount));
        } else {
            p.sendMessage(ChatColor.RED + "You need at least " + amount * 3 + " redstone to trade for " + amount + " glowstone dust!");
        }

        return true;
    }
}
