package ru.rstudios.castlefight.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
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

    public void loadElfsRole() {
        Map<String, Object> spiderslairT1 = new HashMap<>();
        Map<String, Object> spiderslairT2 = new HashMap<>();
        Map<String, Object> spiderslairT3 = new HashMap<>();
        Map<String, Object> spiderslairT4 = new HashMap<>();

        spiderslairT1.put("UnitName", "Лесной тарантул");
        spiderslairT1.put("EntityType", "CAVE_SPIDER");
        spiderslairT1.put("Damage", 1);
        spiderslairT1.put("Health", 6);
        spiderslairT1.put("Cooldown", 60);
        spiderslairT1.put("SpawnRate", 500);
        spiderslairT1.put("Owner", "none");
        spiderslairT1.put("AttackType", "Колющий");
        spiderslairT1.put("DefenseType", "Лёгкий");
        spiderslairT1.put("Cost", 0);
        spiderslairT1.put("Income", 5);

        spiderslairT2.put("UnitName", "Тарантул");
        spiderslairT2.put("EntityType", "SPIDER");
        spiderslairT2.put("Damage", 2);
        spiderslairT2.put("Health", 10);
        spiderslairT2.put("Cooldown", 45);
        spiderslairT2.put("SpawnRate", 500);
        spiderslairT2.put("Owner", "none");
        spiderslairT2.put("AttackType", "Колющий");
        spiderslairT2.put("DefenseType", "Лёгкий");
        spiderslairT2.put("Cost", 200);
        spiderslairT2.put("Income", 7);

        spiderslairT3.put("UnitName", "Древний тарантул");
        spiderslairT3.put("EntityType", "SPIDER");
        spiderslairT3.put("Damage", 4);
        spiderslairT3.put("Health", 26);
        spiderslairT3.put("Cooldown", 45);
        spiderslairT3.put("SpawnRate", 400);
        spiderslairT3.put("Owner", "none");
        spiderslairT3.put("AttackType", "Колющий");
        spiderslairT3.put("DefenseType", "Лёгкий");
        spiderslairT3.put("Cost", 260);
        spiderslairT3.put("Income", 10);

        spiderslairT4.put("UnitName", "Призрачный тарантул");
        spiderslairT4.put("EntityType", "SPIDER");
        spiderslairT4.put("Damage", 26);
        spiderslairT4.put("Health", 40);
        spiderslairT4.put("Cooldown", 40);
        spiderslairT4.put("SpawnRate", 1500);
        spiderslairT4.put("Owner", "none");
        spiderslairT4.put("AttackType", "Колющий");
        spiderslairT4.put("DefenseType", "Лёгкий");
        spiderslairT4.put("Cost", 1000);
        spiderslairT4.put("Income", 15);

        Map<String, List<Map<String, Object>>> elfsData = Map.of(
                "spiderslair", List.of(
                    spiderslairT1,
                    spiderslairT2,
                    spiderslairT3,
                    spiderslairT4
                )
        );


        try {
            roleUtil.createRole("elfs", elfsData);
        } catch (IOException e) {
            errorUtil.criterrorfromconfig(null, "castlefight.errors.cannot-create-role");
        }
    }
}
