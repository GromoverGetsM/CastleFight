package ru.rstudios.castlefight.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.rstudios.castlefight.utils.ErrorUtil;
import ru.rstudios.castlefight.utils.MessagesUtil;
import ru.rstudios.castlefight.utils.TowerUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.rstudios.castlefight.CastleFight.plugin;

public class structureCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 4) {
                File mainFolder = new File(plugin.getDataFolder(), "roles");
                File roleFolder = new File(mainFolder, args[1]);
                File towerFolder = new File(roleFolder, args[2]);
                File levelFile = new File(towerFolder, args[3] + ".yml");

                if (levelFile.exists()) {
                    switch (args[0]) {
                        case "save":
                            Block lookup = ((Player) sender).getTargetBlockExact(5);
                            if (lookup != null && lookup.getType().getKey() != Material.AIR.getKey()) {
                                Location loc = lookup.getLocation();
                                try {
                                    TowerUtil.saveStructure(args[1], args[2], Integer.parseInt(args[3]), loc);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                String message = MessagesUtil.messageString("castlefight.commands.structure.saved");
                                sender.sendMessage(message.replace("%level%", args[3]).replace("%tower%", args[2]).replace("%role%", args[1]));
                            }
                            break;
                        case "load":
                            Block lookup2 = ((Player) sender).getTargetBlockExact(5);
                            if (lookup2 != null && lookup2.getType().getKey() != Material.AIR.getKey()) {
                                Location loc = lookup2.getLocation();
                                TowerUtil.loadStructure(args[1], args[2], Integer.parseInt(args[3]), loc);
                                String message = MessagesUtil.messageString("castlefight.commands.structure.loading");
                                sender.sendMessage(message.replace("%level%", args[3]).replace("%tower%", args[2]).replace("%role%", args[1]));
                            }
                            break;
                        default:
                            break;
                    }
                }

            } else {
                player.sendMessage(MessagesUtil.messageString("castlefight.commands.structure.usage"));
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
                pArgs.add("save");
                pArgs.add("load");
                return pArgs;
            case 2:
                File roles = new File(plugin.getDataFolder(), "roles");
                for (File file : roles.listFiles()) {
                    pArgs.add(file.getName());
                }
                return pArgs;
            case 3:
                File towers = new File(new File(plugin.getDataFolder(), "roles"), args[1]);
                for (File towerFile : towers.listFiles()) {
                    pArgs.add(towerFile.getName());
                }
                return pArgs;
            case 4:
                File levels = new File(new File(new File(plugin.getDataFolder(), "roles"), args[1]), args[2]);
                for (File levelFile : levels.listFiles()) {
                    if (levelFile.getName().contains(".yml")) {
                        pArgs.add(levelFile.getName().replace(".yml", ""));
                    }
                }
                return pArgs;
            default:
                return pArgs;
        }
    }
}
