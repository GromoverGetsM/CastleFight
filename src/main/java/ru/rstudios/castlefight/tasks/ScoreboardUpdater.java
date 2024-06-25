package ru.rstudios.castlefight.tasks;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.rstudios.castlefight.modules.PlayerInfo;

import java.util.List;

import static ru.rstudios.castlefight.CastleFight.*;

public class ScoreboardUpdater implements Runnable {

    private String playerName;
    public ScoreboardUpdater (String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void run() {
        PlayerInfo playerInfo = new PlayerInfo(playerName);

        if (playerInfo.getGameID() == -1) {
            scoreBoardUtil.deleteScoreboard(playerName + "_" + playerInfo.getGameID(), playerName);
            return;
        }

        String scoreboardName = playerName + "_" + playerInfo.getGameID();
        if (!scoreBoardUtil.scoreboards.containsKey(scoreboardName)) {
            scoreBoardUtil.createConfigScoreboard(scoreboardName, "game_scoreboard", playerName);
        } else {
            FileConfiguration messages = fileUtil.loadFile("messages.yml");
            List<String> scoreLines = messages.getStringList("castlefight.scoreboards.game_scoreboard.lines");
            scoreBoardUtil.loadScoreboard(scoreLines, scoreboardName, playerName);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getName().equals(playerName)) {
                scoreBoardUtil.hideScoreboard(scoreboardName, player.getName());
            }
        }

        scoreBoardUtil.showScoreboard(scoreboardName, playerName);
    }

}
