package nl._99th_dutchies.halloween_heist.team;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Team {
    private final String name;
    private final ArrayList<Player> players = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public boolean hasPlayer(Player player) {
        return this.players.contains(player);
    }
}
