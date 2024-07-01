package ru.rstudios.castlefight;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.rstudios.castlefight.commands.*;
import ru.rstudios.castlefight.listeners.*;
import ru.rstudios.castlefight.tasks.ParticleTask;
import ru.rstudios.castlefight.tasks.TabPlayersShowTask;
import ru.rstudios.castlefight.utils.*;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class CastleFight extends JavaPlugin {

    public static JavaPlugin plugin;


    @Override
    public void onEnable() {

        getLogger().info("CastleFight загружает необходимые утилиты...");
        plugin = this;

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new ParticleTask(), 0L, 5L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new TabPlayersShowTask(), 0L, 20L);
        getLogger().info("Утилиты загружены.");

        getLogger().info("CastleFight загружает необходимые файлы...");
        FileUtil.saveUnusualConfig("messages.yml", FileUtil.loadFile("messages.yml").getString("messages-version") == null || !MessagesUtil.messageString("messages-version").equalsIgnoreCase("1.6"));
        FileUtil.saveUnusualConfig("playersDataTemplate.yml", !new File(plugin.getDataFolder(), "playersDataTemplate.yml").exists());
        FileUtil.createStarterFolder("data");
        FileUtil.createStarterFolder("roles");
        FileUtil.createStarterFolder("maps");
        try {
            FileUtil.createNewFile(plugin.getDataFolder(), "activeGames.yml");
        } catch (IOException e) {
            ErrorUtil.criterror(null, e.getLocalizedMessage());
        }
        getLogger().info("Файлы загружены.");

        getLogger().info("CastleFight загружает необходимые команды...");
        Objects.requireNonNull(getCommand("gamemode")).setExecutor(new gameModeCommand());
        Objects.requireNonNull(getCommand("stats")).setExecutor(new statsCommand());
        Objects.requireNonNull(getCommand("spawn")).setExecutor(new spawnCommand());
        Objects.requireNonNull(getCommand("structure")).setExecutor(new structureCommand());
        Objects.requireNonNull(getCommand("openmenu")).setExecutor(new openMenuCommand());
        Objects.requireNonNull(getCommand("creategame")).setExecutor(new createGameCommand());
        Objects.requireNonNull(getCommand("closegame")).setExecutor(new closeGameCommand());
        Objects.requireNonNull(getCommand("world")).setExecutor(new worldCommand());
        Objects.requireNonNull(getCommand("world")).setTabCompleter(new worldCommand());
        Objects.requireNonNull(getCommand("closegame")).setTabCompleter(new closeGameCommand());
        Objects.requireNonNull(getCommand("creategame")).setTabCompleter(new createGameCommand());
        Objects.requireNonNull(getCommand("openmenu")).setTabCompleter(new openMenuCommand());
        Objects.requireNonNull(getCommand("structure")).setTabCompleter(new structureCommand());
        Objects.requireNonNull(getCommand("spawn")).setTabCompleter(new spawnCommand());
        Objects.requireNonNull(getCommand("stats")).setTabCompleter(new statsCommand());
        getLogger().info("Команды загружены.");

        getLogger().info("CastleFight загружает необходимые слушатели...");
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinedServerListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerRightClickedListener(), this);
        getServer().getPluginManager().registerEvents(new clickInventoryItem(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeft(), this);
        getLogger().info("Слушатели загружены.");

        getLogger().info("CastleFight загружает стартовую расу...");
        RoleUtil.loadElfsRole();
        getLogger().info("Раса загружена.");

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!new File(new File(plugin.getDataFolder(), "data"), player.getName() + ".yml").exists()) {
                try {
                    FileUtil.createNewFile("data", player.getName() + ".yml");
                    DataUtil.savePlayersDataTemplate(player.getName(), "data");
                } catch (IOException e) {
                    ErrorUtil.criterrorfromconfig(player, "castlefight.errors.cannot-create-file");
                }
            }
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }
}
