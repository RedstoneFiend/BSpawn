/*
 * The MIT License
 *
 * Copyright 2015 Chris Courson.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.redstonefiend.bspawn.commands;

import io.github.redstonefiend.bspawn.Main;
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

    private final Main plugin;

    public SetSpawn(Main plugin) {
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
