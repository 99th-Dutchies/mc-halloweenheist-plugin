package nl._99th_dutchies.halloween_heist.command;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.bukkit.command.CommandExecutor;

public abstract class ACommand implements CommandExecutor {
    protected final HalloweenHeistPlugin plugin;

    public ACommand(HalloweenHeistPlugin plugin) {
        this.plugin = plugin;
    }
}
