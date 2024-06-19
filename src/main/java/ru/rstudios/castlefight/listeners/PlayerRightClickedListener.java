package ru.rstudios.castlefight.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.io.File;

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

                    player.sendMessage("§cБашня определена.");
                    player.sendMessage("===Debug Info Mode===");
                    player.sendMessage("= Role found: " + role);
                    player.sendMessage("= Tower found: " + tower);
                    player.sendMessage("= Level found: " + level);

                    if (player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_AXE) {
                        File mainFolder = new File(plugin.getDataFolder(), "roles");
                        File roleFolder = new File(mainFolder, role);
                        File towerFolder = new File(roleFolder, tower);
                        int possibleLevel = level + 1;
                        File levelFile = new File(towerFolder, possibleLevel + ".yml");

                        if (levelFile.exists()) {
                            int[] coords = relativeStructureUtil.findTowerCoordinates(block.getLocation(), block.getType().name(), role, tower, level);
                            Location leftBottom = new Location(player.getWorld(), coords[0], coords[1], coords[2]);
                            Location rightTop = new Location(player.getWorld(), coords[3], coords[4], coords[5]);

                            player.sendMessage("=====================");
                            player.sendMessage("= LBPoint: " + leftBottom);
                            player.sendMessage("= RTPoint: " + rightTop);


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
