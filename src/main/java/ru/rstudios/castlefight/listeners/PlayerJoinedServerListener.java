package ru.rstudios.castlefight.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;

import static ru.rstudios.castlefight.CastleFight.*;

public class PlayerJoinedServerListener implements Listener {
    @EventHandler
    public void onPlayerJoined(PlayerJoinEvent event) throws IOException {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (!new File(new File(plugin.getDataFolder(), "data"), playerName + ".yml").exists()) {
            fileUtil.createNewFile("data", playerName + ".yml");
            dataUtil.savePlayersDataTemplate(playerName, "data");
        }
        itemUtil.setItem(Material.BOOK, player, "main_menu",1, 4);
    }
}
