package ru.rstudios.castlefight.modules;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameInfo {
    private final org.bukkit.Location redBase;
    private final org.bukkit.Location blueBase;
    private final int expectedPlayers;
    private final Map<String, String> teamsInfo = new HashMap<>();
    private final Map<String, String> activeRoles = new HashMap<>();
    private final int ID;

    public GameInfo (int id) {
        FileConfiguration gameInfo = YamlConfiguration.loadConfiguration(new File(Bukkit.getWorldContainer() + File.separator + id + File.separator + "mapInfo.yml"));

        if (gameInfo.contains("players")) {
            Set<String> players = gameInfo.getConfigurationSection("players").getKeys(false);
            for (String player : players) {
                String team = gameInfo.getString("players." + player);
                teamsInfo.put(player, team);
            }
        }

        if (gameInfo.contains("activeRoles")) {
            Set<String> players = gameInfo.getConfigurationSection("activeRoles").getKeys(false);
            for (String player : players) {
                String role = gameInfo.getString("activeRoles." + player);
                activeRoles.put(player, role);
            }
        }

        this.ID = id;
        this.expectedPlayers = gameInfo.getInt("expectedPlayers");
        this.blueBase = new org.bukkit.Location(Bukkit.getWorld(String.valueOf(id)), gameInfo.getInt("blueBase.x"), gameInfo.getInt("blueBase.y"), gameInfo.getInt("blueBase.z"));
        this.redBase = new org.bukkit.Location(Bukkit.getWorld(String.valueOf(id)), gameInfo.getInt("redBase.x"), gameInfo.getInt("redBase.y"), gameInfo.getInt("redBase.z"));
    }

    public Location getBlueBase() {
        return blueBase;
    }

    public Location getRedBase() {
        return redBase;
    }

    public int getExpectedPlayers() {
        return expectedPlayers;
    }

    public String getPlayerTeam (String playerName) {
        return teamsInfo.get(playerName);
    }

    public void setPlayerTeam (String playerName, String team) throws IOException {
        FileConfiguration gameInfo = YamlConfiguration.loadConfiguration(new File(Bukkit.getWorldContainer() + File.separator + ID + File.separator + "mapInfo.yml"));
        gameInfo.set("players." + playerName, team);
        gameInfo.save(new File(Bukkit.getWorldContainer() + File.separator + ID + File.separator + "mapInfo.yml"));
    }

    public String getPlayerActiveRole (String playerName) {
        return activeRoles.get(playerName);
    }

    public void setPlayerActiveRole (String playerName, String role) throws IOException {
        FileConfiguration gameInfo = YamlConfiguration.loadConfiguration(new File(Bukkit.getWorldContainer() + File.separator + ID + File.separator + "mapInfo.yml"));
        gameInfo.set("activeRoles." + playerName, role);
        gameInfo.save(new File(Bukkit.getWorldContainer() + File.separator + ID + File.separator + "mapInfo.yml"));
    }

    public Map<String, String> getActiveRoles() {
        return activeRoles;
    }

    public Map<String, String> getTeamsInfo() {
        return teamsInfo;
    }
}
