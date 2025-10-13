package nl._99th_dutchies.halloween_heist.command;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import nl._99th_dutchies.halloween_heist.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSurrender extends ACommand {
    public CommandSurrender(HalloweenHeistPlugin plugin) {
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

        team.playerSurrendered(p);
        checkOneTeamLeft();

        return true;
    }

    public void checkOneTeamLeft(){
        int teamsLeft = 0;

        for(Team team : this.plugin.teamManager.getTeams()){
            if(!team.allPlayersSurrendered()){
                teamsLeft++;
            }
        }

        if (teamsLeft == 1){
            this.plugin.heistState.set("forceEnd", true);
        }
    }
}
