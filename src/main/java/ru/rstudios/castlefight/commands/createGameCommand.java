package ru.rstudios.castlefight.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.rstudios.castlefight.modules.GameInfo;
import ru.rstudios.castlefight.tasks.IncomeTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.rstudios.castlefight.CastleFight.*;

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
                FileConfiguration data = dataUtil.loadPlayerData(sender.getName());
                data.set("gameID", ID);
                data.set("lastGameTime", System.currentTimeMillis());

                GameInfo gameInfo = new GameInfo(ID);
                try {
                    gameInfo.setPlayerTeam(sender.getName(), "blue");
                    gameInfo.setPlayerActiveRole(sender.getName(), "elfs");
                    gameInfo.setPlayerBalance(sender.getName(), 200);
                    gameInfo.setPlayerTowerLimit(sender.getName(), gameInfo.getExpectedPerTeamTowers()/gameInfo.getTeamList(gameInfo.getPlayerTeam(sender.getName())).size());
                    gameInfo.setPlayerIncome(sender.getName(), 20);
                    Bukkit.getScheduler().runTaskTimer(plugin, new IncomeTask(sender.getName()), 0, 20);
                } catch (IOException e) {
                    errorUtil.error(null, e.getLocalizedMessage());
                }
                ((Player) sender).teleport(new Location(Bukkit.getWorld(String.valueOf(ID)), 0, 64, 0));
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
