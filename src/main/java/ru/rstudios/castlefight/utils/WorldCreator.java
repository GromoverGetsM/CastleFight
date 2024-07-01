package ru.rstudios.castlefight.utils;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.rstudios.castlefight.modules.PlayerInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ru.rstudios.castlefight.CastleFight.plugin;

public class WorldCreator {
    public static int createGameWorld (int players) throws IOException {
        Random random = new Random();
        int ID;

        FileConfiguration games = FileUtil.loadFile("activeGames.yml");

        do {
            ID = random.nextInt(1, 1000000);
        } while (games.getString(String.valueOf(ID)) != null);

        World created = Bukkit.createWorld(new org.bukkit.WorldCreator(String.valueOf(ID)));
        if (created != null) {
            created.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            created.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            created.setGameRule(GameRule.DO_ENTITY_DROPS, false);
            created.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
            created.setGameRule(GameRule.DO_MOB_LOOT, false);
            created.setGameRule(GameRule.DO_TILE_DROPS, false);
            created.setGameRule(GameRule.MOB_GRIEFING, false);
            created.setGameRule(GameRule.DO_MOB_SPAWNING, false);

            created.setTime(12000);
        }
        Bukkit.unloadWorld(String.valueOf(ID), true);

        File maps = new File(plugin.getDataFolder(), "maps");
        if (maps.exists() && maps.isDirectory()) {
            File[] allMaps = maps.listFiles(File::isDirectory);
            if (allMaps != null) {
                List<File> matchingFolders = new ArrayList<>();
                String prefix = players + "_";
                for (File folder : allMaps) {
                    if (folder.getName().startsWith(prefix)) {
                        matchingFolders.add(folder);
                    }
                }

                File world = matchingFolders.get(random.nextInt(matchingFolders.size()));
                File mainWorldFile = new File(Bukkit.getServer().getWorldContainer() + File.separator + ID + File.separator);
                FileUtil.copyFilesTo(world, mainWorldFile);
                games.set(String.valueOf(ID), "active");
                games.save(new File(plugin.getDataFolder(), "activeGames.yml"));
                Bukkit.createWorld(new org.bukkit.WorldCreator(String.valueOf(ID)));
            } else {
                return -1;
            }
        } else {
            return -1;
        }


        return ID;
    }

    public static void deleteGameWorld (int ID) throws IOException {
        World world = Bukkit.getWorld(String.valueOf(ID));
        if (world != null) {
            File worldFile = new File(Bukkit.getServer().getWorldContainer() + File.separator + ID + File.separator);

            BossBarUtil.deleteBossbar(ID + "_redTeam");
            BossBarUtil.deleteBossbar(ID + "_blueTeam");

            for (Player player : world.getPlayers()) {
                player.sendMessage(MessagesUtil.messageString("castlefight.main.game-ended"));
                player.getInventory().clear();
                player.teleport(new Location(Bukkit.getWorld("world"), 0, 64, 0));
                PlayerInfo playerInfo = new PlayerInfo(player.getName());
                List<Integer> tasks = playerInfo.getTasksID();

                if (!tasks.isEmpty()) {
                    for (int taskID : tasks) {
                        Bukkit.getScheduler().cancelTask(taskID);
                    }

                    tasks.clear();
                }

                playerInfo.setTasks(player.getName(), tasks);
                ScoreBoardUtil.deleteScoreboard(player.getName() + "_" + ID, player.getName());
            }

            Bukkit.unloadWorld(world, false);
            FileConfiguration games = FileUtil.loadFile("activeGames.yml");
            games.set(String.valueOf(ID), null);
            games.save(new File(plugin.getDataFolder(), "activeGames.yml"));
            FileUtil.deleteWorld(worldFile);
        }
    }
}
