package ru.rstudios.castlefight.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.List;
import java.util.Map;

import static ru.rstudios.castlefight.CastleFight.*;

public class PlayerRightClickedListener implements Listener {

    @EventHandler
    public void onPlayerRightClicked (PlayerInteractEvent event) {
        if (event.getAction().isRightClick()) {
            Player player = event.getPlayer();
            if (player.getInventory().getItemInMainHand().getType().getKey() == Material.GOLDEN_AXE.getKey()) {
                Inventory towerList = inventoryUtil.inventoryFromConfig("towers_" + dataUtil.returnData(player.getName(), "roles"), player);
                player.openInventory(towerList);
            }

            if (event.getClickedBlock() == null) {
                return;
            }

            Block block = event.getClickedBlock();
            if (block.getType() != Material.AIR && player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_AXE) {
                String result = relativeStructureUtil.findTower(block.getType().name(), block.getLocation());

                if (result != null) {
                    String[] parts = result.split("\\|");

                    String role = parts[0];
                    String tower = parts[1];
                    int level = Integer.parseInt(parts[2]);

                    String str = parts[3].substring(9, parts[3].length() - 1);
                    String[] parts2 = str.split(",");

                    double x = Double.parseDouble(parts2[1].substring(parts2[1].indexOf('=') + 1));
                    double y = Double.parseDouble(parts2[2].substring(parts2[2].indexOf('=') + 1));
                    double z = Double.parseDouble(parts2[3].substring(parts2[3].indexOf('=') + 1));
                    float pitch = Float.parseFloat(parts2[4].substring(parts2[4].indexOf('=') + 1));
                    float yaw = Float.parseFloat(parts2[5].substring(parts2[5].indexOf('=') + 1));

                    Location LBLoc = new Location(player.getWorld(), x, y, z, yaw, pitch);

                    if (player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_AXE) {
                        File mainFolder = new File(plugin.getDataFolder(), "roles");
                        File roleFolder = new File(mainFolder, role);
                        File towerFolder = new File(roleFolder, tower);
                        int possibleLevel = level + 1;
                        File levelFile = new File(towerFolder, possibleLevel + ".yml");

                        if (levelFile.exists()) {
                            List<Map<?, ?>> structureConfig = YamlConfiguration.loadConfiguration(levelFile).getMapList("StructureConfig");
                            if (!structureConfig.isEmpty()) {
                                int[] coords = relativeStructureUtil.findTowerCoordinates(LBLoc, role, tower, level);
                                Location leftBottom = new Location(player.getWorld(), coords[0], coords[1], coords[2]);
                                Location rightTop = new Location(player.getWorld(), coords[3], coords[4], coords[5]);

                                for (int sx = leftBottom.getBlockX(); sx <= rightTop.getBlockX(); sx++) {
                                    for (int sy = leftBottom.getBlockY(); sy <= rightTop.getBlockY(); sy++) {
                                        for (int sz = leftBottom.getBlockZ(); sz <= rightTop.getBlockZ(); sz++) {
                                            Block b = new Location(player.getWorld(), sx, sy, sz).getBlock();
                                            b.setType(Material.AIR);
                                        }
                                    }
                                }

                                towerUtil.loadStructure(role, tower, possibleLevel, leftBottom);
                            } else {
                                errorUtil.errorfromconfig(player, "castlefight.errors.tower-not-found");
                            }
                        } else {
                            Inventory upgradeInventory = inventoryUtil.inventoryFromConfig(role + "_" + tower + "_" + level, player);
                            player.openInventory(upgradeInventory);
                        }
                    } else {
                        Inventory upgradeInventory = inventoryUtil.inventoryFromConfig(role + "_" + tower + "_" + level, player);
                        player.openInventory(upgradeInventory);
                    }
                } else {
                    errorUtil.errorfromconfig(player, "castlefight.errors.tower-not-defined");
                }
            }
        }
    }

}
