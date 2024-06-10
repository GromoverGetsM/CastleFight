package ru.rstudios.castlefight.utils;

import org.bukkit.entity.Player;

import static ru.rstudios.castlefight.CastleFight.messagesUtil;
import static ru.rstudios.castlefight.CastleFight.plugin;

public class ErrorUtil {
    public void warning (Player player, String error) {
        if (player != null) {
            player.sendMessage(messagesUtil.messageString("castlefight.errors.warning").replace("%error%", error));
        }
        plugin.getLogger().warning(messagesUtil.messageString("castlefight.errors.warning").replace("%error%", error));
    }

    public void error (Player player, String error) {
        if (player != null) {
            player.sendMessage(messagesUtil.messageString("castlefight.errors.error").replace("%error%", error));
        }
        plugin.getLogger().severe(messagesUtil.messageString("castlefight.errors.error").replace("%error%", error));
    }

    public void criterror (Player player, String error) {
        if (player != null) {
            player.sendMessage(messagesUtil.messageString("castlefight.errors.criterror").replace("%error%", error));
        }
        plugin.getLogger().severe(messagesUtil.messageString("castlefight.errors.criterror").replace("%error%", error));
    }

    public void warningfromconfig (Player player, String error) {
        if (player != null) {
            player.sendMessage(messagesUtil.messageString("castlefight.errors.warning").replace("%error%", messagesUtil.messageString(error)));
        }
        plugin.getLogger().warning(messagesUtil.messageString("castlefight.errors.warning").replace("%error%", messagesUtil.messageString(error)));
    }

    public void errorfromconfig (Player player, String error) {
        if (player != null) {
            player.sendMessage(messagesUtil.messageString("castlefight.errors.error").replace("%error%", messagesUtil.messageString(error)));
        }
        plugin.getLogger().severe(messagesUtil.messageString("castlefight.errors.error").replace("%error%", messagesUtil.messageString(error)));
    }

    public void criterrorfromconfig (Player player, String error) {
        if (player != null) {
            player.sendMessage(messagesUtil.messageString("castlefight.errors.criterror").replace("%error%", messagesUtil.messageString(error)));
        }
        plugin.getLogger().severe(messagesUtil.messageString("castlefight.errors.criterror").replace("%error%", messagesUtil.messageString(error)));
    }
}
