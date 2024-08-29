package me.tavius.housing.managers;

import me.tavius.housing.Housing;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class LocationManager {

    private final Map<String, Location> spawnLocations = new HashMap<>();
    private final Map<String, Location> bridgeLocations = new HashMap<>();
    private final FileConfiguration config;

    public LocationManager() {
        this.config = Housing.getInstance().getConfig();
        loadLocations();
    }

    public void setSpawnLocation(String name, Location location) {
        spawnLocations.put(name, location);
        saveLocation("spawn.locations." + name, location);
    }

    public void setBridgeLocation(String name, Location location) {
        bridgeLocations.put(name, location);
        saveLocation("bridge.locations." + name, location);
    }

    public Location getSpawnLocation(String name) {
        return spawnLocations.get(name);
    }

    public Location getBridgeLocation(String name) {
        return bridgeLocations.get(name);
    }

    public boolean isInSpawn(Location location) {
        double spawnRadius = config.getDouble("spawn.radius", 7.0);
        for (Location spawnLocation : spawnLocations.values()) {
            if (location.distance(spawnLocation) <= spawnRadius) {
                return true;
            }
        }
        return false;
    }

    public String getBridgeName(Location location) {
        double bridgeRadius = config.getDouble("bridge.radius", 50.0);
        for (Map.Entry<String, Location> entry : bridgeLocations.entrySet()) {
            if (location.distance(entry.getValue()) <= bridgeRadius) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void saveLocation(String path, Location location) {
        config.set(path + ".world", location.getWorld().getName());
        config.set(path + ".x", location.getX());
        config.set(path + ".y", location.getY());
        config.set(path + ".z", location.getZ());
        config.set(path + ".yaw", location.getYaw());
        config.set(path + ".pitch", location.getPitch());
        Housing.getInstance().saveConfig();
    }

    private void loadLocations() {
        if (config.isConfigurationSection("spawn.locations")) {
            for (String name : config.getConfigurationSection("spawn.locations").getKeys(false)) {
                spawnLocations.put(name, loadLocation("spawn.locations." + name));
            }
        }
        if (config.isConfigurationSection("bridge.locations")) {
            for (String name : config.getConfigurationSection("bridge.locations").getKeys(false)) {
                bridgeLocations.put(name, loadLocation("bridge.locations." + name));
            }
        }
    }

    private Location loadLocation(String path) {
        String worldName = config.getString(path + ".world");
        if (worldName == null) return null;
        return new Location(
                Bukkit.getWorld(worldName),
                config.getDouble(path + ".x"),
                config.getDouble(path + ".y"),
                config.getDouble(path + ".z"),
                (float) config.getDouble(path + ".yaw"),
                (float) config.getDouble(path + ".pitch")
        );
    }
}
