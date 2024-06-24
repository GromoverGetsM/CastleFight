package ru.rstudios.castlefight.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

import static ru.rstudios.castlefight.CastleFight.*;

public class spawnCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 7) {
                Location spawn = player.getLocation();
                Location enemyBase = new Location(((Player) sender).getWorld(), Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));
                unitCreator.createUnit(player.getName(), args[0], args[1], Integer.parseInt(args[2]), args[3], enemyBase, spawn);
            } else {
                player.sendMessage(messagesUtil.messageString("castlefight.commands.spawn.usage"));
            }
        } else {
            errorUtil.errorfromconfig(null, "castlefight.errors.only-player");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        List<String> pArgs = new ArrayList<>();
        switch (args.length) {
            case 1:
                File roles = new File(plugin.getDataFolder(), "roles");
                for (File file : roles.listFiles()) {
                    pArgs.add(file.getName());
                }
                return pArgs;
            case 2:
                File towers = new File(new File(plugin.getDataFolder(), "roles"), args[0]);
                for (File towerFile : towers.listFiles()) {
                    pArgs.add(towerFile.getName());
                }
                return pArgs;
            case 3:
                File levels = new File(new File(new File(plugin.getDataFolder(), "roles"), args[0]), args[1]);
                for (File levelFile : levels.listFiles()) {
                    if (levelFile.getName().contains(".yml")) {
                        pArgs.add(levelFile.getName().replace(".yml", ""));
                    }
                }
                return pArgs;
            case 4:
                pArgs.add("blue");
                pArgs.add("red");
                return pArgs;
            case 5:
                pArgs.add(String.valueOf(((Player) sender).getLocation().getBlockX()));
                return pArgs;
            case 6:
                pArgs.add(String.valueOf(((Player) sender).getLocation().getBlockY()));
                return pArgs;
            case 7:
                pArgs.add(String.valueOf(((Player) sender).getLocation().getBlockZ()));
                return pArgs;
            default:
                return pArgs;
        }
    }

    private String getColorCode(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        return name.substring(0, 2);
    }

    private boolean hasSameColorCode(LivingEntity entity, String colorCode) {
        String entityName = entity.getCustomName();
        return entityName != null && entityName.startsWith(colorCode);
    }
}