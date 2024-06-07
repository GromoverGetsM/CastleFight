package ru.rstudios.castlefight.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

import static ru.rstudios.castlefight.CastleFight.fileUtil;

public class MessagesUtil {
    public String messageString (String messagePath) {
        return fileUtil.loadFile("messages.yml").getString(messagePath);
    }

    public List<String> messageList (String messageListPath) {
        List<String> messageList = new ArrayList<>();
        for (String messageListPart : fileUtil.loadFile("messages.yml").getStringList(messageListPath)) {
            messageList.add(ChatColor.translateAlternateColorCodes('&', messageListPart));
        }
        return messageList;
    }
}
