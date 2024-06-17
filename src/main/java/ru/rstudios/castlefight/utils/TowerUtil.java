package ru.rstudios.castlefight.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.rstudios.castlefight.CastleFight.*;

public class TowerUtil {
    public void saveStructure(String roleName, String towerName, int level, Location start) throws IOException {
        File mainFolder = new File(plugin.getDataFolder(), "roles");
        File roleFolder = new File(mainFolder, roleName);
        File towerFolder = new File(roleFolder, towerName);
        File levelFile = new File(towerFolder, level + ".yml");

        List<Map<String, Object>> structureData = new ArrayList<>();
        World world = start.getWorld();

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    Block block = world.getBlockAt(start.clone().add(x, y, z));
                    Map<String, Object> blockData = new HashMap<>();
                    blockData.put("x", x);
                    blockData.put("y", y);
                    blockData.put("z", z);
                    blockData.put("type", block.getType().name());
                    structureData.add(blockData);
                }
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(levelFile);
        config.set("StructureConfig", structureData);
        config.save(levelFile);
    }

    public void loadStructure(String roleName, String towerName, int level, Location start) {
        File mainFolder = new File(plugin.getDataFolder(), "roles");
        File roleFolder = new File(mainFolder, roleName);
        File towerFolder = new File(roleFolder, towerName);
        File levelFile = new File(towerFolder, level + ".yml");

        FileConfiguration config = YamlConfiguration.loadConfiguration(levelFile);
        List<Map<String, Object>> structureData = (List<Map<String, Object>>) config.getList("StructureConfig");
        World world = start.getWorld();

        if (structureData != null && !structureData.isEmpty()) {
            new BukkitRunnable() {
                int index = 0;

                @Override
                public void run() {
                    if (index >= structureData.size()) {
                        cancel();
                        return;
                    }

                    Map<String, Object> blockData = structureData.get(index);
                    int x = (int) blockData.get("x");
                    int y = (int) blockData.get("y");
                    int z = (int) blockData.get("z");
                    Material material = Material.valueOf((String) blockData.get("type"));
                    Location blockLocation = start.clone().add(x, y, z);
                    world.getBlockAt(blockLocation).setType(material);

                    index++;
                }
            }.runTaskTimer(plugin, 0, 5);
        } else {
            errorUtil.errorfromconfig(null, "castlefight.errors.role-not-found");
        }
    }
}
