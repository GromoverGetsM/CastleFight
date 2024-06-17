package ru.rstudios.castlefight.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static ru.rstudios.castlefight.CastleFight.*;

public class InventoryUtil {

    public Inventory inventoryFromConfig(@NotNull String configName, @NotNull Player player) {
        FileConfiguration menuConfig = fileUtil.loadFile("messages.yml");
        Inventory inv = Bukkit.createInventory(player, menuConfig.getInt("castlefight.menus." + configName + ".size"), messagesUtil.messageString("castlefight.menus." + configName + ".title"));
        Map<String, Object> items = menuConfig.getConfigurationSection("castlefight.menus." + configName + ".items").getValues(false);

        if (!items.isEmpty()) {
            for (String key : items.keySet()) {
                String name = ChatColor.translateAlternateColorCodes('&', menuConfig.getString("castlefight.menus." + configName + ".items." + key + ".name")) != null ? ChatColor.translateAlternateColorCodes('&', menuConfig.getString("castlefight.menus." + configName + ".items." + key + ".name", "Безымянный предмет")) : " ";
                int amount = menuConfig.getInt("castlefight.menus." + configName + ".items." + key + ".amount", 1);
                int slot = menuConfig.getInt("castlefight.menus." + configName + ".items." + key + ".slot", 1);
                String typeString = menuConfig.getString("castlefight.menus." + configName + ".items." + key + ".type", "STONE");
                List<String> lore = menuConfig.getStringList("castlefight.menus." + configName + ".items." + key + ".lore");

                for (int i = 0; i < lore.size(); i++) {
                    lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
                }

                Material type = Material.matchMaterial(typeString);
                if (type == null) {
                    type = Material.STONE;
                }

                ItemStack item = new ItemStack(type, amount);
                ItemMeta meta = item.getItemMeta();

                if (meta != null) {
                    meta.setDisplayName(name);
                    meta.setLore(lore);

                    if (type == Material.PLAYER_HEAD) {
                        String texture = menuConfig.getString("castlefight.menus." + configName + ".items." + key + ".texture");
                        if (texture != null) {
                            SkullMeta skullMeta = (SkullMeta) meta;

                            GameProfile profile = new GameProfile(player.getUniqueId(), player.getName());
                            profile.getProperties().put("textures", new Property("textures", texture));
                            try {
                                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                                profileField.setAccessible(true);
                                profileField.set(skullMeta, profile);
                            } catch (NoSuchFieldException | IllegalAccessException e) {
                                e.printStackTrace();
                            }

                            item.setItemMeta(skullMeta);
                        } else {
                            item = new ItemStack(Material.PLAYER_HEAD, amount);
                            meta = item.getItemMeta();
                            SkullMeta skullMeta = (SkullMeta) meta;
                            skullMeta.setOwner("Steve");
                            item.setItemMeta(skullMeta);
                        }
                    }

                    if (menuConfig.contains("castlefight.menus." + configName + ".items." + key + ".enchantments")) {
                        Map<String, Object> enchantments = menuConfig.getConfigurationSection("castlefight.menus." + configName + ".items." + key + ".enchantments").getValues(false);
                        for (String enchantmentKey : enchantments.keySet()) {
                            Enchantment enchantment = Enchantment.getByKey(org.bukkit.NamespacedKey.minecraft(enchantmentKey.toLowerCase()));
                            int level = (int) enchantments.get(enchantmentKey);
                            if (enchantment != null) {
                                meta.addEnchant(enchantment, level, true);
                            }
                        }
                    }

                    item.setItemMeta(meta);
                }

                String permission = menuConfig.getString("castlefight.menus." + configName + ".items." + key + ".viewPermission");
                if (permission != null && Bukkit.getPluginManager().getPermission(permission) == null) {
                    addPermission(permission);
                }

                if (permission != null) {
                    if (player.hasPermission(permission)) {
                        inv.setItem(slot, item);
                    }
                } else {
                    inv.setItem(slot, item);
                }
            }
        }

        return inv;
    }

    private void addPermission (String name) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        Permission permission = new Permission(name);
        pluginManager.addPermission(permission);
    }
}