package nl._99th_dutchies.halloween_heist.command;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class CommandLocation implements CommandExecutor {
    private final HalloweenHeistPlugin plugin;

    public CommandLocation(HalloweenHeistPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command");
            return false;
        }

        Player p = (Player) sender;

        if(p.hasMetadata("nl._99th_dutchies.halloween_heist.location.time") &&
                p.getMetadata("nl._99th_dutchies.halloween_heist.location.time").get(0).asInt() + 60*60 >= (System.currentTimeMillis() / 1000L)) {
            sender.sendMessage(ChatColor.RED + "You have to wait 60 minutes between using /location.");
        } else {
            // Send location
            Location approxLocation = this.plugin.heistObjectLocation.getLocationWithRandomOffset();
            sender.sendMessage("The " + this.plugin.season.getHeistObjectName() + " can be found somewhere around [X:" + ((int) approxLocation.getX()) + ",Y:" + ((int) approxLocation.getY()) + ",Z:" + ((int) approxLocation.getZ()) + "]");

            p.setMetadata("nl._99th_dutchies.halloween_heist.location.time", new FixedMetadataValue(plugin, (System.currentTimeMillis() / 1000L)));
            p.setMetadata("nl._99th_dutchies.halloween_heist.location.x", new FixedMetadataValue(plugin, (int) approxLocation.getX()));
            p.setMetadata("nl._99th_dutchies.halloween_heist.location.y", new FixedMetadataValue(plugin, (int) approxLocation.getY()));
            p.setMetadata("nl._99th_dutchies.halloween_heist.location.z", new FixedMetadataValue(plugin, (int) approxLocation.getZ()));
        }

        return true;
    }
}
