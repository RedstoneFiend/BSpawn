/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.redstonefiend.bspawn.commands;

import io.github.redstonefiend.bspawn.BSpawn;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Chris
 */
public class SetSpawn implements CommandExecutor {

    private final BSpawn plugin;

    public SetSpawn(BSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "setspawn must be used by in-game player.");
        } else if (args.length > 1) {
            sender.sendMessage(ChatColor.RED + "setspawn accepts one optional argument.");
            return false;
        } else {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("force")) {
                    this.plugin.getConfig().set("spawn.force", true);
                } else {
                    sender.sendMessage(ChatColor.RED + "error in argument.");
                    return false;
                }
            } else {
                this.plugin.getConfig().set("spawn.force", false);
            }
            
            Location location = ((Player) sender).getLocation();
            this.plugin.getConfig().set("spawn.world", location.getWorld().getName());
            this.plugin.getConfig().set("spawn.x", location.getBlockX());
            this.plugin.getConfig().set("spawn.y", location.getBlockY());
            this.plugin.getConfig().set("spawn.z", location.getBlockZ());
            this.plugin.getConfig().set("spawn.yaw", (int) Math.floor(location.getYaw()));
            this.plugin.getConfig().set("spawn.pitch", (int) Math.floor(location.getPitch()));
            this.plugin.saveConfig();
            
            this.plugin.loadSpawn();
            
            sender.sendMessage(ChatColor.YELLOW + "Spawn set.");
        }

        return true;
    }
}
