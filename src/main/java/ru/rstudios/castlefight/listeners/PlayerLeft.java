package ru.rstudios.castlefight.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.rstudios.castlefight.modules.PlayerInfo;

import java.io.IOException;
import java.util.List;

public class PlayerLeft implements Listener {
    @EventHandler
    public void onPlayerLeft (PlayerQuitEvent event) throws IOException {
        Player player = event.getPlayer();
        PlayerInfo playerInfo = new PlayerInfo(player);

        if (playerInfo.getGameID() != -1) {
            playerInfo.setGameID(player.getName(), -1);
            List<Integer> tasks = playerInfo.getTasksID();

            if (!tasks.isEmpty()) {
                for (int taskID : tasks) {
                    Bukkit.getScheduler().cancelTask(taskID);
                }

                tasks.clear();
            }

            playerInfo.setTasks(player.getName(), tasks);
        }
    }
}
