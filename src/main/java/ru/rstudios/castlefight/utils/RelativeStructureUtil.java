package ru.rstudios.castlefight.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.Map;

import static ru.rstudios.castlefight.CastleFight.plugin;

// return roleName+"|"+towerName+"|"+Integer.parseInt(levelFile.getName().substring(0, levelFile.getName().indexOf(".")));
public class RelativeStructureUtil {

    public String findTower(String blockType, Location loc) {
        File mainFolder = new File(plugin.getDataFolder(), "roles");

        for (File roleFolder : mainFolder.listFiles()) {
            if (roleFolder.isDirectory()) {
                String roleName = roleFolder.getName();

                for (File towerFolder : roleFolder.listFiles()) {
                    if (towerFolder.isDirectory()) {
                        String towerName = towerFolder.getName();

                        for (File levelFile : towerFolder.listFiles()) {
                            if (levelFile.isFile() && levelFile.getName().endsWith(".yml")) {
                                YamlConfiguration config = YamlConfiguration.loadConfiguration(levelFile);
                                List<Map<?, ?>> structureConfig = config.getMapList("StructureConfig");

                                for (Map<?, ?> blockData : structureConfig) {
                                    String type = (String) blockData.get("type");
                                    if (type != null && type.equals(blockType)) {
                                        int x = (Integer) blockData.get("x");
                                        int y = (Integer) blockData.get("y");
                                        int z = (Integer) blockData.get("z");

                                        Location pLocation = loc.clone().add(-x, -y, -z);
                                        for (Map<?, ?> blockData2 : structureConfig) {
                                            int dx = (Integer) blockData2.get("x");
                                            int dy = (Integer) blockData2.get("y");
                                            int dz = (Integer) blockData2.get("z");

                                            if (dx == 0 && dy == 0 && dz == 0) {
                                                String dtype = (String) blockData2.get("type");
                                                if (pLocation.getBlock().getType().name().equals(dtype)) {
                                                    Location LBLocation = pLocation;
                                                    if (checkStructure(structureConfig, LBLocation)) {
                                                        return roleName+"|"+towerName+"|"+Integer.parseInt(levelFile.getName().substring(0, levelFile.getName().indexOf(".")))+"|"+LBLocation;
                                                    } else {
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    public boolean checkStructure (List<Map<?, ?>> structureConfig, Location LBLocation) {
        boolean result = false;

        for (Map<?, ?> blockData : structureConfig) {
            int x = (Integer) blockData.get("x");
            int y = (Integer) blockData.get("y");
            int z = (Integer) blockData.get("z");
            String type = (String) blockData.get("type");

            Location pLocation = LBLocation.clone().add(x, y, z);
            Block block = pLocation.getBlock();
            if (block.getType().name().equals(type)) {
                result = true;
            } else {
                result = false;
                break;
            }
        }
        return result;
    }

    public int[] findTowerCoordinates(Location blockLoc, String role, String tower, int level) {
        File mainFolder = new File(plugin.getDataFolder(), "roles");
        File roleFolder = new File(mainFolder, role);
        File towerFolder = new File(roleFolder, tower);
        File levelFile = new File(towerFolder, level + ".yml");

        if (levelFile.exists() && levelFile.isFile()) {
            int minX = blockLoc.getBlockX();
            int minY = blockLoc.getBlockY();
            int minZ = blockLoc.getBlockZ();

            int maxX = minX + 2;
            int maxY = minY + 2;
            int maxZ = minZ + 2;

            return new int[]{minX, minY, minZ, maxX, maxY, maxZ};
        }
        return null;
    }
}
