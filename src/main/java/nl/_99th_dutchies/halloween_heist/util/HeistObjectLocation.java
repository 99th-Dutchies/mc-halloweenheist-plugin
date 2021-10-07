package nl._99th_dutchies.halloween_heist.util;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

public class HeistObjectLocation {
    private final HalloweenHeistPlugin plugin;
    public HeistObjectContainer container;
    public Location location;
    public Player lastPlayer = null;
    public Entity storingEntity = null;

    public HeistObjectLocation(HalloweenHeistPlugin plugin) {
        this.plugin = plugin;
        this.load();
    }

    private void load() {
        this.location = this.plugin.heistState.getLocation("heistObject.location");

        switch(this.plugin.heistState.getString("heistObject.container")) {
            case "PLAYER":
                this.container = HeistObjectContainer.PLAYER;
                break;
            case "STORAGE_BLOCK":
                this.container = HeistObjectContainer.STORAGE_BLOCK;
                break;
            default:
            case "DROPPED":
                this.container = HeistObjectContainer.DROPPED;
                break;
        }

        String lp = this.plugin.heistState.getString("heistObject.lastPlayer");
        if(StringUtils.isEmpty(lp)) {
            this.lastPlayer = null;
        } else {
            this.lastPlayer = Bukkit.getPlayer(UUID.fromString(lp));
        }

        String se = this.plugin.heistState.getString("heistObject.storingEntity");
        if(StringUtils.isEmpty(se)) {
            this.storingEntity = null;
        } else {
            this.storingEntity = Bukkit.getEntity(UUID.fromString(se));
        }
    }

    public void resetPlayer() {
        this.lastPlayer = null;

        this.plugin.heistState.set("heistObject.lastPlayer", null);
    }

    public void updateDropped(Location l) {
        this.container = HeistObjectContainer.DROPPED;
        this.location = l;

        this.plugin.heistState.set("heistObject.container", HeistObjectContainer.DROPPED.name());
        this.plugin.heistState.set("heistObject.location", l);
    }

    public void updatePlayer(Location l, Player p) {
        this.container = HeistObjectContainer.PLAYER;
        this.location = l;
        this.lastPlayer = p;

        this.plugin.heistState.set("heistObject.container", HeistObjectContainer.PLAYER.name());
        this.plugin.heistState.set("heistObject.location", l);
        this.plugin.heistState.set("heistObject.lastPlayer", p.getUniqueId().toString());
    }

    public void updateInventoryHolder(InventoryHolder i) {
        if(i instanceof Entity) {
            this.updateStorageEntity((Entity) i);
        } else if (i instanceof BlockInventoryHolder) {
            this.updateStorageBlock((BlockInventoryHolder) i);
        }
    }

    public void updateStorageBlock(BlockInventoryHolder b) {
        this.container = HeistObjectContainer.STORAGE_BLOCK;
        this.location = b.getBlock().getLocation();

        this.plugin.heistState.set("heistObject.container", HeistObjectContainer.STORAGE_BLOCK.name());
        this.plugin.heistState.set("heistObject.location", b.getBlock().getLocation());
    }

    public void updateStorageEntity(Entity e) {
        this.container = HeistObjectContainer.STORAGE_ENTITY;
        this.location = e.getLocation();
        this.storingEntity = e;

        this.plugin.heistState.set("heistObject.container", HeistObjectContainer.STORAGE_ENTITY.name());
        this.plugin.heistState.set("heistObject.location", e.getLocation());
        this.plugin.heistState.set("heistObject.storingEntity", e.getUniqueId().toString());
    }

    public Location getLocation() {
        switch (this.container) {
            case PLAYER:
                return Bukkit.getPlayer(lastPlayer.getUniqueId()).getLocation();
            case STORAGE_BLOCK:
            case DROPPED:
                return this.location;
            case STORAGE_ENTITY:
                return Bukkit.getEntity(storingEntity.getUniqueId()).getLocation();
            default:
                return null;
        }
    }
}
