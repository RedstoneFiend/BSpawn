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

/**
 *
 * @author Chris
 */
public class UnsetSpawn implements CommandExecutor {

    private final BSpawn plugin;

    public UnsetSpawn(BSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 0) {
            sender.sendMessage(ChatColor.RED + "unsetspawn takes no arguments.");
            return false;
        } else {
            this.plugin.setSpawn(null);
            this.plugin.setForceSpawn(false);
            this.plugin.getConfig().set("spawn", null);
            this.plugin.saveConfig();
            sender.sendMessage(ChatColor.YELLOW + "Spawn unset.");
        }

        return true;
    }
}
