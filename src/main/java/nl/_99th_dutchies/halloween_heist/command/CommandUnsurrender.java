package nl._99th_dutchies.halloween_heist.command;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import nl._99th_dutchies.halloween_heist.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUnsurrender extends ACommand {
    public CommandUnsurrender(HalloweenHeistPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!this.plugin.config.getBoolean("teams.enabled")) {
            sender.sendMessage(ChatColor.RED + "Teams are not enabled for your game");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command");
            return false;
        }

        Player p = (Player) sender;
        Team team = this.plugin.teamManager.getTeamForPlayer(p);

        if(team == null){
            sender.sendMessage("You are not in a team");
            return false;
        }

        if(!team.playerHasSurrendered(p)){
            sender.sendMessage("You are not surrendered");
            return true;
        }

        team.playerSurrendered(p, false);

        int playersSurrendered = 0;
        for(Player player : team.getPlayers()){
            if(team.playerHasSurrendered(player)){
                playersSurrendered++;
            }
            player.sendMessage("Your team-member " + p.getName() + " has regained faith");
        }
        sender.sendMessage(playersSurrendered + "/" + team.getPlayers().size() + " team-members have surrendered");

        return true;
    }
}
