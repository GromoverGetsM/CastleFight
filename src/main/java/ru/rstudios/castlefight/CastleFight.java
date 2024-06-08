package ru.rstudios.castlefight;

import org.bukkit.plugin.java.JavaPlugin;
import ru.rstudios.castlefight.commands.gameModeCommand;
import ru.rstudios.castlefight.utils.ErrorUtil;
import ru.rstudios.castlefight.utils.FileUtil;
import ru.rstudios.castlefight.utils.GameModeUtil;
import ru.rstudios.castlefight.utils.MessagesUtil;

import java.io.File;
import java.util.Objects;

public final class CastleFight extends JavaPlugin {

    public static JavaPlugin plugin;
    public static ErrorUtil errorUtil;
    public static FileUtil fileUtil;
    public static GameModeUtil gameModeUtil;
    public static MessagesUtil messagesUtil;

    @Override
    public void onEnable() {
        getLogger().info("CastleFight загружает необходимые утилиты...");
        plugin = this;
        errorUtil = new ErrorUtil();
        fileUtil = new FileUtil();
        gameModeUtil = new GameModeUtil();
        messagesUtil = new MessagesUtil();
        getLogger().info("Утилиты загружены.");

        getLogger().info("CastleFight загружает необходимые файлы...");
        fileUtil.saveUnusualConfig("messages.yml", !new File(getDataFolder(), "messages.yml").exists());
        getLogger().info("Команды загружены.");

        getLogger().info("CastleFight загружает необходимые команды...");
        Objects.requireNonNull(getCommand("gamemode")).setExecutor(new gameModeCommand());
        getLogger().info("Команды загружены.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
