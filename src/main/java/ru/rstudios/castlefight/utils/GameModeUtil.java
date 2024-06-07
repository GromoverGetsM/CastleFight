package ru.rstudios.castlefight.utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class GameModeUtil implements Listener {

    GameMode mode = null;

    public void SetMode(String modeset, Player player) {
        if (modeset == "c") {
            GameMode mode = GameMode.CREATIVE;
        } else if (modeset == "a") {
            GameMode mode = GameMode.ADVENTURE;
        } else if (modeset == "s") {
            GameMode mode = GameMode.SPECTATOR;
        }
        player.setGameMode(mode);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player pl = e.getPlayer();
        SetMode("c",pl);
    }

}
