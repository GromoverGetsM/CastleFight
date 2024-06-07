package ru.rstudios.castlefight.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static ru.rstudios.castlefight.CastleFight.plugin;

public class FileUtil {
    public FileConfiguration loadFile (String fileName) {
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), fileName));
    }

    public void saveUnusualConfig (String fileName, boolean needsToReplace) {
        plugin.saveResource(fileName, needsToReplace);
    }
}
