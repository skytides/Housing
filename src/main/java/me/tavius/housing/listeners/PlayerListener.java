package me.tavius.housing.listeners;

import me.tavius.housing.Housing;
import me.tavius.housing.managers.LocationManager;
import me.tavius.housing.managers.PlayerManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerListener implements Listener {

    private final LocationManager locationManager = Housing.getInstance().getLocationManager();
    private final PlayerManager playerManager = Housing.getInstance().getPlayerManager();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();

        if (locationManager.isInSpawn(playerLocation)) {
            playerManager.clearInventory(player);
            player.getInventory().setArmorContents(null);
        } else {
            String bridgeName = locationManager.getBridgeName(playerLocation);
            if (bridgeName != null && !playerManager.playersOnBridge.containsKey(player)) {
                playerManager.giveBridgeItems(player, bridgeName);
            }
            if (bridgeName != null && playerLocation.getY() < locationManager.getBridgeLocation(bridgeName).getY() - 3.0) {
                playerManager.handleFall(player);
            } else {
                playerManager.playerFallCount.put(player, 0);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (playerManager.playersOnBridge.containsKey(player)) {
                event.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player damaged = (Player) event.getEntity();

            if (locationManager.isInSpawn(damager.getLocation()) || locationManager.isInSpawn(damaged.getLocation())) {
                event.setCancelled(true);
            } else {
                playerManager.setMetadataForLastDamager(damaged, damager);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        playerManager.handleDeath(player);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        playerManager.clearInventory(player);
        playerManager.handleFall(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerManager.clearInventory(player);
    }
}

