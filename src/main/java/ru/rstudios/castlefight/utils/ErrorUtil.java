package ru.rstudios.castlefight.utils;

import org.bukkit.entity.Player;

import static ru.rstudios.castlefight.CastleFight.plugin;

public class ErrorUtil {
    public static void warning (Player player, String error) {
        if (player != null) {
            player.sendMessage(MessagesUtil.messageString("castlefight.errors.warning").replace("%error%", error));
        }
        plugin.getLogger().warning(MessagesUtil.messageString("castlefight.errors.warning").replace("%error%", error));
    }

    public static void error (Player player, String error) {
        if (player != null) {
            player.sendMessage(MessagesUtil.messageString("castlefight.errors.error").replace("%error%", error));
        }
        plugin.getLogger().severe(MessagesUtil.messageString("castlefight.errors.error").replace("%error%", error));
    }

    public static void criterror (Player player, String error) {
        if (player != null) {
            player.sendMessage(MessagesUtil.messageString("castlefight.errors.criterror").replace("%error%", error));
        }
        plugin.getLogger().severe(MessagesUtil.messageString("castlefight.errors.criterror").replace("%error%", error));
    }

    public static void warningfromconfig (Player player, String error) {
        if (player != null) {
            player.sendMessage(MessagesUtil.messageString("castlefight.errors.warning").replace("%error%", MessagesUtil.messageString(error)));
        }
        plugin.getLogger().warning(MessagesUtil.messageString("castlefight.errors.warning").replace("%error%", MessagesUtil.messageString(error)));
    }

    public static void errorfromconfig (Player player, String error) {
        if (player != null) {
            player.sendMessage(MessagesUtil.messageString("castlefight.errors.error").replace("%error%", MessagesUtil.messageString(error)));
        }
        plugin.getLogger().severe(MessagesUtil.messageString("castlefight.errors.error").replace("%error%", MessagesUtil.messageString(error)));
    }

    public static void criterrorfromconfig (Player player, String error) {
        if (player != null) {
            player.sendMessage(MessagesUtil.messageString("castlefight.errors.criterror").replace("%error%", MessagesUtil.messageString(error)));
        }
        plugin.getLogger().severe(MessagesUtil.messageString("castlefight.errors.criterror").replace("%error%", MessagesUtil.messageString(error)));
    }
}
