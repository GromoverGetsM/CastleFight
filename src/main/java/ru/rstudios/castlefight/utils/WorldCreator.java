package ru.rstudios.castlefight.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ru.rstudios.castlefight.CastleFight.*;

public class WorldCreator {
    public int createGameWorld (int players) throws IOException {
        Random random = new Random();
        int ID;

        FileConfiguration games = fileUtil.loadFile("activeGames.yml");
        if (games == null) {
            return -1;
        }

        do {
            ID = random.nextInt(1, 1000000);
        } while (games.getString(ID + "_" + players) != null);

        Bukkit.createWorld(new org.bukkit.WorldCreator(String.valueOf(ID)));
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
                fileUtil.copyFilesTo(world, mainWorldFile);
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

    public boolean deleteGameWorld (int ID) throws IOException {
        World world = Bukkit.getWorld(String.valueOf(ID));
        if (world != null) {
            File worldFile = new File(Bukkit.getServer().getWorldContainer() + File.separator + ID + File.separator);

            for (Player player : world.getPlayers()) {
                player.sendMessage(messagesUtil.messageString("castlefight.main.game-ended"));
                player.teleport(new Location(Bukkit.getWorld("world"), 0, 64, 0));
            }

            Bukkit.unloadWorld(world, false);
            FileConfiguration games = fileUtil.loadFile("activeGames.yml");
            if (games != null) {
                games.set(String.valueOf(ID), null);
                games.save(new File(plugin.getDataFolder(), "activeGames.yml"));
            }
            return worldFile.delete();
        }

        return false;
    }
}
