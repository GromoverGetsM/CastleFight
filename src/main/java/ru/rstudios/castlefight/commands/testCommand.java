package ru.rstudios.castlefight.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static ru.rstudios.castlefight.CastleFight.*;

public class testCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        FileConfiguration linked = fileUtil.loadFile("linked.yml");
        plugin.getLogger().info(linked.getString(sender.getName()));
        linked.set(sender.getName(), "1");
        try {
            fileUtil.save("linked.yml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        plugin.getLogger().info(linked.getString(sender.getName()));
        return true;
    }
}
