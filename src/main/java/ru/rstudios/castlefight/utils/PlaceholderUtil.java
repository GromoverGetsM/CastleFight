package ru.rstudios.castlefight.utils;

import ru.rstudios.castlefight.modules.GameInfo;
import ru.rstudios.castlefight.modules.PlayerInfo;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderUtil {

    public static String replacePlaceholders(String playerName, String text) {
        PlayerInfo playerInfo = new PlayerInfo(playerName);
        GameInfo gameInfo = null;
        List<String> placeholders = new ArrayList<>();

        if (playerInfo.getGameID() != -1) {
            gameInfo = new GameInfo(playerInfo.getGameID());

            if (text.contains("%income%")) placeholders.add("%income%");
            if (text.contains("%expectedPlayers%")) placeholders.add("%expectedPlayers%");
            if (text.contains("%expectedPerTeamTowers%")) placeholders.add("%expectedPerTeamTowers%");
            if (text.contains("%baseHealth%")) placeholders.add("%baseHealth%");
            if (text.contains("%blueHealth%")) placeholders.add("%blueHealth%");
            if (text.contains("%redHealth%")) placeholders.add("%redHealth%");
            if (text.contains("%activeRole%")) placeholders.add("%activeRole%");
            if (text.contains("%ingameMoney%")) placeholders.add("%ingameMoney%");
            if (text.contains("%towerLimit%")) placeholders.add("%towerLimit%");
            if (text.contains("%team%")) placeholders.add("%team%");
        }

        if (text.contains("%rating%")) placeholders.add("%rating%");
        if (text.contains("%money%")) placeholders.add("%money%");
        if (text.contains("%gameID%")) placeholders.add("%gameID%");
        if (text.contains("%player%")) placeholders.add("%player%");
        if (text.contains("%lastJoinTime%")) placeholders.add("%lastJoinTime%");
        if (text.contains("%lastGameTime%")) placeholders.add("%lastGameTime%");
        if (text.contains("%lastWorld%")) placeholders.add("%lastWorld%");
        if (text.contains("%nowWorld%")) placeholders.add("%nowWorld%");

        for (String placeholder : placeholders) {
            switch (placeholder) {
                case "%rating%":
                    text = text.replace("%rating%", String.valueOf(playerInfo.getRating()));
                    break;
                case "%money%":
                    text = text.replace("%money%", String.valueOf(playerInfo.getMoney()));
                    break;
                case "%gameID%":
                    text = text.replace("%gameID%", String.valueOf(playerInfo.getGameID()));
                    break;
                case "%player%":
                    text = text.replace("%player%", playerName);
                    break;
                case "%lastJoinTime%":
                    text = text.replace("%lastJoinTime%", String.valueOf(playerInfo.getLastJoinTime()));
                    break;
                case "%lastGameTime%":
                    text = text.replace("%lastGameTime%", String.valueOf(playerInfo.getLastGameTime()));
                    break;
                case "%lastWorld%":
                    text = text.replace("%lastWorld%", String.valueOf(playerInfo.getLastWorld()));
                    break;
                case "%nowWorld%":
                    text = text.replace("%nowWorld%", String.valueOf(playerInfo.getNowWorld()));

                case "%income%":
                    if (playerInfo.getGameID() != -1) {
                        text = text.replace("%income%", String.valueOf(gameInfo.getPlayerIncome(playerName)));
                    }
                    break;
                case "%expectedPlayers%":
                    if (playerInfo.getGameID() != -1) {
                        text = text.replace("%expectedPlayers%", String.valueOf(gameInfo.getExpectedPlayers()));
                    }
                    break;
                case "%expectedPerTeamTowers%":
                    if (playerInfo.getGameID() != -1) {
                        text = text.replace("%expectedPerTeamTowers%", String.valueOf(gameInfo.getExpectedPerTeamTowers()));
                    }
                    break;
                case "%baseHealth%":
                    if (playerInfo.getGameID() != -1) {
                        text = text.replace("%baseHealth%", String.valueOf(gameInfo.getBaseHealth()));
                    }
                    break;
                case "%blueHealth%":
                    if (playerInfo.getGameID() != -1) {
                        text = text.replace("%blueHealth%", String.valueOf(gameInfo.getBlueHealth()));
                    }
                    break;
                case "%redHealth%":
                    if (playerInfo.getGameID() != -1) {
                        text = text.replace("%redHealth%", String.valueOf(gameInfo.getRedHealth()));
                    }
                    break;
                case "%activeRole%":
                    if (playerInfo.getGameID() != -1) {
                        text = text.replace("%activeRole%", String.valueOf(gameInfo.getPlayerActiveRole(playerName)));
                    }
                    break;
                case "%ingameMoney%":
                    if (playerInfo.getGameID() != -1) {
                        text = text.replace("%ingameMoney%", String.valueOf(gameInfo.getPlayerBalance(playerName)));
                    }
                    break;
                case "%towerLimit%":
                    if (playerInfo.getGameID() != -1) {
                        text = text.replace("%towerLimit%", String.valueOf(gameInfo.getPlayerTowerLimit(playerName)));
                    }
                    break;
                case "%team%":
                    if (playerInfo.getGameID() != -1) {
                        text = text.replace("%team%", String.valueOf(gameInfo.getPlayerTeam(playerName)));
                    }
                    break;
                default:
                    break;
            }
        }

        return text;
    }

    public static String replacePlaceholders(int gameID, String text) {
        GameInfo gameInfo = null;
        List<String> placeholders = new ArrayList<>();

        if (gameID != -1) {
            gameInfo = new GameInfo(gameID);

            if (text.contains("%income%")) placeholders.add("%income%");
            if (text.contains("%expectedPlayers%")) placeholders.add("%expectedPlayers%");
            if (text.contains("%expectedPerTeamTowers%")) placeholders.add("%expectedPerTeamTowers%");
            if (text.contains("%baseHealth%")) placeholders.add("%baseHealth%");
            if (text.contains("%blueHealth%")) placeholders.add("%blueHealth%");
            if (text.contains("%redHealth%")) placeholders.add("%redHealth%");
            if (text.contains("%activeRole%")) placeholders.add("%activeRole%");
            if (text.contains("%ingameMoney%")) placeholders.add("%ingameMoney%");
            if (text.contains("%towerLimit%")) placeholders.add("%towerLimit%");
            if (text.contains("%team%")) placeholders.add("%team%");
        }

        if (text.contains("%rating%")) placeholders.add("%rating%");
        if (text.contains("%money%")) placeholders.add("%money%");
        if (text.contains("%gameID%")) placeholders.add("%gameID%");
        if (text.contains("%player%")) placeholders.add("%player%");
        if (text.contains("%lastJoinTime%")) placeholders.add("%lastJoinTime%");
        if (text.contains("%lastGameTime%")) placeholders.add("%lastGameTime%");
        if (text.contains("%lastWorld%")) placeholders.add("%lastWorld%");
        if (text.contains("%nowWorld%")) placeholders.add("%nowWorld%");

        for (String placeholder : placeholders) {
            switch (placeholder) {

                case "%expectedPlayers%":
                    if (gameID != -1) {
                        text = text.replace("%expectedPlayers%", String.valueOf(gameInfo.getExpectedPlayers()));
                    }
                    break;
                case "%expectedPerTeamTowers%":
                    if (gameID != -1) {
                        text = text.replace("%expectedPerTeamTowers%", String.valueOf(gameInfo.getExpectedPerTeamTowers()));
                    }
                    break;
                case "%baseHealth%":
                    if (gameID != -1) {
                        text = text.replace("%baseHealth%", String.valueOf(gameInfo.getBaseHealth()));
                    }
                    break;
                case "%blueHealth%":
                    if (gameID != -1) {
                        text = text.replace("%blueHealth%", String.valueOf(gameInfo.getBlueHealth()));
                    }
                    break;
                case "%redHealth%":
                    if (gameID != -1) {
                        text = text.replace("%redHealth%", String.valueOf(gameInfo.getRedHealth()));
                    }
                    break;
                default:
                    break;
            }
        }

        return text;
    }
}
