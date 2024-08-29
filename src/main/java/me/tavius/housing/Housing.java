package me.tavius.housing;

import me.tavius.housing.commands.HousingCmds;
import me.tavius.housing.listeners.PlayerListener;
import me.tavius.housing.managers.LocationManager;
import me.tavius.housing.managers.PlayerManager;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

public class Housing extends JavaPlugin {

    private static Housing instance;
    private LocationManager locationManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        this.locationManager = new LocationManager();
        this.playerManager = new PlayerManager();

        BukkitCommandHandler handler = BukkitCommandHandler.create(this);
        handler.register(new HousingCmds());

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public static Housing getInstance() {
        return instance;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}