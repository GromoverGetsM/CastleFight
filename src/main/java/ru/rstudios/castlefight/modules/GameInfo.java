package ru.rstudios.castlefight.modules;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GameInfo {
    private final org.bukkit.Location redBase;
    private final org.bukkit.Location blueBase;
    private final int expectedPlayers;
    private final int expectedPerTeamTowers;
    private final Map<String, String> teamsInfo = new HashMap<>();
    private final Map<String, String> activeRoles = new HashMap<>();
    private final Map<String, Integer> balances = new HashMap<>();
    private final Map<String, Integer> incomes = new HashMap<>();
    private final Map<String, Integer> towerLimits = new HashMap<>();
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

        if (gameInfo.contains("playersBalances")) {
            Set<String> players = gameInfo.getConfigurationSection("playersBalances").getKeys(false);
            for (String player : players) {
                int balance = gameInfo.getInt("playersBalances." + player);
                balances.put(player, balance);
            }
        }

        if (gameInfo.contains("playersIncomes")) {
            Set<String> players = gameInfo.getConfigurationSection("playersIncomes").getKeys(false);
            for (String player : players) {
                int income = gameInfo.getInt("playersIncomes." + player);
                incomes.put(player, income);
            }
        }

        if (gameInfo.contains("playersTowerLimits")) {
            Set<String> players = gameInfo.getConfigurationSection("playersTowerLimits").getKeys(false);
            for (String player : players) {
                int limit = gameInfo.getInt("playersTowerLimits." + player);
                towerLimits.put(player, limit);
            }
        }

        this.ID = id;
        this.expectedPlayers = gameInfo.getInt("expectedPlayers");
        this.expectedPerTeamTowers = gameInfo.getInt("expectedPerTeamTowers");
        this.blueBase = new org.bukkit.Location(Bukkit.getWorld(String.valueOf(id)), gameInfo.getInt("blueBase.x"), gameInfo.getInt("blueBase.y"), gameInfo.getInt("blueBase.z"));
        this.redBase = new org.bukkit.Location(Bukkit.getWorld(String.valueOf(id)), gameInfo.getInt("redBase.x"), gameInfo.getInt("redBase.y"), gameInfo.getInt("redBase.z"));
    }

    public int getExpectedPerTeamTowers() {
        return expectedPerTeamTowers;
    }

    public List<String> getTeamList (String team) {
        List<String> teamMembers = new ArrayList<>();
        for (String key : teamsInfo.keySet()) {
            if (teamsInfo.get(key).equals(team)) {
                teamMembers.add(key);
            }
        }

        return teamMembers;
    }

    public void setExpectedPerTeamTowers(int towers) throws IOException {
        FileConfiguration gameInfo = YamlConfiguration.loadConfiguration(new File(Bukkit.getWorldContainer() + File.separator + ID + File.separator + "mapInfo.yml"));
        gameInfo.set("expectedPerTeamTowers", towers);
        gameInfo.save(new File(Bukkit.getWorldContainer() + File.separator + ID + File.separator + "mapInfo.yml"));
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

    public int getPlayerBalance (String playerName) {
        return balances.get(playerName);
    }

    public void setPlayerBalance (String playerName, int balance) throws IOException {
        FileConfiguration gameInfo = YamlConfiguration.loadConfiguration(new File(Bukkit.getWorldContainer() + File.separator + ID + File.separator + "mapInfo.yml"));
        gameInfo.set("playersBalances." + playerName, balance);
        gameInfo.save(new File(Bukkit.getWorldContainer() + File.separator + ID + File.separator + "mapInfo.yml"));
    }

    public int getPlayerIncome (String playerName) {
        return incomes.get(playerName);
    }

    public void setPlayerIncome (String playerName, int income) throws IOException {
        FileConfiguration gameInfo = YamlConfiguration.loadConfiguration(new File(Bukkit.getWorldContainer() + File.separator + ID + File.separator + "mapInfo.yml"));
        gameInfo.set("playersIncomes." + playerName, income);
        gameInfo.save(new File(Bukkit.getWorldContainer() + File.separator + ID + File.separator + "mapInfo.yml"));
    }

    public int getPlayerTowerLimit (String playerName) {
        return towerLimits.get(playerName);
    }

    public void setPlayerTowerLimit (String playerName, int limit) throws IOException {
        FileConfiguration gameInfo = YamlConfiguration.loadConfiguration(new File(Bukkit.getWorldContainer() + File.separator + ID + File.separator + "mapInfo.yml"));
        gameInfo.set("playersTowerLimits." + playerName, limit);
        gameInfo.save(new File(Bukkit.getWorldContainer() + File.separator + ID + File.separator + "mapInfo.yml"));
    }

    public Map<String, String> getActiveRoles() {
        return activeRoles;
    }

    public Map<String, String> getTeamsInfo() {
        return teamsInfo;
    }

    public Map<String, Integer> getBalances() {
        return balances;
    }

    public Map<String, Integer> getIncomes() {
        return incomes;
    }

    public Map<String, Integer> getTowerLimits() {
        return towerLimits;
    }
}
