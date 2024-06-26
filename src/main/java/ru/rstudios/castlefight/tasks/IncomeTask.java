package ru.rstudios.castlefight.tasks;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.rstudios.castlefight.modules.ClickActions;
import ru.rstudios.castlefight.modules.GameInfo;
import ru.rstudios.castlefight.modules.PlayerInfo;
import ru.rstudios.castlefight.utils.MessagesUtil;

import static ru.rstudios.castlefight.CastleFight.plugin;

public class IncomeTask implements Runnable {

    private final String playerName;
    private int incomeTimer;

    public IncomeTask(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void run() {
        // Вся обработка в асинхронке
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            PlayerInfo playerInfo = new PlayerInfo(playerName);
            if (playerInfo.getGameID() == -1) {
                return;
            }

            incomeTimer++;

            if (incomeTimer >= 10) {
                incomeTimer = 0;
                GameInfo gameInfo = new GameInfo(playerInfo.getGameID());

                int playerIncome = gameInfo.getPlayerIncome(playerName);

                // Мейн для взаимодействия с игроком
                Bukkit.getScheduler().runTask(plugin, () -> {
                    Player player = Bukkit.getPlayerExact(playerName);
                    if (player != null) {
                        player.sendActionBar(Component.text(MessagesUtil.messageString("castlefight.income.main", playerName)));
                    }

                    // Обратно в асинк ибо больше взаимодействия не будет
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, new ClickActionsHandlerTask(playerName, ClickActions.ADD_GAME_MONEY, String.valueOf(playerIncome)));
                });
            }
        });
    }
}
