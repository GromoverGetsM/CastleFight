package ru.rstudios.castlefight.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static ru.rstudios.castlefight.CastleFight.*;

public class DataUtil {

    public Object returnData (String playerName, String data) {
        return loadPlayerData(playerName).getString(data, "Не найдено");
    }

    public FileConfiguration loadPlayerData (String playerName) {
        return YamlConfiguration.loadConfiguration(new File(new File(plugin.getDataFolder(), "data"), playerName+".yml"));
    }
    public void savePlayerData (String playerName, String folder) throws IOException {
        File uFile = new File(plugin.getDataFolder(), folder);
        if (uFile.exists() && !uFile.isFile()) {
            File dFile = new File(uFile, playerName + ".yml");
            if (dFile.exists() && dFile.isFile()) {
                FileConfiguration data = loadPlayerData(playerName);
                data.save(new File(new File(plugin.getDataFolder(), "data"), playerName+".yml"));
            } else {
                errorUtil.error(null, "Файл сохранения не найден.");
            }
        } else {
            errorUtil.error(null, "Папка сохранения не найдена.");
        }
    }

    public void savePlayersDataTemplate (String playerName, String folder) throws IOException {
        File uFile = new File(plugin.getDataFolder(), folder);
        if (uFile.exists() && !uFile.isFile()) {
            File dFile = new File(uFile, playerName + ".yml");
            if (dFile.exists() && dFile.isFile()) {
                FileConfiguration data = loadPlayerData(playerName);
                data.set("money", 0);
                data.set("rating", 100);
                data.set("roles", "tech");
                data.set("permission_level", 0);
                data.save(new File(new File(plugin.getDataFolder(), "data"), playerName+".yml"));
            } else {
                fileUtil.createNewFile(folder, playerName + ".yml");
            }
        } else {
            fileUtil.createStarterFolder(folder);
        }
    }

}
