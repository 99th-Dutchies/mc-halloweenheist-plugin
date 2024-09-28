package nl._99th_dutchies.halloween_heist.command;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import nl._99th_dutchies.halloween_heist.util.TimeHelper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class CommandLocation extends ACommand {
    public CommandLocation(HalloweenHeistPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command");
            return false;
        }

        Player p = (Player) sender;
        int cooldown = this.cooldownToUsage(p);

        if (cooldown > 0) {
            sender.sendMessage(ChatColor.RED + "You have to wait " + TimeHelper.secondsToTime(cooldown) + " before using /location.");
        } else {
            // Send location
            Location approxLocation = this.plugin.heistObjectLocation.getLocationWithRandomOffset();
            if (approxLocation == null) {
                sender.sendMessage(ChatColor.RED + "The location of the " + this.plugin.season.getHeistObjectName() + " could not be determined.");
                return false;
            }

            p.playSound(p.getLocation(), "heist.halloweeeen", 1, 1);

            sender.sendMessage("The " + this.plugin.season.getHeistObjectName() + " can be found somewhere around [X:" + ((int) approxLocation.getX()) + ",Y:" + ((int) approxLocation.getY()) + ",Z:" + ((int) approxLocation.getZ()) + "]");

            p.setMetadata("nl._99th_dutchies.halloween_heist.location.time", new FixedMetadataValue(plugin, (System.currentTimeMillis() / 1000L)));
            p.setMetadata("nl._99th_dutchies.halloween_heist.location.x", new FixedMetadataValue(plugin, (int) approxLocation.getX()));
            p.setMetadata("nl._99th_dutchies.halloween_heist.location.y", new FixedMetadataValue(plugin, (int) approxLocation.getY()));
            p.setMetadata("nl._99th_dutchies.halloween_heist.location.z", new FixedMetadataValue(plugin, (int) approxLocation.getZ()));
        }

        return true;
    }

    private int cooldownToUsage(Player p) {
        if (!p.hasMetadata("nl._99th_dutchies.halloween_heist.location.time")) {
            return 0;
        }

        // Default cooldown time: 60 minutes, down to 30 minutes for last hour of gameplay.
        int cooldownTime = 60 * 60;
        if (this.plugin.getTimeTillEnd() <= 60 * 60) {
            cooldownTime = 30 * 60;
        }

        return (int) (p.getMetadata("nl._99th_dutchies.halloween_heist.location.time").get(0).asInt() + cooldownTime - (System.currentTimeMillis() / 1000L));
    }
}
