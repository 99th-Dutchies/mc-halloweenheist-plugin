package nl._99th_dutchies.halloween_heist.command;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import nl._99th_dutchies.halloween_heist.util.ItemTrade;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class CommandTrade extends ACommand {
    private static ItemTrade[] config = new ItemTrade[] {
            new ItemTrade("glowstone", Material.REDSTONE, 3, Material.GLOWSTONE_DUST, 1),
            new ItemTrade("netherwart", Material.ROTTEN_FLESH, 5, Material.NETHER_WART, 1),
            new ItemTrade("ghasttear", Material.TROPICAL_FISH, 1, Material.GHAST_TEAR, 1),
            new ItemTrade("dragonbreath", Material.POISONOUS_POTATO, 1, Material.DRAGON_BREATH, 1),
            new ItemTrade("magmacream", Material.TURTLE_EGG, 5, Material.MAGMA_CREAM, 1)
    };

    public CommandTrade(HalloweenHeistPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command");
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Missing arguments for /trade. Usage: /trade [material] [count]");
            return false;
        }
        ItemTrade itemTrade = CommandTrade.getConfig(args[0]);
        if (itemTrade == null) {
            StringBuilder knownMaterials = new StringBuilder();
            for(ItemTrade it : CommandTrade.config) {
                knownMaterials.append(
                        knownMaterials.toString() == "" ? it.getName() : ", " + it.getName()
                );
            }

            sender.sendMessage(ChatColor.RED + "Unknown material for /trade. Known materials: " + knownMaterials);
            return false;
        }
        if (args.length == 2) {
            sender.sendMessage(ChatColor.RED + "Missing arguments for /trade. Usage: /trade [material] [count]");
            sender.sendMessage(ChatColor.YELLOW + "Trade recipe: " +
                    itemTrade.getSourceCount() + " " + itemTrade.getSourceMaterial().name() + " -> " +
                    itemTrade.getTargetCount() + " " + itemTrade.getTargetMaterial().name());
            return false;
        }


        Player p = (Player) sender;

        int available = 0;
        int amount = 1;


        try {
            amount = Integer.parseInt(args[1]);
        } catch(Exception ignored) {}

        for (ItemStack invItemStack : p.getInventory()) {
            if (invItemStack != null && invItemStack.isSimilar(new ItemStack(itemTrade.getSourceMaterial(), 1))) {
                available += invItemStack.getAmount();
            }
        }

        if(available >= amount * itemTrade.getSourceCount()) {
            int traded = 0;

            for(ItemStack invItemStack : p.getInventory()) {
                if(invItemStack != null && invItemStack.isSimilar(new ItemStack(itemTrade.getSourceMaterial()))) {
                    int thisStack = invItemStack.getAmount();
                    int tradeThisStack = Math.min(thisStack, amount * itemTrade.getSourceCount() - traded);

                    invItemStack.setAmount(thisStack - tradeThisStack);
                    traded += tradeThisStack;

                    if (traded >= amount * itemTrade.getSourceCount()) {
                        break;
                    }
                }
            }
            p.getInventory().addItem(new ItemStack(itemTrade.getTargetMaterial(), amount * itemTrade.getTargetCount()));
        } else {
            p.sendMessage(ChatColor.RED + "You need at least " + amount * itemTrade.getSourceCount() + " " + itemTrade.getSourceMaterial().name() + " to trade for " + amount * itemTrade.getTargetCount() + " " + itemTrade.getTargetMaterial().name() + "!");
        }

        return true;
    }

    private static ItemTrade getConfig(String name) {
        for(ItemTrade it : CommandTrade.config) {
            if (it.getName().equals(name)) {
                return it;
            }
        }

        return null;
    }
}
