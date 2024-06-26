package ru.rstudios.castlefight.tasks;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.rstudios.castlefight.modules.PlayerInfo;
import ru.rstudios.castlefight.utils.FileUtil;
import ru.rstudios.castlefight.utils.ScoreBoardUtil;

import java.util.List;

import static ru.rstudios.castlefight.CastleFight.plugin;

public class ScoreboardUpdater implements Runnable {

    private final String playerName;

    public ScoreboardUpdater(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void run() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            PlayerInfo playerInfo = new PlayerInfo(playerName);

            if (playerInfo.getGameID() == -1) {
                Bukkit.getScheduler().runTask(plugin, () ->
                        ScoreBoardUtil.deleteScoreboard(playerName + "_" + playerInfo.getGameID(), playerName));
                return;
            }

            String scoreboardName = playerName + "_" + playerInfo.getGameID();
            if (!ScoreBoardUtil.scoreboards.containsKey(scoreboardName)) {
                Bukkit.getScheduler().runTask(plugin, () ->
                        ScoreBoardUtil.createConfigScoreboard(scoreboardName, "game_scoreboard", playerName));
            } else {
                FileConfiguration messages = FileUtil.loadFile("messages.yml");
                List<String> scoreLines = messages.getStringList("castlefight.scoreboards.game_scoreboard.lines");
                Bukkit.getScheduler().runTask(plugin, () ->
                        ScoreBoardUtil.loadScoreboard(scoreLines, scoreboardName, playerName));
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!player.getName().equals(playerName)) {
                        ScoreBoardUtil.hideScoreboard(scoreboardName, player.getName());
                    }
                }
                ScoreBoardUtil.showScoreboard(scoreboardName, playerName);
            });
        });
    }
}
