package nl._99th_dutchies.halloween_heist.util;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getLogger;

public class HeistState {
    private HalloweenHeistPlugin plugin;
    private File configFile;
    private FileConfiguration config;

    public HeistState(HalloweenHeistPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "heistState.yml");
        this.config = new YamlConfiguration();

        this.load();
    }

    private void load() {
        if(!this.configFile.exists()){
            this.configFile.getParentFile().mkdirs();
            plugin.saveResource("heistState.yml", false);
        }

        try{
            this.config.load(this.configFile);
        }catch (IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }

    public void set(String path, Object value) {
        this.config.set(path, value);
        this.save();
    }

    public void save() {
        if (this.config == null || this.configFile == null) {
            return;
        }

        try {
            this.config.save(this.configFile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save heistState to " + this.configFile, ex);
        }
    }

    public boolean getBoolean(String path) {
        return this.config.getBoolean(path);
    }

    public boolean getBoolean(String path, boolean def) {
        return this.config.getBoolean(path, def);
    }

    public int getInt(String path) {
        return this.config.getInt(path);
    }

    public int getInt(String path, int def) {
        return this.config.getInt(path, def);
    }

    public String getString(String path) {
        return this.config.getString(path);
    }

    public String getString(String path, String def) {
        return this.config.getString(path, def);
    }

    public Location getLocation(String path) {
        return this.config.getLocation(path);
    }

    public Location getLocation(String path, Location def) {
        return this.config.getLocation(path, def);
    }
}
