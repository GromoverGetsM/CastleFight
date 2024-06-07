package ru.rstudios.castlefight.utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class GameModeUtil implements Listener {

    GameMode mode = null;

    public void SetMode(String modeset, Player player) {
        if (modeset == "1") {
            GameMode mode = GameMode.CREATIVE;
        } else if (modeset == "2") {
            GameMode mode = GameMode.ADVENTURE;
        } else if (modeset == "3") {
            GameMode mode = GameMode.SPECTATOR;
        }
        player.setGameMode(mode);
    }
}
