package ru.rstudios.castlefight.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemUtil {
    public static void setItem (Material material, Player player, String configName, int amount, int slot) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta menuMeta = item.getItemMeta();
        menuMeta.setDisplayName(MessagesUtil.messageString("castlefight.items."+configName+".name"));
        List<String> lore = MessagesUtil.messageList("castlefight.items."+configName+".lore");
        menuMeta.setLore(lore);
        item.setItemMeta(menuMeta);
        player.getInventory().setItem(slot, item);
    }
}