/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.redstonefiend.bspawn;

import io.github.redstonefiend.bspawn.commands.SetSpawn;
import io.github.redstonefiend.bspawn.commands.Spawn;
import io.github.redstonefiend.bspawn.commands.UnsetSpawn;
import io.github.redstonefiend.bspawn.listeners.PlayerChat;
import io.github.redstonefiend.bspawn.listeners.PlayerJoin;
import io.github.redstonefiend.bspawn.listeners.PlayerQuit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Chris
 */
public class BSpawn extends JavaPlugin implements Listener {

    private Location spawn = null;
    private boolean forceSpawn = false;

    @Override
    public void onEnable() {
        // this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        this.saveResource("config.txt", false);

        if (this.getConfig().contains("spawn")) {
            loadSpawn();
        }

        this.getCommand("spawn").setExecutor(new Spawn(this));
        this.getCommand("setspawn").setExecutor(new SetSpawn(this));
        this.getCommand("unsetspawn").setExecutor(new UnsetSpawn(this));

        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(this), this);

        this.getLogger().info("BSpawn loaded.");
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public boolean isForceSpawn() {
        return forceSpawn;
    }

    public void setForceSpawn(boolean forceSpawn) {
        this.forceSpawn = forceSpawn;
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
}
