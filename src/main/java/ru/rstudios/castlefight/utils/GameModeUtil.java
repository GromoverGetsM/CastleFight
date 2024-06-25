package ru.rstudios.castlefight.utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class GameModeUtil implements Listener {

    private static GameMode mode = null;

    public static void SetMode(String modeset, Player player) {
        switch (modeset) {
            case "0", "s", "survival":
                mode = GameMode.SURVIVAL;
                break;
            case "1", "c", "creative":
                mode = GameMode.CREATIVE;
                break;
            case "2", "a", "adventure":
                mode = GameMode.ADVENTURE;
                break;
            case "3", "sp", "spectator":
                mode = GameMode.SPECTATOR;
                break;
            default:
                ErrorUtil.warningfromconfig(player, "castlefight.errors.invalid-args");
                break;
        }
        player.setGameMode(mode);
    }
}