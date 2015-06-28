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
package io.github.redstonefiend.bspawn;

import io.github.redstonefiend.bspawn.commands.BSpawn;
import io.github.redstonefiend.bspawn.commands.SetSpawn;
import io.github.redstonefiend.bspawn.commands.Spawn;
import io.github.redstonefiend.bspawn.commands.UnsetSpawn;
import io.github.redstonefiend.bspawn.listeners.PlayerChat;
import io.github.redstonefiend.bspawn.listeners.PlayerJoin;
import io.github.redstonefiend.bspawn.listeners.PlayerQuit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Chris
 */
public class Main extends JavaPlugin implements Listener {

    public Location spawn = null;
    public boolean forceSpawn = false;

    @Override
    public void onEnable() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        if (this.getConfig().contains("spawn")) {
            loadSpawn();
        }

        this.getCommand("bspawn").setExecutor(new BSpawn(this));
        this.getCommand("spawn").setExecutor(new Spawn(this));
        this.getCommand("setspawn").setExecutor(new SetSpawn(this));
        this.getCommand("unsetspawn").setExecutor(new UnsetSpawn(this));

        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(this), this);

        this.getLogger().log(Level.INFO, "BSpawn version {0} loaded.", this.getDescription().getVersion());
    }

    public void loadSpawn() {
        World world;
        Double x, y, z;
        Float yaw, pitch;

        world = this.getServer().getWorld(this.getConfig().getString("spawn.world"));
        x = (double) this.getConfig().getInt("spawn.x") + 0.5;
        y = (double) this.getConfig().getInt("spawn.y") + 0.5;
        z = (double) this.getConfig().getInt("spawn.z") + 0.5;
        yaw = (float) this.getConfig().getInt("spawn.yaw");
        pitch = (float) this.getConfig().getInt("spawn.pitch");
        this.spawn = new Location(world, x, y, z, yaw, pitch);
        this.forceSpawn = this.getConfig().getBoolean("spawn.force");
    }

    @Override
    public void saveConfig() {
        String header
                = "#################################\n"
                + "# Boomerang Spawn Configuration #\n"
                + "#################################\n\n"
                + "# Message strings should be quoted using single quotes (').\n"
                + "# Message strings can include the following formating codes:\n"
                + "#   &x - color code where x is the color number as defined at\n"
                + "#        http://minecraft.gamepedia.com/Formatting_codes.\n"
                + "#   @p - player's in-game name\n"
                + "#   @s - command sender's in-game name\n"
                + "#   @( - scoreboard team prefix (team color)\n"
                + "#   @) - scoreboard team suffix";

        String str = this.getConfig().saveToString();
        StringBuilder sb = new StringBuilder(str);
        sb.replace(0, sb.indexOf("\n"), header);

        sb.insert(sb.indexOf("\nversion:") + 1,
                "\n# Configuration version used during upgrade. Do not change.\n");

        sb.insert(sb.indexOf("\nteleport_message:") + 1,
                "\n# Message displayed to player when teleported by spawn\n"
                + "# command. Accepts formatting codes &x, @p and @s.\n");

        sb.insert(sb.indexOf("\nteleport_other_message:") + 1,
                "\n# Message displayed to player when teleported by spawn <player>\n"
                + "# command. Accepts formatting codes &x, @p and @s.\n");

        sb.insert(sb.indexOf("\nuse_team_colors:") + 1,
                "\n# When 'true', formats players' in-game names in chat with team\n"
                + "# color. This is the default Minecraft behavior that Bukkit does\n"
                + "# not properly support.\n");

        sb.insert(sb.indexOf("\nnew_join_commands:") + 1,
                "\n# These commands are run when a player joins the game for the\n"
                + "# first time. Accepts formatting codes &x, @p, @s, @( and @).\n");

        sb.insert(sb.indexOf("\njoin_commands:") + 1,
                "\n# These commands are run when a player rejoins the game.\n"
                + "# Accepts formatting codes &x, @p, @s, @( and @).\n");

        sb.insert(sb.indexOf("\nquit_commands:") + 1,
                "\n# These commands are run when a player leaves the game.\n"
                + "# Accepts formatting codes &x, @p, @s, @( and @).\n");

        if (this.getConfig().contains("spawn")) {
            sb.insert(sb.indexOf("spawn:"),
                    "\n# Server spawn is set to this location. It can be reset to\n"
                    + "# another location by typing '/setspawn' while standing where\n"
                    + "# you would like players to spawn.\n");
        } else {
            sb.append(
                    "\n# Server spawn has not been. It can be set to a location by\n"
                    + "# typing '/setspawn' while standing where you would like\n"
                    + "# players to spawn.\n");
        }

        final File cfg_file = new File(this.getDataFolder(), "config.yml");
        final String cfg_str = sb.toString();
        final Logger logger = this.getLogger();

        new BukkitRunnable() {
            @Override
            public void run() {
                cfg_file.delete();
                try (PrintWriter writer = new PrintWriter(cfg_file, "UTF-8")) {
                    writer.write(cfg_str);
                    writer.close();
                } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                    logger.severe("Error saving configuration!");
                }
            }
        }.runTaskLater(this, 1);
    }
}
