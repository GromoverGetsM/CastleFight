package ru.rstudios.castlefight.tasks;

import org.bukkit.Bukkit;
import ru.rstudios.castlefight.modules.ClickActions;
import ru.rstudios.castlefight.modules.GameInfo;
import ru.rstudios.castlefight.modules.PlayerInfo;

import static ru.rstudios.castlefight.CastleFight.plugin;

public class IncomeTask implements Runnable {



    private String playerName;
    private int income;
    private int incomeTimer;

    public IncomeTask (String playerName) {
        this.playerName = playerName;

        PlayerInfo playerInfo = new PlayerInfo(playerName);
        GameInfo gameInfo;
        int income;

        if (playerInfo.getGameID() != -1) {
            gameInfo = new GameInfo(playerInfo.getGameID());
            gameInfo.updateGameInfo(playerInfo.getGameID());
            income = gameInfo.getPlayerIncome(playerName);
            this.income = income;
        }
    }

    @Override
    public void run() {
        PlayerInfo playerInfo = new PlayerInfo(playerName);
        if (playerInfo.getGameID() == -1) {
            return;
        }

        incomeTimer++;

        if (incomeTimer >= 10) {
            incomeTimer = 0;
            GameInfo gameInfo = new GameInfo(playerInfo.getGameID());

            Bukkit.getScheduler().runTask(plugin, new ClickActionsHandlerTask(playerName, ClickActions.ADD_GAME_MONEY, String.valueOf(gameInfo.getPlayerIncome(playerName))));
        }
    }
}
