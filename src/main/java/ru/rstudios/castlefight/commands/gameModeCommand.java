package ru.rstudios.castlefight.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static ru.rstudios.castlefight.CastleFight.*;

public class gameModeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 1) {
            if (sender instanceof Player) {
                if (sender.hasPermission("castlefight.gamemode")) {
                    gameModeUtil.SetMode(args[0], (Player) sender);
                } else {
                    errorUtil.error((Player) sender, fileUtil.loadFile("messages.yml").getString("castlefight.commands.gamemode.no-permission"));
                }
            } else {
                errorUtil.warning(null, messagesUtil.messageString("castlefight.errors.only-player"));
            }
        } else if (args.length >= 2) {
            if (sender.hasPermission("castlefight.gamemode.others")) {
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null) {
                    errorUtil.error(null, fileUtil.loadFile("messages.yml").getString("castlefight.errors.player-not-found"));
                } else {
                    gameModeUtil.SetMode(args[1], player);
                }
            } else {
                if (sender instanceof Player) {
                    gameModeUtil.SetMode(args[0], (Player) sender);
                } else {
                    errorUtil.warning(null, messagesUtil.messageString("castlefight.errors.only-player"));
                }
            }
        } else {
            if (sender instanceof Player) {
                errorUtil.error((Player) sender, fileUtil.loadFile("messages.yml").getString("castlefight.errors.invalid-args"));
            } else {
                errorUtil.error(null, fileUtil.loadFile("messages.yml").getString("castlefight.errors.invalid-args"));
            }
        }
        return true;
    }
}
