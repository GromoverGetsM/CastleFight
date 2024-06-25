package ru.rstudios.castlefight.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

import static ru.rstudios.castlefight.CastleFight.*;

public class MessagesUtil {
    public String messageString (String messagePath) {
        String message = fileUtil.loadFile("messages.yml").getString(messagePath);
        if (message != null) {
            return ChatColor.translateAlternateColorCodes('&', message);
        } else {
            errorUtil.error(null, fileUtil.loadFile("messages.yml").getString("castlefight.errors.message-not-found").replace("%message%", messagePath));
            return ChatColor.translateAlternateColorCodes('&', fileUtil.loadFile("messages.yml").getString("castlefight.errors.message-not-found").replace("%message%", messagePath));
        }
    }

    public String messageString (String messagePath, String playerName) {
        String message = fileUtil.loadFile("messages.yml").getString(messagePath);
        message = placeholderUtil.replacePlaceholders(playerName, message);
        if (message != null) {
            return ChatColor.translateAlternateColorCodes('&', message);
        } else {
            errorUtil.error(null, fileUtil.loadFile("messages.yml").getString("castlefight.errors.message-not-found").replace("%message%", messagePath));
            return ChatColor.translateAlternateColorCodes('&', fileUtil.loadFile("messages.yml").getString("castlefight.errors.message-not-found").replace("%message%", messagePath));
        }
    }
    public String messageString (String messagePath, int gameID) {
        String message = fileUtil.loadFile("messages.yml").getString(messagePath);
        message = placeholderUtil.replacePlaceholders(gameID, message);
        if (message != null) {
            return ChatColor.translateAlternateColorCodes('&', message);
        } else {
            errorUtil.error(null, fileUtil.loadFile("messages.yml").getString("castlefight.errors.message-not-found").replace("%message%", messagePath));
            return ChatColor.translateAlternateColorCodes('&', fileUtil.loadFile("messages.yml").getString("castlefight.errors.message-not-found").replace("%message%", messagePath));
        }
    }

    public List<String> messageList (String messageListPath) {
        List<String> messageList = new ArrayList<>();
        for (String messageListPart : fileUtil.loadFile("messages.yml").getStringList(messageListPath)) {
            messageList.add(ChatColor.translateAlternateColorCodes('&', messageListPart));
        }
        return messageList;
    }
}
