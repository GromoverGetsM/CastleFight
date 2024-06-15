package ru.rstudios.castlefight;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.rstudios.castlefight.commands.gameModeCommand;
import ru.rstudios.castlefight.commands.spawnCommand;
import ru.rstudios.castlefight.commands.statsCommand;
import ru.rstudios.castlefight.commands.structureCommand;
import ru.rstudios.castlefight.listeners.EntityDamageListener;
import ru.rstudios.castlefight.listeners.PlayerJoinedServerListener;
import ru.rstudios.castlefight.utils.*;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class CastleFight extends JavaPlugin {

    public static JavaPlugin plugin;

    public static CountDamageUtil countDamageUtil;
    public static DataUtil dataUtil;
    public static ErrorUtil errorUtil;
    public static FileUtil fileUtil;
    public static GameModeUtil gameModeUtil;
    public static ItemUtil itemUtil;
    public static MessagesUtil messagesUtil;
    public static RoleUtil roleUtil;
    public static TowerUtil towerUtil;

    @Override
    public void onEnable() {

        getLogger().info("CastleFight загружает необходимые утилиты...");
        plugin = this;
        countDamageUtil = new CountDamageUtil();
        dataUtil = new DataUtil();
        errorUtil = new ErrorUtil();
        fileUtil = new FileUtil();
        gameModeUtil = new GameModeUtil();
        itemUtil = new ItemUtil();
        messagesUtil = new MessagesUtil();
        roleUtil = new RoleUtil();
        towerUtil = new TowerUtil();
        getLogger().info("Утилиты загружены.");

        getLogger().info("CastleFight загружает необходимые файлы...");
        fileUtil.saveUnusualConfig("messages.yml", false);
        fileUtil.createStarterFolder("data");
        fileUtil.createStarterFolder("roles");
        getLogger().info("Файлы загружены.");

        getLogger().info("CastleFight загружает необходимые команды...");
        Objects.requireNonNull(getCommand("gamemode")).setExecutor(new gameModeCommand());
        Objects.requireNonNull(getCommand("stats")).setExecutor(new statsCommand());
        Objects.requireNonNull(getCommand("spawn")).setExecutor(new spawnCommand());
        Objects.requireNonNull(getCommand("structure")).setExecutor(new structureCommand());
        Objects.requireNonNull(getCommand("structure")).setTabCompleter(new structureCommand());
        Objects.requireNonNull(getCommand("spawn")).setTabCompleter(new spawnCommand());
        Objects.requireNonNull(getCommand("stats")).setTabCompleter(new statsCommand());
        getLogger().info("Команды загружены.");

        getLogger().info("CastleFight загружает необходимые слушатели...");
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinedServerListener(), this);
        getLogger().info("Слушатели загружены.");

        getLogger().info("CastleFight загружает стартовую расу...");
        roleUtil.loadElfsRole();
        getLogger().info("Расы загружены.");

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!new File(new File(plugin.getDataFolder(), "data"), player.getName() + ".yml").exists()) {
                try {
                    fileUtil.createNewFile("data", player.getName() + ".yml");
                    dataUtil.savePlayersDataTemplate(player.getName(), "data");
                } catch (IOException e) {
                    errorUtil.criterrorfromconfig(player, "castlefight.errors.cannot-create-file");
                }
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
