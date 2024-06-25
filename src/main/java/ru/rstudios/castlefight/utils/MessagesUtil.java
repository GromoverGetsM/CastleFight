package ru.rstudios.castlefight.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

import static ru.rstudios.castlefight.CastleFight.*;

public class MessagesUtil {
    public static String messageString (String messagePath) {
        String message = FileUtil.loadFile("messages.yml").getString(messagePath);
        if (message != null) {
            return ChatColor.translateAlternateColorCodes('&', message);
        } else {
            ErrorUtil.error(null, FileUtil.loadFile("messages.yml").getString("castlefight.errors.message-not-found").replace("%message%", messagePath));
            return ChatColor.translateAlternateColorCodes('&', FileUtil.loadFile("messages.yml").getString("castlefight.errors.message-not-found").replace("%message%", messagePath));
        }
    }

    public static String messageString (String messagePath, String playerName) {
        String message = FileUtil.loadFile("messages.yml").getString(messagePath);
        message = PlaceholderUtil.replacePlaceholders(playerName, message);
        if (message != null) {
            return ChatColor.translateAlternateColorCodes('&', message);
        } else {
            ErrorUtil.error(null, FileUtil.loadFile("messages.yml").getString("castlefight.errors.message-not-found").replace("%message%", messagePath));
            return ChatColor.translateAlternateColorCodes('&', FileUtil.loadFile("messages.yml").getString("castlefight.errors.message-not-found").replace("%message%", messagePath));
        }
    }
    public static String messageString (String messagePath, int gameID) {
        String message = FileUtil.loadFile("messages.yml").getString(messagePath);
        message = PlaceholderUtil.replacePlaceholders(gameID, message);
        if (message != null) {
            return ChatColor.translateAlternateColorCodes('&', message);
        } else {
            ErrorUtil.error(null, FileUtil.loadFile("messages.yml").getString("castlefight.errors.message-not-found").replace("%message%", messagePath));
            return ChatColor.translateAlternateColorCodes('&', FileUtil.loadFile("messages.yml").getString("castlefight.errors.message-not-found").replace("%message%", messagePath));
        }
    }

    public static List<String> messageList (String messageListPath) {
        List<String> messageList = new ArrayList<>();
        for (String messageListPart : FileUtil.loadFile("messages.yml").getStringList(messageListPath)) {
            messageList.add(ChatColor.translateAlternateColorCodes('&', messageListPart));
        }
        return messageList;
    }
}
