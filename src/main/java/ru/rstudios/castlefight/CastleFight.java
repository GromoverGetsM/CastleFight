package ru.rstudios.castlefight;

import org.bukkit.plugin.java.JavaPlugin;
import ru.rstudios.castlefight.commands.gameModeCommand;
import ru.rstudios.castlefight.listeners.ServerJoinListener;
import ru.rstudios.castlefight.utils.*;

import java.util.Objects;

public final class CastleFight extends JavaPlugin {

    public static JavaPlugin plugin;
    public static ErrorUtil errorUtil;
    public static FileUtil fileUtil;
    public static GameModeUtil gameModeUtil;
    public static ItemUtil itemUtil;
    public static MessagesUtil messagesUtil;

    @Override
    public void onEnable() {
        getLogger().info("CastleFight загружает необходимые утилиты...");
        plugin = this;
        errorUtil = new ErrorUtil();
        fileUtil = new FileUtil();
        gameModeUtil = new GameModeUtil();
        itemUtil = new ItemUtil();
        messagesUtil = new MessagesUtil();
        getLogger().info("Утилиты загружены.");

        getLogger().info("CastleFight загружает необходимые файлы...");
        fileUtil.saveUnusualConfig("messages.yml", false);
        fileUtil.createStarterFolder("data");
        getLogger().info("Файлы загружены.");

        getLogger().info("CastleFight загружает необходимые команды...");
        Objects.requireNonNull(getCommand("gamemode")).setExecutor(new gameModeCommand());
        getLogger().info("Команды загружены.");

        getLogger().info("CastleFight загружает необходимые слушатели...");
        getServer().getPluginManager().registerEvents(new ServerJoinListener(), this);
        getLogger().info("Слушатели загружены.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
