package ru.rstudios.castlefight.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.rstudios.castlefight.utils.ErrorUtil;
import ru.rstudios.castlefight.utils.FileUtil;
import ru.rstudios.castlefight.utils.WorldCreator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static ru.rstudios.castlefight.CastleFight.*;

public class closeGameCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 1) {
            try {
                WorldCreator.deleteGameWorld(Integer.parseInt(args[0]));
            } catch (IOException e) {
                ErrorUtil.criterror(null, e.getLocalizedMessage());
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        List<String> pArgs = new ArrayList<>();
        if (args.length == 1) {
            FileConfiguration games = FileUtil.loadFile("activeGames.yml");
            Set<String> keys = games.getKeys(false);

            for (String key : keys) {
                String status = games.getString(key);
                if ("active".equals(status)) {
                    pArgs.add(key);
                }
            }
        }
        return pArgs;
    }
}
