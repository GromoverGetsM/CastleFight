package ru.rstudios.castlefight.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreBoardUtil {

    private static final ScoreboardManager manager = Bukkit.getScoreboardManager();
    public static final Map<String, Scoreboard> scoreboards = new HashMap<>();

    public static void createScoreboard (String name, String displayName) {
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective(name, "dummy", ChatColor.translateAlternateColorCodes('&', displayName));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        scoreboards.put(name, board);
    }

    public static void createConfigScoreboard (String name, String configName, String playerName) {
        FileConfiguration messages = FileUtil.loadFile("messages.yml");
        String title = MessagesUtil.messageString("castlefight.scoreboards." + configName + ".title");
        List<String> scoreLines = messages.getStringList("castlefight.scoreboards." + configName + ".lines");

        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective(name, "dummy", ChatColor.translateAlternateColorCodes('&', title));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        scoreboards.put(name, board);
        loadScoreboard(scoreLines, name, playerName);
    }

    public static void deleteScoreboard(String name, String playerName) {
        hideScoreboard(name, playerName);
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
                board.resetScores(text);
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

    public static void loadScoreboard(List<String> lines, String scoreboardName, String playerName) {
        if (scoreboards.containsKey(scoreboardName)) {
            Scoreboard board = scoreboards.get(scoreboardName);
            Objective objective = board.getObjective(scoreboardName);
            if (objective != null) {
                for (String entry : board.getEntries()) {
                    board.resetScores(entry);
                }

                int score = 99;
                for (String line : lines) {
                    String parsedLine = ChatColor.translateAlternateColorCodes('&', PlaceholderUtil.replacePlaceholders(playerName, line));
                    Score scoreLine = objective.getScore(parsedLine);
                    scoreLine.setScore(score);
                    score--;
                    if (score < 0) break;
                }
            }
        }
    }
}