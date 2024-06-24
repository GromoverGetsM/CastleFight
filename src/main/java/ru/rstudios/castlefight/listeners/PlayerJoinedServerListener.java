package ru.rstudios.castlefight.listeners;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
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
        if (messagesUtil.messageString("castlefight.items.main_menu.give-on-join").equalsIgnoreCase("true") && fileUtil.loadFile("messages.yml").getString("castlefight.items.main_menu.slot") != null && fileUtil.loadFile("messages.yml").getInt("castlefight.items.main_menu.slot") >= 0 && fileUtil.loadFile("messages.yml").getInt("castlefight.items.main_menu.slot") <= 8) {
            itemUtil.setItem(Material.BOOK, player, "main_menu",1, fileUtil.loadFile("messages.yml").getInt("castlefight.items.main_menu.slot"));
        }

        YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + "data" + File.separator + playerName + ".yml")).set("lastJoinTime", System.currentTimeMillis());
        YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + "data" + File.separator + playerName + ".yml")).save(new File(plugin.getDataFolder() + File.separator + "data" + File.separator + playerName + ".yml"));
    }
}
