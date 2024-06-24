package ru.rstudios.castlefight.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreBoardUtil {

    private static final ScoreboardManager manager = Bukkit.getScoreboardManager();
    private static final Map<String, Scoreboard> scoreboards = new HashMap<>();

    public static void createScoreboard(String name, String displayName) {
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective(name, "dummy", ChatColor.translateAlternateColorCodes('&', displayName));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        scoreboards.put(name, board);
    }

    public static void deleteScoreboard(String name) {
        scoreboards.remove(name);
    }

    public static void showScoreboard(String name, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null && scoreboards.containsKey(name)) {
            player.setScoreboard(scoreboards.get(name));
        }
    }

    public static void hideScoreboard(String name, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null && scoreboards.containsKey(name)) {
            player.setScoreboard(manager.getNewScoreboard());
        }
    }

    public static void setScore(String scoreboardName, String text, int score) {
        if (scoreboards.containsKey(scoreboardName)) {
            Scoreboard board = scoreboards.get(scoreboardName);
            Objective objective = board.getObjective(scoreboardName);
            if (objective != null) {
                Score scoreLine = objective.getScore(text);
                scoreLine.setScore(score);
            }
        }
    }

    public static void removeScore(String scoreboardName, int score) {
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

    public static void loadScoreboard(List<String> lines, String scoreboardName) {
        if (scoreboards.containsKey(scoreboardName)) {
            Scoreboard board = scoreboards.get(scoreboardName);
            Objective objective = board.getObjective(scoreboardName);
            if (objective != null) {
                int score = 99;
                for (String line : lines) {
                    Score scoreLine = objective.getScore(ChatColor.translateAlternateColorCodes('&', line));
                    scoreLine.setScore(score);
                    score--;
                    if (score < 0) break;
                }
            }
        }
    }
}
