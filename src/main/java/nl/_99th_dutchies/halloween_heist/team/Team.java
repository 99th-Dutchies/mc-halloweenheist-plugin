package nl._99th_dutchies.halloween_heist.team;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Team {
    private final String name;
    private final HashMap<Player, Boolean> players = new HashMap<>();

    public Team(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void addPlayer(Player player) {
        this.players.put(player, false);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    public ArrayList<Player> getPlayers() { return new ArrayList<>(this.players.keySet()); }

    public boolean hasPlayer(Player player) {
        return this.players.containsKey(player);
    }

    public void playerSurrendered(Player player) { players.replace(player, Boolean.TRUE); }

    public Boolean allPlayersSurrendered() { return !players.containsValue(false);}
}
