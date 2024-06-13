package ru.rstudios.castlefight.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.rstudios.castlefight.CastleFight.*;

public class RoleUtil {

    public void createRole(String roleName, Map<String, List<Map<String, Object>>> towersData) throws IOException {
        File folder = new File(new File(plugin.getDataFolder(), "roles"), roleName);

        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                errorUtil.criterrorfromconfig(null, "castlefight.errors.cannot-create-role");
                return;
            }
        }

        for (Map.Entry<String, List<Map<String, Object>>> towerEntry : towersData.entrySet()) {
            String towerName = towerEntry.getKey();
            List<Map<String, Object>> levelsData = towerEntry.getValue();

            File towerFolder = new File(folder, towerName);
            if (!towerFolder.exists()) {
                if (!towerFolder.mkdirs()) {
                    errorUtil.criterrorfromconfig(null, "castlefight.errors.cannot-create-role");
                    return;
                }
            }

            for (int i = 1; i <= levelsData.size(); i++) {
                File file = new File(towerFolder, i + ".yml");
                if (!file.exists()) {
                    if (!file.createNewFile()) {
                        errorUtil.criterrorfromconfig(null, "castlefight.errors.cannot-create-role");
                        return;
                    }
                }

                Map<String, Object> data = levelsData.get(i - 1);
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);

                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    config.set(entry.getKey(), entry.getValue());
                }

                config.save(file);
            }
        }
    }

    public HashMap<String, Object> getRoleUnitData (String roleName, String towerName, int towerLevel) {
        File roleFolder = new File(new File(plugin.getDataFolder(), "roles"), roleName);
        if (roleFolder.exists()) {
            File towerFolder = new File(roleFolder, towerName);
            if (towerFolder.exists()) {
                File unitConfig = new File(towerFolder, towerLevel + ".yml");
                if (unitConfig.exists()) {
                    FileConfiguration dataFile = YamlConfiguration.loadConfiguration(unitConfig);
                    HashMap<String, Object> data = new HashMap<>();

                    data.put("SpawnRate", dataFile.getInt("SpawnRate"));
                    data.put("DefenseType", dataFile.getString("DefenseType"));
                    data.put("EntityType", dataFile.getString("EntityType"));
                    data.put("Owner", dataFile.getString("Owner"));
                    data.put("UnitName", dataFile.getString("UnitName"));
                    data.put("AttackType", dataFile.getString("AttackType"));
                    data.put("Cooldown", dataFile.getInt("Cooldown"));
                    data.put("Damage", dataFile.getInt("Damage"));
                    data.put("Health", dataFile.getInt("Health"));
                    data.put("Cost", dataFile.getInt("Cost"));

                    return data;
                } else {
                    errorUtil.errorfromconfig(null, "role-not-found");
                    return null;
                }
            } else {
                errorUtil.errorfromconfig(null, "role-not-found");
                return null;
            }
        } else {
            errorUtil.errorfromconfig(null, "role-not-found");
            return null;
        }
    }
}
