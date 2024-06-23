package ru.rstudios.castlefight.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.rstudios.castlefight.CastleFight.errorUtil;
import static ru.rstudios.castlefight.CastleFight.worldCreator;

public class createGameCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length >= 1) {
            int players = Integer.parseInt(args[0]);
            int ID = 0;
            try {
                ID = worldCreator.createGameWorld(players);
            } catch (IOException e) {
                errorUtil.criterror(null, e.getLocalizedMessage());
            }

            if (sender instanceof Player && ID != -1) {
                ((Player) sender).teleport(new Location(Bukkit.getWorld(ID + "_" + players), 0, 64, 0));
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        List<String> pArgs = new ArrayList<>();
        if (args.length == 1) {
            pArgs.add("1");
            pArgs.add("2");
            pArgs.add("3");
            pArgs.add("4");
        }
        return pArgs;
    }

}
