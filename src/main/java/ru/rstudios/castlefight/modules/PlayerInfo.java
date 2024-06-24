package ru.rstudios.castlefight.modules;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

import static ru.rstudios.castlefight.CastleFight.plugin;

public class PlayerInfo {
    private final int money;
    private final int rating;
    private final List<String> roles;
    private final long lastJoinTime;
    private final long lastGameTime;
    private String lastWorld;
    private String nowWorld;
    private final int gameID;

    public PlayerInfo (String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        File file = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + playerName + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);

        this.money = data.getInt("money", 0);
        this.rating = data.getInt("rating", 100);
        this.roles = data.getStringList("roles");
        this.gameID = data.getInt("gameID", -1);
        this.lastJoinTime = data.getLong("lastJoinTime", System.currentTimeMillis());
        this.lastGameTime = data.getLong("lastGameTime", 0);
        this.lastWorld = data.getString("lastWorld");

        if (player != null && player.isOnline()) {
            this.lastWorld = player.getWorld().getName();
            this.nowWorld = player.getWorld().getName();
        }
    }

    public PlayerInfo (Player player) {
        String playerName = player.getName();
        File file = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + playerName + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);

        this.money = data.getInt("money", 0);
        this.rating = data.getInt("rating", 100);
        this.roles = data.getStringList("roles");
        this.gameID = data.getInt("gameID", -1);
        this.lastJoinTime = data.getLong("lastJoinTime", System.currentTimeMillis());
        this.lastGameTime = data.getLong("lastGameTime", 0);
        this.lastWorld = data.getString("lastWorld");

        if (player.isOnline()) {
            this.lastWorld = player.getWorld().getName();
            this.nowWorld = player.getWorld().getName();
        }
    }

    public int getMoney() {
        return money;
    }

    public int getRating() {
        return rating;
    }

    public List<String> getRoles() {
        return roles;
    }

    public long getLastJoinTime() {
        return lastJoinTime;
    }

    public long getLastGameTime() {
        return lastGameTime;
    }

    public String getLastWorld() {
        return lastWorld;
    }

    public String getNowWorld() {
        return nowWorld;
    }

    public int getGameID() {
        return gameID;
    }
}
