package ru.rstudios.castlefight.modules;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ClickActions {
    CONSOLE("[console]", "Использовать команду от имени консоли", "- '[console] Команда'"),
    PLAYER("[player]", "Использовать команду от имени игрока", "- '[player] Команда'"),
    SET_ROLE("[setRole]", "Устанавливает кликнувшему указанную роль", "- '[setRole] Роль (англ.)'"),
    SET_TEAM("[setTeam]", "Устанавливает кликнувшему указанную команду", "- '[setTeam] blue/red'"),
    SEND_MESSAGE("[sendMessage]", "Отправляет игроку указанное сообщение", "- '[sendMessage] Соообщение'"),
    CLOSE("[close]", "Закрывает кликнувшему текущий открытый инвентарь", "- '[close]'"),
    ADD_MONEY("[addMoneyHub]", "Добавляет игроку деньги на баланс хаба", "- '[addMoneyHub] Количество'"),
    TAKE_MONEY("[takeMoneyHub]", "Забирает у игрока деньги с баланса хаба", "- '[takeMoneyHub] Количество'"),
    ADD_GAME_MONEY("[addMoneyGame]", "Добавляет игроку деньги на баланс внутри игры", "- '[addMoneyGame] Количество'"),
    TAKE_GAME_MONEY("[takeMoneyGame]", "Забирает у игрока деньги с внутриигрового баланса", "- '[takeMoneyGame] Количество'"),
    SET_MONEY("[setMoneyHub]", "Устанавливает игроку баланс хаба", "- '[setMoneyHub] Количество'"),
    SET_GAME_MONEY("[setMoneyGame]", "Устанавливает игроку внутриигровой баланс", "- '[setMoneyGame] Количество'"),
    ADD_INCOME("[addIncome]", "Добавляет игроку внутриигровой доход", "- '[addIncome] Количество'"),
    TAKE_INCOME("[takeIncome]", "Забирает у игрока внутриигровой доход", "- '[takeIncome] Количество'"),
    SET_INCOME("[setIncome]", "Устанавливает игроку внутриигровой доход", "- '[setIncome] Количество'"),
    ADD_TOWERS_LIMIT("[addTowersLimit]", "Добавляет игроку лимит башен", "- '[addTowersLimit] Количество'"),
    TAKE_TOWERS_LIMIT("[takeTowersLimit]", "Забирает у игрока лимит башен", "- '[takeTowersLimit] Количество'"),
    SET_TOWERS_LIMIT("[setTowersLimit]", "Устанавливает игроку лимит башен", "- '[setTowersLimit] Количество'");


    private final String id;
    private final String description;
    private final String usage;

    private static final Map<String, ClickActions> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap((e) -> e.name().toUpperCase(Locale.ROOT), Function.identity()));

    ClickActions(@NotNull String id, String description, String usage) {
        this.id = id;
        this.description = description;
        this.usage = usage;
    }

    public static @Nullable ClickActions getByName(@NotNull String name) {
        return BY_NAME.get(name.toUpperCase(Locale.ROOT));
    }

    public static @Nullable ClickActions getById(@NotNull String identifier) {
        return BY_NAME.values().stream().filter((e) -> e.id.equalsIgnoreCase(identifier)).findFirst().orElse(null);
    }

    public static @Nullable ClickActions getByStart(@NotNull String string) {
        Stream<String> stream = BY_NAME.values().stream().map(ClickActions::getId);
        Objects.requireNonNull(string);
        return stream.filter(string::startsWith).findFirst().map(ClickActions::getById).orElse(null);
    }

    public static String listAllActionTypes() {
        StringBuilder builder = new StringBuilder();

        for (ClickActions type : BY_NAME.values()) {
            builder.append("\n").append(type.getId()).append(" - ").append(type.getDescription()).append("\n").append("Использование: ").append(type.getUsage()).append("\n");
        }

        return builder.toString();
    }

    public @NotNull String getId() {
        return this.id;
    }

    public @NotNull String getDescription() {
        return this.description;
    }

    public @NotNull String getUsage() {
        return this.usage;
    }

    public String getExecutableParts (String action) {
        if (action.startsWith(this.id)) {
            return action.substring(this.id.length()).trim();
        }
        return null;
    }
}
