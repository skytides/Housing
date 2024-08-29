package me.tavius.housing.managers;

import me.tavius.housing.Housing;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    public final Map<Player, String> playersOnBridge = new HashMap<>();
    public final Map<Player, Integer> playerFallCount = new HashMap<>();
    private final Map<Player, Integer> playerKills = new HashMap<>();

    public void giveBridgeItems(Player player, String bridgeName) {
        PlayerInventory inventory = player.getInventory();
        inventory.setHelmet(new ItemStack(Material.LEATHER_HELMET));
        inventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        inventory.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        inventory.setBoots(new ItemStack(Material.IRON_BOOTS));
        inventory.setItemInHand(new ItemStack(Material.WOOD_SWORD));
        playersOnBridge.put(player, bridgeName);
    }

    public void clearInventory(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        playersOnBridge.remove(player);
        playerFallCount.remove(player);
    }

    public void handleFall(Player player) {
        if (!playerFallCount.containsKey(player) || playerFallCount.get(player) == 0) {
            player.teleport(player.getWorld().getSpawnLocation());
            playerFallCount.put(player, 1);
            if (player.hasMetadata("LastDamager")) {
                player.setMetadata("KnockedOffBridgeBy", new FixedMetadataValue(Housing.getInstance(), player.getMetadata("LastDamager").get(0).asString()));
                String killerName = player.getMetadata("KnockedOffBridgeBy").get(0).asString();
                String deathMessage = ChatColor.RED + player.getName() + ChatColor.GOLD + " got toasted by " + ChatColor.YELLOW + killerName + ChatColor.DARK_GRAY + " R.I.P";
                Housing.getInstance().getServer().broadcastMessage(deathMessage);
            }
        }
    }

    public void handleDeath(Player player) {
        playersOnBridge.remove(player);
        playerFallCount.remove(player);
    }

    public void setMetadataForLastDamager(Player player, Player damager) {
        player.setMetadata("LastDamager", new FixedMetadataValue(Housing.getInstance(), damager.getName()));
    }
}
