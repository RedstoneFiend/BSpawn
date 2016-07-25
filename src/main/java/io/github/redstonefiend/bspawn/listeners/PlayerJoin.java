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
package io.github.redstonefiend.bspawn.listeners;

import io.github.redstonefiend.bspawn.Main;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Team;

/**
 *
 * @author chrisbot
 */
public class PlayerJoin implements Listener {

    private final Main plugin;

    public PlayerJoin(Main plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings({"deprecation", "unchecked"})
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Server server = this.plugin.getServer();

        List<String> commands;
        if ((player.getLastPlayed() == 0) || this.plugin.forceSpawn) {
            if (this.plugin.spawn != null) {
                player.teleport(this.plugin.spawn);
            }
            commands = (List<String>) this.plugin.getConfig().getList("new_join_commands");
        } else {
            commands = (List<String>) this.plugin.getConfig().getList("join_commands");
        }

        if ((commands != null) && (commands.size() > 0)) {
            String prefix = "";
            String suffix = "";
            if (this.plugin.getConfig().getBoolean("use_team_colors")) {
                Team team = this.plugin.getServer().getScoreboardManager().getMainScoreboard().getPlayerTeam(player);
                if (team != null) {
                    prefix = team.getPrefix();
                    suffix = team.getSuffix();
                }
            }

            for (String command : commands) {
                if (command.startsWith("/")) {
                    command = command.substring(1);
                }
                command = command
                        .replaceAll("@p", player.getName())
                        .replaceAll("@\\(", prefix)
                        .replaceAll("@\\)", suffix)
                        .replaceAll("(?i)&([a-f0-9])", "\u00A7$1");
                server.dispatchCommand(server.getConsoleSender(), command);
            }

            e.setJoinMessage(null);
        }
    }
}
