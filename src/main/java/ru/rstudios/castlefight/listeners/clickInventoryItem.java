package ru.rstudios.castlefight.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.metadata.FixedMetadataValue;
import ru.rstudios.castlefight.modules.ClickActions;
import ru.rstudios.castlefight.modules.PlayerInfo;
import ru.rstudios.castlefight.tasks.ClickActionsHandlerTask;
import ru.rstudios.castlefight.tasks.UnitSpawner;
import ru.rstudios.castlefight.utils.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static ru.rstudios.castlefight.CastleFight.*;

public class clickInventoryItem implements Listener {

    @EventHandler
    public void onPlayerClickedInventory (InventoryClickEvent event) {
        FileConfiguration messages = FileUtil.loadFile("messages.yml");
        Map<String, Object> menus = messages.getConfigurationSection("castlefight.menus").getValues(false);
        if (!menus.isEmpty()) {
            for (String key : menus.keySet()) {
                String inventoryName = MessagesUtil.messageString("castlefight.menus." + key + ".title");
                if (event.getView().getTitle().equalsIgnoreCase(inventoryName)) {
                    event.setCancelled(true);
                }

                Map<String, Object> items = FileUtil.loadFile("messages.yml").getConfigurationSection("castlefight.menus." + key + ".items").getValues(false);
                if (!items.isEmpty()) {
                    for (String key2 : items.keySet()) {
                        if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null && event.getCurrentItem().getItemMeta().getDisplayName().equals(MessagesUtil.messageString("castlefight.menus." + key + ".items." + key2 + ".name"))) {
                            List<String> clickActions = messages.getStringList("castlefight.menus." + key + ".items." + ".onClick");
                            if (!clickActions.isEmpty()) {
                                for (String actions : clickActions) {
                                    String[] parts = actions.split(" ");
                                    ClickActions clickActions1 = ClickActions.getById(parts[0]);
                                    String executablePart = clickActions1.getExecutableParts(actions.substring(parts[0].length()).trim());
                                    Bukkit.getScheduler().runTask(plugin, new ClickActionsHandlerTask(event.getWhoClicked().getName(), clickActions1, executablePart));
                                }
                            }

                            if (messages.getString("castlefight.menus." + key + ".items." + key2 + ".role") != null && messages.getString("castlefight.menus." + key + ".items." + key2 + ".tower") != null && messages.getString("castlefight.menus." + key + ".items." + key2 + ".level") != null) {
                                if (!event.getWhoClicked().getWorld().getName().equals("world")) {
                                    String role = MessagesUtil.messageString("castlefight.menus." + key + ".items." + key2 + ".role");
                                    String tower = MessagesUtil.messageString("castlefight.menus." + key + ".items." + key2 + ".tower");
                                    int level = Integer.parseInt(MessagesUtil.messageString("castlefight.menus." + key + ".items." + key2 + ".level"));

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

                                        event.setCancelled(true);
                                        if (!hasObstruction) {
                                            viewLoc.add(-1, 1, -1);
                                            player.closeInventory();
                                            TowerUtil.loadStructure(role, tower, level, viewLoc).thenAccept(successfulLoad -> {
                                                if (successfulLoad) {
                                                    File mainFolder = new File(plugin.getDataFolder(), "roles");
                                                    File roleFolder = new File(mainFolder, role);
                                                    File towerFolder = new File(roleFolder, tower);
                                                    File levelFile = new File(towerFolder, level + ".yml");

                                                    Random random = new Random();
                                                    int id = random.nextInt(1, 1000000);
                                                    viewLoc.getBlock().setMetadata("owner", new FixedMetadataValue(plugin, player.getName()));
                                                    viewLoc.getBlock().setMetadata("id", new FixedMetadataValue(plugin, id));
                                                    viewLoc.getBlock().setMetadata("holoName", new FixedMetadataValue(plugin, player.getName()+"_"+tower+"_"+level+"_"+id));

                                                    HoloUtil.createHologram(viewLoc.clone().add(1.5, 2, 1.5), player.getName()+"_"+tower+"_"+level+"_"+id, YamlConfiguration.loadConfiguration(levelFile).getString("UnitName"));
                                                    HoloUtil.addHoloLine(viewLoc.getWorld(), player.getName()+"_"+tower+"_"+level+"_"+id, "§b██████████", 2);

                                                    HashMap<String, Object> unitData = RoleUtil.getRoleUnitData(role, tower, level);
                                                    int taskID = Bukkit.getScheduler().runTaskTimer(plugin, new UnitSpawner(Integer.parseInt(viewLoc.getWorld().getName()), player.getName(), role, tower, level, Integer.parseInt(unitData.get("SpawnRate").toString()), viewLoc), 0, 2).getTaskId();
                                                    PlayerInfo playerInfo = new PlayerInfo(player.getName());
                                                    try {
                                                        playerInfo.addTaskId(player.getName(), taskID);
                                                        viewLoc.getBlock().setMetadata("taskID", new FixedMetadataValue(plugin, taskID));
                                                    } catch (IOException e) {
                                                        ErrorUtil.error(null, e.getLocalizedMessage());
                                                    }
                                                }
                                            });
                                        } else {
                                            player.closeInventory();
                                            player.sendMessage(MessagesUtil.messageString("castlefight.errors.cannot-place-tower"));
                                        }
                                    }
                                } else {
                                    event.getWhoClicked().closeInventory();
                                    event.getWhoClicked().sendMessage(MessagesUtil.messageString("castlefight.errors.cannot-place-tower"));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
