package ru.rstudios.castlefight.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
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

import static ru.rstudios.castlefight.CastleFight.fileUtil;
import static ru.rstudios.castlefight.CastleFight.messagesUtil;

public class InventoryUtil {

    public static Inventory inventoryFromConfig(@NotNull String configName, @NotNull Player player) {
        FileConfiguration menuConfig = FileUtil.loadFile("messages.yml");
        ConfigurationSection menuSection = menuConfig.getConfigurationSection("castlefight.menus." + configName);

        if (menuSection != null) {
            Inventory inv = Bukkit.createInventory(player, menuConfig.getInt("castlefight.menus." + configName + ".size"), MessagesUtil.messageString("castlefight.menus." + configName + ".title"));
            ConfigurationSection itemsSection = menuConfig.getConfigurationSection("castlefight.menus." + configName + ".items");

            if (itemsSection != null) {
                for (String key : itemsSection.getKeys(false)) {
                    String name = ChatColor.translateAlternateColorCodes('&', itemsSection.getString(key + ".name"));
                    int amount = itemsSection.getInt(key + ".amount", 1);
                    List<String> lore = itemsSection.getStringList(key + ".lore");
                    List<String> enchants = itemsSection.getStringList(key + ".enchantments");

                    lore.replaceAll(textToTranslate -> ChatColor.translateAlternateColorCodes('&', textToTranslate));

                    Material type = Material.matchMaterial(itemsSection.getString(key + ".type", "STONE"));
                    if (type == null) {
                        type = Material.STONE;
                    }

                    ItemStack item = new ItemStack(type, amount);
                    ItemMeta meta = item.getItemMeta();

                    if (meta != null) {
                        meta.setDisplayName(name);
                        meta.setLore(lore);

                        if (type == Material.PLAYER_HEAD) {
                            String texture = itemsSection.getString(key + ".texture");
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

                        for (String enchantmentInfo : enchants) {
                            String[] enchantData = enchantmentInfo.split(":");
                            if (enchantData.length == 2) {
                                String enchantmentName = enchantData[0].trim().toLowerCase();
                                int level = Integer.parseInt(enchantData[1].trim());
                                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentName));
                                if (enchantment != null) {
                                    meta.addEnchant(enchantment, level, true);
                                }
                            }
                        }

                        item.setItemMeta(meta);
                    }

                    String permission = itemsSection.getString(key + ".viewPermission");
                    if (permission != null && Bukkit.getPluginManager().getPermission(permission) == null) {
                        addPermission(permission);
                    }

                    if (permission == null || player.hasPermission(permission)) {
                        if (itemsSection.isList(key + ".slots")) {
                            List<Integer> slots = itemsSection.getIntegerList(key + ".slots");
                            for (int slot : slots) {
                                inv.setItem(slot, item);
                            }
                        } else {
                            int slot = itemsSection.getInt(key + ".slot", 1);
                            inv.setItem(slot, item);
                        }
                    }
                }
            }

            return inv;
        } else {
            return Bukkit.createInventory(player, 9, ChatColor.translateAlternateColorCodes('&', "&cИнвентарь не найден"));
        }
    }


    private static void addPermission (String name) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        Permission permission = new Permission(name);
        pluginManager.addPermission(permission);
    }
}