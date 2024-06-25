package ru.rstudios.castlefight.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.rstudios.castlefight.modules.GameInfo;
import ru.rstudios.castlefight.modules.PlayerInfo;
import ru.rstudios.castlefight.tasks.IncomeTask;
import ru.rstudios.castlefight.tasks.ScoreboardUpdater;
import ru.rstudios.castlefight.utils.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.rstudios.castlefight.CastleFight.*;

public class createGameCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length >= 1) {
            sender.sendMessage(MessagesUtil.messageString("castlefight.commands.creategame.preparing"));
            int players = Integer.parseInt(args[0]);
            int ID = 0;
            try {
                ID = WorldCreator.createGameWorld(players);
            } catch (IOException e) {
                ErrorUtil.criterror(null, e.getLocalizedMessage());
            }

            if (sender instanceof Player && ID != -1) {
                FileConfiguration data = DataUtil.loadPlayerData(sender.getName());
                data.set("gameID", ID);
                data.set("lastGameTime", System.currentTimeMillis());
                try {
                    data.save(new File(plugin.getDataFolder() + File.separator + "data" + File.separator + sender.getName() + ".yml"));
                } catch (IOException e) {
                    ErrorUtil.error(null, e.getLocalizedMessage());
                }

                GameInfo gameInfo = new GameInfo(ID);
                sender.sendMessage(MessagesUtil.messageString("castlefight.commands.creategame.created", sender.getName()));
                try {
                    gameInfo.setPlayerTeam(sender.getName(), "blue");
                    gameInfo.setPlayerActiveRole(sender.getName(), "elfs");
                    gameInfo.setPlayerBalance(sender.getName(), 200);
                    gameInfo.setPlayerIncome(sender.getName(), 20);
                    gameInfo.updateGameInfo(ID);
                    int TaskID = Bukkit.getScheduler().runTaskTimer(plugin, new IncomeTask(sender.getName()), 0, 20).getTaskId();
                    PlayerInfo playerInfo = new PlayerInfo(sender.getName());
                    playerInfo.addTaskId(sender.getName(), TaskID);
                    gameInfo.setPlayerTowerLimit(sender.getName(), gameInfo.getExpectedPerTeamTowers()/gameInfo.getTeamList(gameInfo.getPlayerTeam(sender.getName())).size());
                    gameInfo.updateGameInfo(ID);
                } catch (IOException e) {
                    ErrorUtil.error(null, e.getLocalizedMessage());
                }
                ((Player) sender).teleport(new Location(Bukkit.getWorld(String.valueOf(ID)), 0, 64, 0));
                BossBarUtil.createBossbar(Objects.requireNonNull(Bukkit.getWorld(String.valueOf(ID))), ID + "_redTeam", ChatColor.translateAlternateColorCodes('&', PlaceholderUtil.replacePlaceholders(ID, "&f[&c%redHealth%&f/&c%baseHealth%&f]")), BarColor.RED, BarStyle.SOLID,true, 1);
                BossBarUtil.createBossbar(Bukkit.getWorld(String.valueOf(ID)), ID + "_blueTeam", ChatColor.translateAlternateColorCodes('&', PlaceholderUtil.replacePlaceholders(ID, "&f[&c%blueHealth%&f/&c%baseHealth%&f]")), BarColor.BLUE, BarStyle.SOLID,true, 1);

                int taskId = Bukkit.getScheduler().runTaskTimer(plugin, new ScoreboardUpdater(sender.getName()), 0, 20).getTaskId();
                PlayerInfo playerInfo = new PlayerInfo(sender.getName());
                try {
                    playerInfo.addTaskId(sender.getName(), taskId);
                } catch (IOException e) {
                    ErrorUtil.error(null, e.getLocalizedMessage());
                }
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
