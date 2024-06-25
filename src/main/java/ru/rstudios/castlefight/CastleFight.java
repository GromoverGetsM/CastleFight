package ru.rstudios.castlefight;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.rstudios.castlefight.commands.*;
import ru.rstudios.castlefight.listeners.EntityDamageListener;
import ru.rstudios.castlefight.listeners.PlayerJoinedServerListener;
import ru.rstudios.castlefight.listeners.PlayerRightClickedListener;
import ru.rstudios.castlefight.listeners.clickInventoryItem;
import ru.rstudios.castlefight.tasks.ParticleTask;
import ru.rstudios.castlefight.tasks.TabPlayersShowTask;
import ru.rstudios.castlefight.utils.*;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class CastleFight extends JavaPlugin {

    public static JavaPlugin plugin;
    public static BossBarUtil bossBarUtil;
    public static CountDamageUtil countDamageUtil;
    public static DataUtil dataUtil;
    public static ErrorUtil errorUtil;
    public static FileUtil fileUtil;
    public static GameModeUtil gameModeUtil;
    public static HoloUtil holoUtil;
    public static InventoryUtil inventoryUtil;
    public static ItemUtil itemUtil;
    public static MessagesUtil messagesUtil;
    public static ParticleUtil particleUtil;
    public static PlaceholderUtil placeholderUtil;
    public static RelativeStructureUtil relativeStructureUtil;
    public static RoleUtil roleUtil;
    public static ScoreBoardUtil scoreBoardUtil;
    public static TowerUtil towerUtil;
    public static UnitCreator unitCreator;
    public static WorldCreator worldCreator;

    public static ParticleTask particleTask;
    public static TabPlayersShowTask tabPlayersShowTask;

    @Override
    public void onEnable() {

        getLogger().info("CastleFight загружает необходимые утилиты...");
        plugin = this;
        bossBarUtil = new BossBarUtil();
        countDamageUtil = new CountDamageUtil();
        dataUtil = new DataUtil();
        errorUtil = new ErrorUtil();
        fileUtil = new FileUtil();
        gameModeUtil = new GameModeUtil();
        holoUtil = new HoloUtil();
        inventoryUtil = new InventoryUtil();
        itemUtil = new ItemUtil();
        messagesUtil = new MessagesUtil();
        particleUtil = new ParticleUtil();
        placeholderUtil = new PlaceholderUtil();
        relativeStructureUtil = new RelativeStructureUtil();
        roleUtil = new RoleUtil();
        scoreBoardUtil = new ScoreBoardUtil();
        towerUtil = new TowerUtil();
        unitCreator = new UnitCreator();
        worldCreator = new WorldCreator();

        particleTask = new ParticleTask();
        tabPlayersShowTask = new TabPlayersShowTask();

        Bukkit.getScheduler().runTaskTimer(this, particleTask, 0L, 5L);
        Bukkit.getScheduler().runTaskTimer(this, tabPlayersShowTask, 0L, 20L);
        getLogger().info("Утилиты загружены.");

        getLogger().info("CastleFight загружает необходимые файлы...");
        fileUtil.saveUnusualConfig("messages.yml", fileUtil.loadFile("messages.yml").getString("messages-version") == null || !messagesUtil.messageString("messages-version").equalsIgnoreCase("1.4"));
        fileUtil.saveUnusualConfig("playersDataTemplate.yml", !new File(plugin.getDataFolder(), "playersDataTemplate.yml").exists());
        fileUtil.createStarterFolder("data");
        fileUtil.createStarterFolder("roles");
        fileUtil.createStarterFolder("maps");
        try {
            fileUtil.createNewFile(plugin.getDataFolder(), "activeGames.yml");
        } catch (IOException e) {
            errorUtil.criterror(null, e.getLocalizedMessage());
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
        getLogger().info("Слушатели загружены.");

        getLogger().info("CastleFight загружает стартовую расу...");
        roleUtil.loadElfsRole();
        getLogger().info("Раса загружена.");

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
        Bukkit.getScheduler().cancelTasks(this);
    }
}
