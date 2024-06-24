package ru.rstudios.castlefight.modules;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
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

    public void setMoney (String playerName, int money) throws IOException {
        FileConfiguration playerInfo = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + "data" + File.separator + playerName + ".yml"));
        playerInfo.set("money." + playerName, money);
        playerInfo.save(new File(plugin.getDataFolder() + File.separator + "data" + File.separator + playerName + ".yml"));
    }

    public int getRating() {
        return rating;
    }

    public void setRating (String playerName, int rating) throws IOException {
        FileConfiguration playerInfo = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + "data" + File.separator + playerName + ".yml"));
        playerInfo.set("rating." + playerName, rating);
        playerInfo.save(new File(plugin.getDataFolder() + File.separator + "data" + File.separator + playerName + ".yml"));
    }

    public List<String> getRoles() {
        return roles;
    }

    public void addRole(String playerName, String role) throws IOException {
        FileConfiguration playerInfo = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + "data" + File.separator + playerName + ".yml"));
        List<String> roles = getRoles();
        roles.add(role);
        playerInfo.set("roles", role);
        playerInfo.save(new File(plugin.getDataFolder() + File.separator + "data" + File.separator + playerName + ".yml"));
    }

    public void removeRole (String playerName, String role) throws IOException {
        FileConfiguration playerInfo = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + "data" + File.separator + playerName + ".yml"));
        List<String> roles = getRoles();
        roles.remove(role);
        playerInfo.set("roles", role);
        playerInfo.save(new File(plugin.getDataFolder() + File.separator + "data" + File.separator + playerName + ".yml"));
    }

    public long getLastJoinTime() {
        return lastJoinTime;
    }

    public void setLastJoinTime (String playerName, long time) throws IOException {
        FileConfiguration playerInfo = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + "data" + File.separator + playerName + ".yml"));
        playerInfo.set("lastJoinTime." + playerName, time);
        playerInfo.save(new File(plugin.getDataFolder() + File.separator + "data" + File.separator + playerName + ".yml"));
    }

    public long getLastGameTime() {
        return lastGameTime;
    }

    public void setLastGameTime (String playerName, long time) throws IOException {
        FileConfiguration playerInfo = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + "data" + File.separator + playerName + ".yml"));
        playerInfo.set("lastGameTime." + playerName, time);
        playerInfo.save(new File(plugin.getDataFolder() + File.separator + "data" + File.separator + playerName + ".yml"));
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
