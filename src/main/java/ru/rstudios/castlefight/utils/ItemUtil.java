package ru.rstudios.castlefight.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static ru.rstudios.castlefight.CastleFight.*;

public class ItemUtil {
    public void setItem (Material material, Player player, String configName, int amount, int slot) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta menuMeta = item.getItemMeta();
        menuMeta.setDisplayName(messagesUtil.messageString("castlefight.items."+configName+".name"));
        List<String> lore = messagesUtil.messageList("castlefight.items."+configName+".lore");
        menuMeta.setLore(lore);
        item.setItemMeta(menuMeta);
        player.getInventory().setItem(slot, item);
    }
}