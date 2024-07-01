package ru.rstudios.castlefight.modules;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GameInfo {
    private org.bukkit.Location redBase;
    private org.bukkit.Location blueBase;
    private Location spawn;
    private int expectedPlayers;
    private int expectedPerTeamTowers;
    private final Map<String, String> teamsInfo = new HashMap<>();
    private final Map<String, String> activeRoles = new HashMap<>();
    private final Map<String, Integer> balances = new HashMap<>();
    private final Map<String, Integer> incomes = new HashMap<>();
    private final Map<String, Integer> towerLimits = new HashMap<>();
    private final Map<String, Integer> activeTowers = new HashMap<>();
    private int ID;
    private int baseHealth;
    private int blueHealth;
    private int redHealth;

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

        if (gameInfo.contains("playerActiveTowers")) {
            Set<String> players = gameInfo.getConfigurationSection("playerActiveTowers").getKeys(false);
            for (String player : players) {
                int limit = gameInfo.getInt("playerActiveTowers." + player);
                activeTowers.put(player, limit);
            }
        }

        this.ID = id;
        this.expectedPlayers = gameInfo.getInt("expectedPlayers");
        this.expectedPerTeamTowers = gameInfo.getInt("expectedPerTeamTowers");
        this.blueBase = new org.bukkit.Location(Bukkit.getWorld(String.valueOf(id)), gameInfo.getInt("blueBase.x"), gameInfo.getInt("blueBase.y"), gameInfo.getInt("blueBase.z"));
        this.redBase = new org.bukkit.Location(Bukkit.getWorld(String.valueOf(id)), gameInfo.getInt("redBase.x"), gameInfo.getInt("redBase.y"), gameInfo.getInt("redBase.z"));
        this.spawn = new Location(Bukkit.getWorld(String.valueOf(id)), gameInfo.getInt("spawn.x"), gameInfo.getInt("spawn.y"), gameInfo.getInt("spawn.z"));
        this.baseHealth = gameInfo.getInt("baseHealth");
        this.blueHealth = gameInfo.getInt("blueBase.nowHealth");
        this.redHealth = gameInfo.getInt("redBase.nowHealth");
    }

    public void updateGameInfo (int id) {
        FileConfiguration gameInfo = YamlConfiguration.loadConfiguration(new File(Bukkit.getWorldContainer() + File.separator + id + File.separator + "mapInfo.yml"));
        teamsInfo.clear();
        activeRoles.clear();
        balances.clear();
        incomes.clear();
        towerLimits.clear();

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

        if (gameInfo.contains("playerActiveTowers")) {
            Set<String> players = gameInfo.getConfigurationSection("playerActiveTowers").getKeys(false);
            for (String player : players) {
                int limit = gameInfo.getInt("playerActiveTowers." + player);
                activeTowers.put(player, limit);
            }
        }

        this.ID = id;
        this.expectedPlayers = gameInfo.getInt("expectedPlayers");
        this.expectedPerTeamTowers = gameInfo.getInt("expectedPerTeamTowers");
        this.blueBase = new org.bukkit.Location(Bukkit.getWorld(String.valueOf(id)), gameInfo.getInt("blueBase.x"), gameInfo.getInt("blueBase.y"), gameInfo.getInt("blueBase.z"));
        this.redBase = new org.bukkit.Location(Bukkit.getWorld(String.valueOf(id)), gameInfo.getInt("redBase.x"), gameInfo.getInt("redBase.y"), gameInfo.getInt("redBase.z"));
        this.spawn = new Location(Bukkit.getWorld(String.valueOf(id)), gameInfo.getInt("spawn.x"), gameInfo.getInt("spawn.y"), gameInfo.getInt("spawn.z"));
        this.baseHealth = gameInfo.getInt("baseHealth");
        this.blueHealth = gameInfo.getInt("blueBase.nowHealth");
        this.redHealth = gameInfo.getInt("redBase.nowHealth");
    }

    public int getExpectedPerTeamTowers() {
        return expectedPerTeamTowers;
    }

    public int getBaseHealth() {
        return this.baseHealth;
    }

    public int getBlueHealth() {
        return this.blueHealth;
    }

    public int getRedHealth() {
        return this.redHealth;
    }

    public Location getWorldSpawn() {
        return spawn;
    }

    public void setBaseHealth(int health) throws IOException {
        FileConfiguration gameInfo = YamlConfiguration.loadConfiguration(new File(Bukkit.getWorldContainer() + File.separator + ID + File.separator + "mapInfo.yml"));
        gameInfo.set("baseHealth", health);
        gameInfo.save(new File(Bukkit.getWorldContainer() + File.separator + ID + File.separator + "mapInfo.yml"));
    }

    public void setNowHealth (String team, int health) throws IOException {
        FileConfiguration gameInfo = YamlConfiguration.loadConfiguration(new File(Bukkit.getWorldContainer() + File.separator + ID + File.separator + "mapInfo.yml"));
        gameInfo.set(team + "Base.nowHealth", health);
        gameInfo.save(new File(Bukkit.getWorldContainer() + File.separator + ID + File.separator + "mapInfo.yml"));
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

    public Map<String, Integer> getActiveTowers() {
        return activeTowers;
    }

    public int getPlayerActiveTowers (String playerName) {
        return activeTowers.get(playerName);
    }

    public void setPlayerActiveTowers (String playerName, int count) throws IOException {
        FileConfiguration gameInfo = YamlConfiguration.loadConfiguration(new File(Bukkit.getWorldContainer() + File.separator + ID + File.separator + "mapInfo.yml"));
        gameInfo.set("playerActiveTowers." + playerName, count);
        gameInfo.save(new File(Bukkit.getWorldContainer() + File.separator + ID + File.separator + "mapInfo.yml"));
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
