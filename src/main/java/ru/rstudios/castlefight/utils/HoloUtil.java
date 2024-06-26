package ru.rstudios.castlefight.utils;

import org.bukkit.*;
import org.bukkit.entity.ArmorStand;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

import static ru.rstudios.castlefight.CastleFight.plugin;

public class HoloUtil {

    public static void createHologram (Location l, String name, String displayName) {
        ArmorStand a = (ArmorStand) l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);

        PersistentDataContainer pdc = a.getPersistentDataContainer();
        NamespacedKey holoName = new NamespacedKey(plugin, "name");
        NamespacedKey line = new NamespacedKey(plugin, "line");
        pdc.set(holoName, PersistentDataType.STRING, name);
        pdc.set(line, PersistentDataType.INTEGER, 1);

        a.setVisible(false);
        a.setGravity(false);
        a.setCustomName(ChatColor.translateAlternateColorCodes('&', displayName));
        a.setCustomNameVisible(true);
    }

    public static void deleteHolo (World world, String name) {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof ArmorStand) {
                PersistentDataContainer pdc = entity.getPersistentDataContainer();
                if (!pdc.isEmpty()) {
                    NamespacedKey holoName = new NamespacedKey(plugin, "name");
                    if (pdc.get(holoName, PersistentDataType.STRING) != null && pdc.get(holoName, PersistentDataType.STRING).equals(name)) {
                        entity.remove();
                    }
                }
            }
        }
    }

    public static void createHoloLine (Location l, String name, String displayName, int line) {
        ArmorStand a = (ArmorStand) l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);

        PersistentDataContainer pdc = a.getPersistentDataContainer();
        NamespacedKey holoName = new NamespacedKey(plugin, "name");
        NamespacedKey lineKey = new NamespacedKey(plugin, "line");
        pdc.set(holoName, PersistentDataType.STRING, name);
        pdc.set(lineKey, PersistentDataType.INTEGER, line);

        a.setVisible(false);
        a.setGravity(false);
        a.setCustomName(ChatColor.translateAlternateColorCodes('&', displayName));
        a.setCustomNameVisible(true);
    }

    public static void addHoloLine (World world, String name, String text, int line) {
        int maxLine = 0;
        Location lastArmorStandLocation = null;

        for (Entity entity : world.getEntities()) {
            if (entity instanceof ArmorStand) {
                PersistentDataContainer pdc = entity.getPersistentDataContainer();
                if (!pdc.isEmpty()) {
                    NamespacedKey holoName = new NamespacedKey(plugin, "name");
                    if (pdc.get(holoName, PersistentDataType.STRING) != null && pdc.get(holoName, PersistentDataType.STRING).equals(name)) {
                        NamespacedKey lineKey = new NamespacedKey(plugin, "line");
                        if (pdc.get(lineKey, PersistentDataType.INTEGER) != null && pdc.get(lineKey, PersistentDataType.INTEGER) > maxLine) {
                            maxLine = pdc.get(lineKey, PersistentDataType.INTEGER);
                            lastArmorStandLocation = entity.getLocation();
                        }
                    }
                }
            }
        }

        if (lastArmorStandLocation != null) {
            createHoloLine(lastArmorStandLocation.add(0.0, -0.25, 0.0), name, text, line);
        }
    }

    public static void setHoloLine (World world, String name, String text, int line) {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof ArmorStand) {
                PersistentDataContainer pdc = entity.getPersistentDataContainer();
                if (!pdc.isEmpty()) {
                    NamespacedKey holoName = new NamespacedKey(plugin, "name");
                    if (pdc.get(holoName, PersistentDataType.STRING) != null && pdc.get(holoName, PersistentDataType.STRING).equals(name)) {
                        NamespacedKey lineKey = new NamespacedKey(plugin, "line");
                        if (pdc.get(lineKey, PersistentDataType.INTEGER) != null && pdc.get(lineKey, PersistentDataType.INTEGER) == line) {
                            entity.setCustomName(text);
                            entity.setCustomNameVisible(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    public static void deleteHoloLine (World world, String name, int line) {
        boolean hasMoreLines = false;
        HashMap<Entity, Location> moreLines = new HashMap<>();

        for (Entity entity : world.getEntities()) {
            if (entity instanceof ArmorStand) {
                PersistentDataContainer pdc = entity.getPersistentDataContainer();
                if (!pdc.isEmpty()) {
                    NamespacedKey holoName = new NamespacedKey(plugin, "name");
                    if (pdc.get(holoName, PersistentDataType.STRING) != null && pdc.get(holoName, PersistentDataType.STRING).equals(name)) {
                        NamespacedKey lineKey = new NamespacedKey(plugin, "line");
                        if (pdc.get(lineKey, PersistentDataType.INTEGER) != null && pdc.get(lineKey, PersistentDataType.INTEGER) == line) {
                            entity.remove();
                        } else if (pdc.get(lineKey, PersistentDataType.INTEGER) != null && pdc.get(lineKey, PersistentDataType.INTEGER) > line) {
                            moreLines.put(entity, entity.getLocation());
                            hasMoreLines = true;
                        }
                    }
                }
            }
        }

        if (hasMoreLines && !moreLines.isEmpty()) {
            for (Entity key : moreLines.keySet()) {
                key.teleport(moreLines.get(key).add(0, 0.25, 0));
            }
        }
    }
}
