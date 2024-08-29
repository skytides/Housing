package me.tavius.housing.commands;

import me.tavius.housing.Housing;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;

public class HousingCmds {

    @Command("housing setspawn")
    public void setSpawnLocation(Player sender, @Named("name") String name) {
        Housing.getInstance().getLocationManager().setSpawnLocation(name, sender.getLocation());
        sender.sendMessage(ChatColor.GREEN + "Spawn location '" + name + "' set!");
    }

    @Command("housing setbridge")
    public void setBridgeLocation(Player sender, @Named("name") String name) {
        Housing.getInstance().getLocationManager().setBridgeLocation(name, sender.getLocation());
        sender.sendMessage(ChatColor.GREEN + "Bridge location '" + name + "' set!");
    }
}