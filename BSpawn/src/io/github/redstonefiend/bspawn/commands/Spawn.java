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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Chris
 */
public class Spawn implements CommandExecutor {

    private final Main plugin;

    public Spawn(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (this.plugin.spawn == null) {
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
                    player.teleport(this.plugin.spawn);
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
                    player.teleport(this.plugin.spawn);
                    player.sendMessage(ChatColor.YELLOW + this.plugin.getConfig().getString("teleport_message")
                            .replaceAll("(?i)&([a-f0-9])", "\u00A7$1"));
                }
            }
        }

        return true;
    }
}
