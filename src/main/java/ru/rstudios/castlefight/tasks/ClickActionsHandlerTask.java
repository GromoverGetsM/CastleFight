package ru.rstudios.castlefight.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.rstudios.castlefight.modules.ClickActions;
import ru.rstudios.castlefight.modules.GameInfo;
import ru.rstudios.castlefight.modules.PlayerInfo;
import ru.rstudios.castlefight.utils.ErrorUtil;

import java.io.IOException;

import static ru.rstudios.castlefight.CastleFight.plugin;

public class ClickActionsHandlerTask implements Runnable {

    private final String playerName;
    private final ClickActions clickActions;
    private final String executableParts;

    public ClickActionsHandlerTask (String playerName, ClickActions clickActions, String executableParts) {
        this.playerName = playerName;
        this.clickActions = clickActions;
        this.executableParts = executableParts;
    }

    @Override
    public void run() {
        // Плеер/гейм инфо в асинке, действия с игроком в мейне
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            PlayerInfo playerInfo = new PlayerInfo(playerName);
            GameInfo gameInfo;
            if (playerInfo.getGameID() != -1) gameInfo = new GameInfo(playerInfo.getGameID()); else gameInfo = null;

            Bukkit.getScheduler().runTask(plugin, () -> {
                Player player = Bukkit.getPlayerExact(playerName);
                if (player != null && player.isOnline()) {
                    ClickActionsHandler (player, playerInfo, gameInfo);
                }
            });
        });
    }

    private void ClickActionsHandler (Player player, PlayerInfo playerInfo, GameInfo gameInfo) {
        switch (this.clickActions) {
            case CLOSE:
                player.closeInventory();
                break;
            case PLAYER:
                Bukkit.dispatchCommand(player, executableParts.replace("%player%", playerName));
                break;
            case CONSOLE:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), executableParts.replace("%player%", playerName));
                break;
            case SEND_MESSAGE:
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', executableParts));
                break;
            case SET_ROLE:
                if (gameInfo != null) {
                    try {
                        gameInfo.setPlayerActiveRole(playerName, executableParts);
                    } catch (IOException e) {
                        ErrorUtil.error(null, e.getLocalizedMessage());
                    }
                }
                break;
            case SET_TEAM:
                if (gameInfo != null) {
                    try {
                        gameInfo.setPlayerTeam(playerName, executableParts);
                    } catch (IOException e) {
                        ErrorUtil.error(null, e.getLocalizedMessage());
                    }
                }
                break;
            case ADD_MONEY:
                try {
                    playerInfo.setMoney(playerName, playerInfo.getMoney() + Integer.parseInt(executableParts));
                } catch (IOException e) {
                    ErrorUtil.error(null, e.getLocalizedMessage());
                }
                break;
            case TAKE_MONEY:
                try {
                    playerInfo.setMoney(playerName, playerInfo.getMoney() - Integer.parseInt(executableParts));
                } catch (IOException e) {
                    ErrorUtil.error(null, e.getLocalizedMessage());
                }
                break;
            case SET_MONEY:
                try {
                    playerInfo.setMoney(playerName, Integer.parseInt(executableParts));
                } catch (IOException e) {
                    ErrorUtil.error(null, e.getLocalizedMessage());
                }
                break;
            case ADD_GAME_MONEY:
                try {
                    gameInfo.setPlayerBalance(playerName, gameInfo.getPlayerBalance(playerName) + Integer.parseInt(executableParts));
                } catch (IOException e) {
                    ErrorUtil.error(null, e.getLocalizedMessage());
                }
                break;
            case TAKE_GAME_MONEY:
                try {
                    gameInfo.setPlayerBalance(playerName, gameInfo.getPlayerBalance(playerName) - Integer.parseInt(executableParts));
                } catch (IOException e) {
                    ErrorUtil.error(null, e.getLocalizedMessage());
                }
                break;
            case SET_GAME_MONEY:
                try {
                    gameInfo.setPlayerBalance(playerName, Integer.parseInt(executableParts));
                } catch (IOException e) {
                    ErrorUtil.error(null, e.getLocalizedMessage());
                }
                break;
            case ADD_INCOME:
                try {
                    gameInfo.setPlayerIncome(playerName, gameInfo.getPlayerIncome(playerName) + Integer.parseInt(executableParts));
                } catch (IOException e) {
                    ErrorUtil.error(null, e.getLocalizedMessage());
                }
                break;
            case TAKE_INCOME:
                try {
                    gameInfo.setPlayerIncome(playerName, gameInfo.getPlayerIncome(playerName) - Integer.parseInt(executableParts));
                } catch (IOException e) {
                    ErrorUtil.error(null, e.getLocalizedMessage());
                }
                break;
            case SET_INCOME:
                try {
                    gameInfo.setPlayerIncome(playerName, Integer.parseInt(executableParts));
                } catch (IOException e) {
                    ErrorUtil.error(null, e.getLocalizedMessage());
                }
                break;
            case ADD_TOWERS_LIMIT:
                try {
                    gameInfo.setPlayerTowerLimit(playerName, gameInfo.getPlayerTowerLimit(playerName) + Integer.parseInt(executableParts));
                } catch (IOException e) {
                    ErrorUtil.error(null, e.getLocalizedMessage());
                }
                break;
            case TAKE_TOWERS_LIMIT:
                try {
                    gameInfo.setPlayerTowerLimit(playerName, gameInfo.getPlayerTowerLimit(playerName) - Integer.parseInt(executableParts));
                } catch (IOException e) {
                    ErrorUtil.error(null, e.getLocalizedMessage());
                }
                break;
            case SET_TOWERS_LIMIT:
                try {
                    gameInfo.setPlayerTowerLimit(playerName, Integer.parseInt(executableParts));
                } catch (IOException e) {
                    ErrorUtil.error(null, e.getLocalizedMessage());
                }
                break;
        }
    }
}
