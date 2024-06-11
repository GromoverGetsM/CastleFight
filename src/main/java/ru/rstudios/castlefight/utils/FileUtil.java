package ru.rstudios.castlefight.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static ru.rstudios.castlefight.CastleFight.*;

public class FileUtil {

    public void createNewFile (String folder, String name) throws IOException {
        File uFile = new File(plugin.getDataFolder(), folder);
        if (!uFile.exists() || uFile.isFile()) {
            if (!uFile.mkdirs()) {
                errorUtil.criterror(null, "Ошибка создания файла: Папка отсутствует и ее создание невозможно.");
            }
        }

        File file = new File(uFile, name);
        if (!file.exists() || !file.isFile()) {
            if (!file.createNewFile()) {
                errorUtil.criterror(null, "Ошибка создания файла: Файл отсутствует и его создание невозможно.");
            } else {
                plugin.getLogger().info("Создан файл " + name);
            }
        }
    }

    public void createStarterFolder (String folderName) {
        File folder = new File(plugin.getDataFolder(), folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
    public FileConfiguration loadFile (String fileName) {
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), fileName));
    }

    public FileConfiguration loadPlayerData (String playerName) {
        return YamlConfiguration.loadConfiguration(new File(new File(plugin.getDataFolder(), "data"), playerName+".yml"));
    }

    public FileConfiguration loadUnusualFolderFile (String fileName, String folder) {
        File uFolder = new File(plugin.getDataFolder(), folder);
        if (uFolder.exists() && !uFolder.isFile()) {
            return YamlConfiguration.loadConfiguration(new File(uFolder, fileName));
        } else {
            errorUtil.criterror(null, "Папка плагина " + uFolder + " не существует!");
            return null;
        }
    }

    public void save (String fileName) throws IOException {
        fileUtil.loadFile(fileName).save(new File(plugin.getDataFolder(), fileName));
    }

    public void saveUnusualConfig (String fileName, boolean needsToReplace) {
        plugin.saveResource(fileName, needsToReplace);
    }
}
