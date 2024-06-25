package ru.rstudios.castlefight.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;

import static ru.rstudios.castlefight.CastleFight.*;

public class DataUtil {

    public String returnData (String playerName, String data) {
        return loadPlayerData(playerName).getString(data, "Не найдено");
    }

    public FileConfiguration loadPlayerData (String playerName) {
        return YamlConfiguration.loadConfiguration(new File(new File(plugin.getDataFolder(), "data"), playerName+".yml"));
    }
    public void savePlayerData (String playerName, String folder) throws IOException {
        File uFile = new File(plugin.getDataFolder(), folder);
        if (uFile.exists() && uFile.isDirectory()) {
            File dFile = new File(uFile, playerName + ".yml");
            if (dFile.exists() && dFile.isFile()) {
                FileConfiguration data = loadPlayerData(playerName);
                data.save(new File(new File(plugin.getDataFolder(), "data"), playerName+".yml"));
            } else {
                errorUtil.errorfromconfig(null, "castlefight.errors.save-folder-not-found");
            }
        } else {
            errorUtil.errorfromconfig(null, "castlefight.errors.save-file-not-found");
        }
    }

    public void savePlayersDataTemplate (String playerName, String folder) throws IOException {
        File uFile = new File(plugin.getDataFolder(), folder);
        if (uFile.exists() && uFile.isDirectory()) {
            FileConfiguration sourceData = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "playersDataTemplate.yml"));
            FileConfiguration destination = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), playerName + ".yml"));

            for (String key : sourceData.getKeys(true)) {
                destination.set(key, sourceData.get(key));
            }

            destination.save(new File(uFile, playerName + ".yml"));
        }
    }
}
