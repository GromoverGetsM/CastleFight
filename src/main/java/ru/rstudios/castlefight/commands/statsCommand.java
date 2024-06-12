package ru.rstudios.castlefight.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static ru.rstudios.castlefight.CastleFight.*;

public class statsCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("rating") || args[0].equalsIgnoreCase("money")) {
                    String data = dataUtil.returnData(player.getName(), args[0].toLowerCase()).toString();
                    sender.sendMessage(messagesUtil.messageString("castlefight.commands.stats."+args[0].toLowerCase()).replace("%value%", data));
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        List<String> pArgs = new ArrayList<>();
        switch (args.length) {
            case 1, 2:
                pArgs.add("Money");
                pArgs.add("Rating");
                break;
            default:
                break;
        }
        return pArgs;
    }
}
