package ru.rstudios.castlefight.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.rstudios.castlefight.utils.ErrorUtil;
import ru.rstudios.castlefight.utils.GameModeUtil;

import static ru.rstudios.castlefight.CastleFight.*;

public class gameModeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        switch (args.length) {
            case 1:
                if (sender instanceof Player) {
                    GameModeUtil.SetMode(args[0], (Player) sender);
                } else {
                    ErrorUtil.errorfromconfig(null, "castlefight.commands.gamemode.no-permission");
                }
                break;

            case 2:
                if (sender.hasPermission("castlefight.gamemode.others")) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null) {
                        if (sender instanceof Player) {
                            ErrorUtil.errorfromconfig((Player) sender, "castlefight.errors.player-not-found");
                        } else {
                            ErrorUtil.errorfromconfig(null, "castlefight.errors.player-not-found");
                        }
                    } else {
                        GameModeUtil.SetMode(args[0], player);
                    }
                } else {
                    if (sender instanceof Player) {
                        GameModeUtil.SetMode(args[0], (Player) sender);
                    } else {
                        ErrorUtil.warningfromconfig(null, "castlefight.errors.only-player");
                    }
                }
                break;

            default:
                if (sender instanceof Player) {
                    ErrorUtil.errorfromconfig((Player) sender, "castlefight.errors.invalid-args");
                } else {
                    ErrorUtil.errorfromconfig(null, "castlefight.errors.invalid-args");
                }
        }
        return true;
    }
}
