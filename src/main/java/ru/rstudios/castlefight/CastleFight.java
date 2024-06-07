package ru.rstudios.castlefight;

import org.bukkit.plugin.java.JavaPlugin;
import ru.rstudios.castlefight.utils.FileUtil;

public final class CastleFight extends JavaPlugin {

    public static JavaPlugin plugin;
    public static FileUtil fileUtil;

    @Override
    public void onEnable() {
        plugin = this;
        fileUtil = new FileUtil();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
