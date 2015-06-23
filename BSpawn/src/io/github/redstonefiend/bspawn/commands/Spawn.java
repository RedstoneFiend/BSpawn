/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.redstonefiend.bspawn.commands;

import io.github.redstonefiend.bspawn.BSpawn;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Chris
 */
public class Spawn implements CommandExecutor {

    private final BSpawn plugin;

    public Spawn(BSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if ((args.length == 1) && (args[0].equalsIgnoreCase("reload")) && (sender.hasPermission("bspawn.reload"))) {
            this.plugin.reloadConfig();
            sender.sendMessage(ChatColor.YELLOW + "BSpawn config reloaded.");
            return true;
        }

        if (this.plugin.getSpawn() == null) {
            sender.sendMessage(ChatColor.RED + "spawn not set.");
        } else {
            if (args.length > 1) {
                sender.sendMessage(ChatColor.RED + "spawn accepts one optional argument.");
                return false;
            } else if (args.length == 1) {
                if (!sender.hasPermission("bspawn.other")) {
                    return false;
                }
                Player player = this.plugin.getServer().getPlayer(args[0]);
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "player not found.");
                } else {
                    player.setFallDistance(0);
                    player.teleport(this.plugin.getSpawn());
                    player.sendMessage(ChatColor.YELLOW + this.plugin.getConfig().getString("teleport_other_message")
                            .replaceAll("@p", player.getName())
                            .replaceAll("@s", sender.getName())
                            .replaceAll("(?i)&([a-f0-9])", "\u00A7$1"));
                    sender.sendMessage(ChatColor.YELLOW + player.getName() + " teleported to spawn.");
                }
            } else {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "setspawn must be used by in-game player.");
                } else {
                    Player player = (Player) sender;
                    player.setFallDistance(0);
                    player.teleport(this.plugin.getSpawn());
                    player.sendMessage(ChatColor.YELLOW + this.plugin.getConfig().getString("teleport_message")
                            .replaceAll("(?i)&([a-f0-9])", "\u00A7$1"));
                }
            }
        }

        return true;
    }
}
