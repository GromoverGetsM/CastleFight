package ru.rstudios.castlefight.utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import static ru.rstudios.castlefight.CastleFight.errorUtil;

public class GameModeUtil implements Listener {

    GameMode mode = null;

    public void SetMode(String modeset, Player player) {
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
                errorUtil.warningfromconfig(player, "castlefight.errors.invalid-args");
                break;
        }
        player.setGameMode(mode);
    }
}