package nl._99th_dutchies.halloween_heist.command;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import nl._99th_dutchies.halloween_heist.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTeam extends ACommand {
    public CommandTeam(HalloweenHeistPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!this.plugin.config.getBoolean("teams.enabled")) {
            sender.sendMessage(ChatColor.RED + "Teams are not enabled for your game");
            return true;
        }

        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + "Players cannot use this command");
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Invalid usage for /team. Usage: /team create [team] OR /team add [team] [player]");
            return false;
        }

        switch (args[0]) {
            case "create":
                return this.handleTeamCreate(sender, command, args);
            case "add":
                return this.handleTeamAdd(sender, command, args);
            default:
                sender.sendMessage(ChatColor.RED + "Invalid usage for /team. Usage: /team create [team] OR /team add [team] [player]");
                return false;
        }
    }

    private boolean handleTeamCreate(CommandSender sender, Command command, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Invalid usage for /team create. Usage: /team create [team]");
            return false;
        }

        String teamName = args[1];
        if(this.plugin.teamManager.getTeamByName(teamName) != null) {
            sender.sendMessage(ChatColor.RED + "A team with name '" + teamName + "' already exists");
            return false;
        }

        this.plugin.teamManager.createTeam(teamName);
        sender.sendMessage(ChatColor.GREEN + "Created team " + teamName);
        return true;
    }

    private boolean handleTeamAdd(CommandSender sender, Command command, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Invalid usage for /team add. Usage: /team add [team] [player]");
            return false;
        }

        String teamName = args[1];
        Team team = this.plugin.teamManager.getTeamByName(teamName);
        if(team == null) {
            sender.sendMessage(ChatColor.RED + "Team '" + teamName + "' does not exist");
            return false;
        }

        String playerName = args[2];
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Could not find player " + playerName);
            return false;
        }

        this.plugin.teamManager.addPlayerToTeam(player, team);
        sender.sendMessage(ChatColor.GREEN + "Added player " + playerName + " to " + teamName);
        return true;
    }
}
