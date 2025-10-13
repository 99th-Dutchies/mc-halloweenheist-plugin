package nl._99th_dutchies.halloween_heist.team;

import com.sun.istack.internal.Nullable;
import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getLogger;

public class TeamManager {
    private final HalloweenHeistPlugin plugin;
    private final File configFile;
    private FileConfiguration config;
    private ArrayList<Team> teams = new ArrayList<>();

    public TeamManager(HalloweenHeistPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "teams.yml");
        this.config = new YamlConfiguration();
    }

    public void load() {
        if(!this.configFile.exists()){
            this.configFile.getParentFile().mkdirs();
            plugin.saveResource("teams.yml", false);
        }

        try{
            this.config.load(this.configFile);
            this.loadFromConfig();
        }catch (IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }

    private void loadFromConfig() {
        System.out.println("Loading teams from config");

        ConfigurationSection cs = this.config.getConfigurationSection("teams");
        if (cs == null) {
            return;
        }

        this.teams = new ArrayList<>();
        Set<String> teamNames = cs.getKeys(false);
        for (String teamName : teamNames) {
            System.out.println("Loading team '" + teamName + "'");
            Team team = new Team(teamName);

            List<String> playerNames = cs.getStringList(teamName);
            for (String playerUuid : playerNames) {
                Player player = Bukkit.getPlayer(UUID.fromString(playerUuid));
                if (player == null) {
                    System.out.println("Could not find player " + playerUuid + " to add to team " + teamName);
                } else {
                    System.out.println("Adding player " + playerUuid + " (" + player.getDisplayName() + ") to team " + teamName);
                    team.addPlayer(player);
                }
            }

            this.teams.add(team);
        }
    }

    public void save() {
        if (this.configFile == null) {
            return;
        }

        YamlConfiguration config = new YamlConfiguration();
        ConfigurationSection cs = config.createSection("teams");
        for (Team team : this.teams) {
            List<String> playerUuids = new ArrayList<>();
            for (Player player : team.getPlayers()) {
                playerUuids.add(player.getUniqueId().toString());
            }
            cs.set(team.getName(), playerUuids);
        }
        this.config = config;

        try {
            this.config.save(this.configFile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save heistState to " + this.configFile, ex);
        }
    }

    public ArrayList<Team> getTeams() {
        return this.teams;
    }

    public Team getTeamByName(String name) {
        for (Team team : this.teams) {
            if (team.getName().equals(name)) {
                return team;
            }
        }

        return null;
    }

    public void createTeam(String name) {
        this.teams.add(new Team(name));

        this.save();
    }

    public void addPlayerToTeam(Player player, Team team) {
        if (team.hasPlayer(player)) {
            return;
        }

        team.addPlayer(player);

        for (Team otherTeam : this.teams) {
            if (otherTeam != team) {
                this.removePlayerFromTeam(player, otherTeam);
            }
        }

        this.save();
    }

    public void removePlayerFromTeam(Player player, Team team) {
        ArrayList<Player> playersToRemove = new ArrayList<>();

        for (Player teamPlayer : team.getPlayers()) {
            if (teamPlayer.getUniqueId().equals(player.getUniqueId())) {
                playersToRemove.add(teamPlayer);
            }
        }

        for (Player playerToRemove : playersToRemove) {
            team.removePlayer(playerToRemove);
        }

        this.save();
    }

    public void reloadPlayer(Player player) {
        System.out.println("Reloading player from config");

        ConfigurationSection cs = this.config.getConfigurationSection("teams");
        if (cs == null) {
            return;
        }

        Set<String> teamNames = cs.getKeys(false);
        for (String teamName : teamNames) {
            List<String> playerNames = cs.getStringList(teamName);
            Team team = this.getTeamByName(teamName);
            if (team == null) {
                continue;
            }

            for (String playerUuid : playerNames) {
                Player configPlayer = Bukkit.getPlayer(UUID.fromString(playerUuid));
                if (configPlayer != null && configPlayer.getUniqueId().equals(player.getUniqueId())) {
                    System.out.println("Reloading player " + playerUuid + " (" + player.getDisplayName() + ") to team " + teamName);
                    this.removePlayerFromTeam(player, team);
                    this.addPlayerToTeam(player, team);
                }
            }
        }
    }

    public Team getTeamForPlayer(Player player) {
        for (Team team : this.teams) {
            if (team.hasPlayer(player)) {
                return team;
            }
        }

        return null;
    }

    @Nullable
    public Team getLastTeam(){
        for (Team team : this.teams){
            if (!team.allPlayersSurrendered()){
                return team;
            }
        }

        return null;
    }
}
