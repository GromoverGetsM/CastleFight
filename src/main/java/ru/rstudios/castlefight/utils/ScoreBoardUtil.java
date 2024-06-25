package ru.rstudios.castlefight.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.rstudios.castlefight.CastleFight.*;

public class ScoreBoardUtil {

    private final ScoreboardManager manager = Bukkit.getScoreboardManager();
    public final Map<String, Scoreboard> scoreboards = new HashMap<>();

    public void createScoreboard (String name, String displayName) {
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective(name, "dummy", ChatColor.translateAlternateColorCodes('&', displayName));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        scoreboards.put(name, board);
    }

    public void createConfigScoreboard (String name, String configName, String playerName) {
        FileConfiguration messages = fileUtil.loadFile("messages.yml");
        String title = messagesUtil.messageString("castlefight.scoreboards." + configName + ".title");
        List<String> scoreLines = messages.getStringList("castlefight.scoreboards." + configName + ".lines");

        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective(name, "dummy", ChatColor.translateAlternateColorCodes('&', title));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        scoreboards.put(name, board);
        loadScoreboard(scoreLines, name, playerName);
    }

    public void deleteScoreboard(String name, String playerName) {
        hideScoreboard(name, playerName);
        scoreboards.remove(name);
    }

    public void showScoreboard(String name, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null && scoreboards.containsKey(name)) {
            player.setScoreboard(scoreboards.get(name));
        }
    }

    public void hideScoreboard(String name, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null && scoreboards.containsKey(name)) {
            player.setScoreboard(manager.getNewScoreboard());
        }
    }

    public void setScore(String scoreboardName, String text, int score) {
        if (scoreboards.containsKey(scoreboardName)) {
            Scoreboard board = scoreboards.get(scoreboardName);
            Objective objective = board.getObjective(scoreboardName);
            if (objective != null) {
                board.resetScores(text);
                Score scoreLine = objective.getScore(text);
                scoreLine.setScore(score);
            }
        }
    }


    public void removeScore(String scoreboardName, int score) {
        if (scoreboards.containsKey(scoreboardName)) {
            Scoreboard board = scoreboards.get(scoreboardName);
            Objective objective = board.getObjective(scoreboardName);
            if (objective != null) {
                for (String entry : board.getEntries()) {
                    if (objective.getScore(entry).getScore() == score) {
                        board.resetScores(entry);
                        break;
                    }
                }
            }
        }
    }

    public void loadScoreboard(List<String> lines, String scoreboardName, String playerName) {
        if (scoreboards.containsKey(scoreboardName)) {
            Scoreboard board = scoreboards.get(scoreboardName);
            Objective objective = board.getObjective(scoreboardName);
            if (objective != null) {
                for (String entry : board.getEntries()) {
                    board.resetScores(entry);
                }

                int score = 99;
                for (String line : lines) {
                    String parsedLine = ChatColor.translateAlternateColorCodes('&', placeholderUtil.replacePlaceholders(playerName, line));
                    Score scoreLine = objective.getScore(parsedLine);
                    scoreLine.setScore(score);
                    score--;
                    if (score < 0) break;
                }
            }
        }
    }

}