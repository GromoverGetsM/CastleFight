package ru.rstudios.castlefight.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static ru.rstudios.castlefight.CastleFight.*;

public class clickInventoryItem implements Listener {

    @EventHandler
    public void onPlayerClickedInventory (InventoryClickEvent event) {
        FileConfiguration messages = fileUtil.loadFile("messages.yml");
        Map<String, Object> menus = messages.getConfigurationSection("castlefight.menus").getValues(false);
        if (!menus.isEmpty()) {
            for (String key : menus.keySet()) {
                String inventoryName = messagesUtil.messageString("castlefight.menus." + key + ".title");
                if (event.getView().getTitle().equalsIgnoreCase(inventoryName)) {
                    event.setCancelled(true);
                }

                Map<String, Object> items = fileUtil.loadFile("messages.yml").getConfigurationSection("castlefight.menus." + key + ".items").getValues(false);
                if (!items.isEmpty()) {
                    for (String key2 : items.keySet()) {
                        if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null && event.getCurrentItem().getItemMeta().getDisplayName().equals(messagesUtil.messageString("castlefight.menus." + key + ".items." + key2 + ".name"))) {
                            if (messages.getString("castlefight.menus." + key + ".items." + key2 + ".role") != null && messages.getString("castlefight.menus." + key + ".items." + key2 + ".tower") != null && messages.getString("castlefight.menus." + key + ".items." + key2 + ".level") != null) {
                                if (!event.getWhoClicked().getWorld().getName().equals("world")) {
                                    String role = messagesUtil.messageString("castlefight.menus." + key + ".items." + key2 + ".role");
                                    String tower = messagesUtil.messageString("castlefight.menus." + key + ".items." + key2 + ".tower");
                                    int level = Integer.parseInt(messagesUtil.messageString("castlefight.menus." + key + ".items." + key2 + ".level"));

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
                                            event.setCancelled(true);
                                            viewLoc.add(-1, 1, -1);
                                            player.closeInventory();
                                            towerUtil.loadStructure(role, tower, level, viewLoc).thenAccept(successfulLoad -> {
                                                if (successfulLoad) {
                                                    File mainFolder = new File(plugin.getDataFolder(), "roles");
                                                    File roleFolder = new File(mainFolder, role);
                                                    File towerFolder = new File(roleFolder, tower);
                                                    File levelFile = new File(towerFolder, level + ".yml");

                                                    Random random = new Random();
                                                    int id = random.nextInt(1, 1000000);
                                                    viewLoc.getBlock().setMetadata("owner", new FixedMetadataValue(plugin, player.getName()));
                                                    viewLoc.getBlock().setMetadata("id", new FixedMetadataValue(plugin, id));

                                                    holoUtil.createHologram(viewLoc.add(1.5, 2, 1.5), player.getName()+"_"+tower+"_"+level+"_"+id, YamlConfiguration.loadConfiguration(levelFile).getString("UnitName"));
                                                }
                                            });
                                        } else {
                                            event.setCancelled(true);
                                            player.closeInventory();
                                            player.sendMessage(messagesUtil.messageString("castlefight.errors.cannot-place-tower"));
                                        }
                                    }
                                } else {
                                    event.getWhoClicked().closeInventory();
                                    event.getWhoClicked().sendMessage(messagesUtil.messageString("castlefight.errors.cannot-place-tower"));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
