package ru.rstudios.castlefight.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.castlefight.modules.GameInfo;
import ru.rstudios.castlefight.modules.PlayerInfo;
import ru.rstudios.castlefight.tasks.UnitSpawner;
import ru.rstudios.castlefight.utils.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static ru.rstudios.castlefight.CastleFight.*;

public class PlayerRightClickedListener implements Listener {

    @EventHandler
    public void onPlayerRightClicked (PlayerInteractEvent event) {
        if (event.getAction().isRightClick()) {
            Player player = event.getPlayer();
            if (player.getInventory().getItemInMainHand().getType().getKey() == Material.GOLDEN_AXE.getKey()) {
                FileConfiguration data = DataUtil.loadPlayerData(player.getName());
                int gameID = data.getInt("gameID");
                if (gameID != -1) {
                    GameInfo gameInfo = new GameInfo(gameID);

                    Inventory towerList = InventoryUtil.inventoryFromConfig("towers_" + gameInfo.getPlayerActiveRole(player.getName()), player);
                    player.openInventory(towerList);
                }
            }

            if (event.getClickedBlock() == null) {
                return;
            }

            Block block = event.getClickedBlock();
            if (block.getType() != Material.AIR && player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_AXE) {
                String result = RelativeStructureUtil.findTower(block.getType().name(), block.getLocation());

                if (result != null) {
                    String[] parts = result.split("\\|");

                    String role = parts[0];
                    String tower = parts[1];
                    int level = Integer.parseInt(parts[2]);

                    Location LBLoc = getLocation(parts, player);

                    if (player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_AXE) {
                        File mainFolder = new File(plugin.getDataFolder(), "roles");
                        File roleFolder = new File(mainFolder, role);
                        File towerFolder = new File(roleFolder, tower);
                        int possibleLevel = level + 1;
                        File levelFile = new File(towerFolder, possibleLevel + ".yml");

                        if (levelFile.exists()) {
                            List<Map<?, ?>> structureConfig = YamlConfiguration.loadConfiguration(levelFile).getMapList("StructureConfig");
                            if (!structureConfig.isEmpty()) {
                                int[] coords = RelativeStructureUtil.findTowerCoordinates(LBLoc, role, tower, level);
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

                                List<MetadataValue> holoName = leftBottom.getBlock().getMetadata("holoName");
                                List<MetadataValue> taskIDlist = leftBottom.getBlock().getMetadata("taskID");
                                if (!holoName.isEmpty() && !taskIDlist.isEmpty()) {
                                    HoloUtil.deleteHolo(player.getWorld(), holoName.get(0).asString());

                                    Bukkit.getScheduler().cancelTask(taskIDlist.get(0).asInt());

                                    TowerUtil.loadStructure(role, tower, possibleLevel, leftBottom).thenAccept(successfulLoad -> {
                                        if (successfulLoad) {
                                            Random random = new Random();
                                            int id = random.nextInt(1, 1000000);
                                            leftBottom.getBlock().setMetadata("owner", new FixedMetadataValue(plugin, player.getName()));
                                            leftBottom.getBlock().setMetadata("holoName", new FixedMetadataValue(plugin, player.getName()+"_"+tower+"_"+possibleLevel+"_"+id));

                                            HoloUtil.createHologram(leftBottom.clone().add(1.5, 2, 1.5), player.getName()+"_"+tower+"_"+possibleLevel+"_"+id, YamlConfiguration.loadConfiguration(levelFile).getString("UnitName"));
                                            HoloUtil.addHoloLine(leftBottom.getWorld(), player.getName()+"_"+tower+"_"+possibleLevel+"_"+id, "§b██████████", 2);

                                            HashMap<String, Object> unitData = RoleUtil.getRoleUnitData(role, tower, level);
                                            int taskID = Bukkit.getScheduler().runTaskTimer(plugin, new UnitSpawner(Integer.parseInt(leftBottom.getWorld().getName()), player.getName(), role, tower, level, Integer.parseInt(unitData.get("SpawnRate").toString()), leftBottom), 0, 1).getTaskId();
                                            PlayerInfo playerInfo = new PlayerInfo(player.getName());
                                            try {
                                                playerInfo.addTaskId(player.getName(), taskID);
                                                leftBottom.getBlock().setMetadata("taskID", new FixedMetadataValue(plugin, taskID));
                                            } catch (IOException e) {
                                                ErrorUtil.error(null, e.getLocalizedMessage());
                                            }
                                        }
                                    });
                                }
                            } else {
                                ErrorUtil.errorfromconfig(player, "castlefight.errors.tower-not-found");
                            }
                        } else {
                            Inventory upgradeInventory = InventoryUtil.inventoryFromConfig(role + "_" + tower + "_" + level, player);
                            player.openInventory(upgradeInventory);
                        }
                    } else {
                        Inventory upgradeInventory = InventoryUtil.inventoryFromConfig(role + "_" + tower + "_" + level, player);
                        player.openInventory(upgradeInventory);
                    }
                } else {
                    ErrorUtil.errorfromconfig(player, "castlefight.errors.tower-not-defined");
                }
            }
        }
    }

    @NotNull
    private static Location getLocation(String[] parts, Player player) {
        String str = parts[3].substring(9, parts[3].length() - 1);
        String[] parts2 = str.split(",");

        double x = Double.parseDouble(parts2[1].substring(parts2[1].indexOf('=') + 1));
        double y = Double.parseDouble(parts2[2].substring(parts2[2].indexOf('=') + 1));
        double z = Double.parseDouble(parts2[3].substring(parts2[3].indexOf('=') + 1));
        float pitch = Float.parseFloat(parts2[4].substring(parts2[4].indexOf('=') + 1));
        float yaw = Float.parseFloat(parts2[5].substring(parts2[5].indexOf('=') + 1));

        return new Location(player.getWorld(), x, y, z, yaw, pitch);
    }

}
