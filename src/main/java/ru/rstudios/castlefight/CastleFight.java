package ru.rstudios.castlefight;

import org.bukkit.plugin.java.JavaPlugin;
import ru.rstudios.castlefight.utils.ErrorUtil;
import ru.rstudios.castlefight.utils.FileUtil;
import ru.rstudios.castlefight.utils.GameModeUtil;
import ru.rstudios.castlefight.utils.MessagesUtil;

public final class CastleFight extends JavaPlugin {

    public static JavaPlugin plugin;
    public static ErrorUtil errorUtil;
    public static FileUtil fileUtil;
    public static GameModeUtil gameModeUtil;
    public static MessagesUtil messagesUtil;

    @Override
    public void onEnable() {
        plugin = this;
        errorUtil = new ErrorUtil();
        fileUtil = new FileUtil();
        gameModeUtil = new GameModeUtil();
        messagesUtil = new MessagesUtil();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
