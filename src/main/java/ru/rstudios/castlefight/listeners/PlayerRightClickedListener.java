package ru.rstudios.castlefight.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import static ru.rstudios.castlefight.CastleFight.*;

public class PlayerRightClickedListener implements Listener {

    @EventHandler
    public void onPlayerRightClicked (PlayerInteractEvent event) {
        if (event.getAction().isRightClick()) {
            Player player = event.getPlayer();
            if (player.getInventory().getItemInMainHand().getType().getKey() == Material.GOLDEN_AXE.getKey()) {
                Inventory towerList = Bukkit.createInventory(player, 36, messagesUtil.messageString("castlefight.menus.towers.title"));
                player.openInventory(towerList);
            }
        }
    }
}
