package ru.rstudios.castlefight.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.File;
import java.util.Map;

import static ru.rstudios.castlefight.CastleFight.*;

public class clickInventoryItem implements Listener {

    @EventHandler
    public void onPlayerClickedInventory (InventoryClickEvent event) {
        FileConfiguration messages = fileUtil.loadFile("messages.yml");
        Map<String, Object> menus = messages.getConfigurationSection("castlefight.menus").getValues(false);
        if (!menus.isEmpty()) {
            for (String key : menus.keySet()) {

                Map<String, Object> items = fileUtil.loadFile("messages.yml").getConfigurationSection("castlefight.menus." + key + ".items").getValues(false);
                if (!items.isEmpty()) {
                    for (String key2 : items.keySet()) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(messagesUtil.messageString("castlefight.menus." + key + ".items." + key2 + "name"))) {
                            if (messagesUtil.messageString("castlefight.menus." + key + ".items." + key2 + "role") != null && messagesUtil.messageString("castlefight.menus." + key + ".items." + key2 + "tower") != null && messagesUtil.messageString("castlefight.menus." + key + ".items." + key2 + "level") != null) {
                                String role = messagesUtil.messageString("castlefight.menus." + key + ".items." + key2 + "role");
                                String tower = messagesUtil.messageString("castlefight.menus." + key + ".items." + key2 + "tower");
                                int level = Integer.parseInt(messagesUtil.messageString("castlefight.menus." + key + ".items." + key2 + "level"));

                                Player player = (Player) event.getWhoClicked();
                                if (player.getTargetBlockExact(5).getType() != Material.AIR) {
                                    Location viewLoc = player.getTargetBlockExact(5).getLocation();
                                    World world = viewLoc.getWorld();
                                    boolean hasObstruction = false;

                                    for (int x = -2; x <= 2; x++) {
                                        for (int y = 1; y <= 4; y++) {
                                            for (int z = -2; z <= 2; z++) {
                                                Location checkLocation = viewLoc.clone().add(x, y, z);
                                                if (world.getBlockAt(checkLocation).getType() != Material.AIR) {
                                                    hasObstruction = true;
                                                }
                                            }
                                            if (hasObstruction) break;
                                        }
                                        if (hasObstruction) break;
                                    }

                                    if (!hasObstruction) {
                                        viewLoc.add(-1, 0, -1);
                                        towerUtil.loadStructure(role, tower, level, viewLoc);
                                    } else {
                                        player.closeInventory();
                                        player.sendMessage(messagesUtil.messageString("castlefight.errors.cannot-place-tower"));
                                    }
                                }
                            }
                        }
                    }
                }

                String inventoryName = messagesUtil.messageString("castlefight.menus." + key + ".title");
                if (event.getView().getTitle().equalsIgnoreCase(inventoryName)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
