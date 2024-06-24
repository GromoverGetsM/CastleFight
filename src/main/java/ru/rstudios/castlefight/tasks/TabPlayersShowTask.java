package ru.rstudios.castlefight.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static ru.rstudios.castlefight.CastleFight.plugin;

public class TabPlayersShowTask implements Runnable {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                if (player.getWorld().equals(otherPlayer.getWorld())) {
                    player.showPlayer(plugin, otherPlayer);
                } else {
                    player.hidePlayer(plugin, otherPlayer);
                }
            }
        }
    }
}
