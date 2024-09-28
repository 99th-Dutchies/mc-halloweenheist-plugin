package nl._99th_dutchies.halloween_heist.team;

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
import java.util.logging.Level;

import static org.bukkit.Bukkit.getLogger;

public class TeamManager {
    private final HalloweenHeistPlugin plugin;
    private final File configFile;
    private FileConfiguration config;
    private final ArrayList<Team> teams = new ArrayList<>();

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
        ConfigurationSection cs = this.config.getConfigurationSection("teams");
        if (cs == null) {
            return;
        }

        Set<String> teamNames = cs.getKeys(false);
        for (String teamName : teamNames) {
            Team team = new Team(teamName);

            List<String> playerNames = cs.getStringList(teamName);
            for (String playerName : playerNames) {
                Player player = Bukkit.getPlayerExact(playerName);
                if (player != null) {
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
            List<String> playerNames = new ArrayList<>();
            for (Player player : team.getPlayers()) {
                playerNames.add(player.getName());
            }
            cs.set(team.getName(), playerNames);
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
        team.addPlayer(player);

        for (Team otherTeam : this.teams) {
            if (otherTeam != team && otherTeam.hasPlayer(player)) {
                otherTeam.removePlayer(player);
            }
        }

        this.save();
    }

    public Team getTeamForPlayer(Player player) {
        for (Team team : this.teams) {
            if (team.hasPlayer(player)) {
                return team;
            }
        }

        return null;
    }
}
