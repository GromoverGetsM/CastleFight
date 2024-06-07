package ru.rstudios.castlefight.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static ru.rstudios.castlefight.CastleFight.plugin;
import static ru.rstudios.castlefight.CastleFight.fileUtil;
public class FileUtil {
    public FileConfiguration loadFile (String fileName) {
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), fileName));
    }

    public void saveUnusualConfig (String fileName, boolean needsToReplace) {
        plugin.saveResource(fileName, needsToReplace);
    }

    public void save (String fileName) throws IOException {
        if (fileName.equalsIgnoreCase("config.yml")) {
            plugin.saveConfig();
        } else {
            fileUtil.loadFile(fileName).save(new File(plugin.getDataFolder(), fileName));
        }
    }
}
