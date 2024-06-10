package ru.rstudios.castlefight.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

import static ru.rstudios.castlefight.CastleFight.*;

public class ServerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoined(PlayerJoinEvent event) throws IOException {
        Player player = event.getPlayer();
        String playerName = player.getName();

        fileUtil.createNewFile("data", playerName + ".yml");
        itemUtil.setItem(Material.BOOK, player, "main_menu",1, 4);
    }
}
