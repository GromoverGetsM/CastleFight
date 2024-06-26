package ru.rstudios.castlefight.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.rstudios.castlefight.utils.ErrorUtil;
import ru.rstudios.castlefight.utils.FileUtil;
import ru.rstudios.castlefight.utils.InventoryUtil;
import ru.rstudios.castlefight.utils.MessagesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class openMenuCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            switch (args.length) {
                case 1:
                    ((Player) sender).openInventory(InventoryUtil.inventoryFromConfig(args[0], (Player) sender));
                    break;
                case 2:
                    Player player = Bukkit.getPlayer(args[0]);
                    if (player != null && player.isOnline()) {
                        player.openInventory(InventoryUtil.inventoryFromConfig(args[1], player));
                    } else {
                        sender.sendMessage(MessagesUtil.messageString("castlefight.errors.player-not-found"));
                    }
                    break;
                default:
                    sender.sendMessage(MessagesUtil.messageString("castlefight.errors.invalid-args"));
                    break;
            }
        } else {
            ErrorUtil.errorfromconfig(null, "castlefight.errors.only-player");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        List<String> pArgs = new ArrayList<>();
        switch (args.length) {
            case 1:
                for (Player player : Bukkit.getOnlinePlayers()) {
                    pArgs.add(player.getName());
                }
                break;
            case 2:
                Map<String, Object> menus = FileUtil.loadFile("messages.yml").getConfigurationSection("castlefight.menus").getValues(false);
                if (!menus.isEmpty()) {
                    pArgs.addAll(menus.keySet());
                }
                break;
        }
        return pArgs;
    }
}
