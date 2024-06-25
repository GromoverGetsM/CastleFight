package ru.rstudios.castlefight.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static ru.rstudios.castlefight.CastleFight.*;

public class CountDamageUtil {

    public static Double getUnitDamage (Entity damager, Entity victim, Double defaultDmg) {
        NamespacedKey attackType = new NamespacedKey(plugin, "AttackType");
        NamespacedKey protectionType = new NamespacedKey(plugin, "ProtectionType");
        PersistentDataContainer damagerContainer = damager.getPersistentDataContainer();
        PersistentDataContainer victimContainer = victim.getPersistentDataContainer();

        String damagerDmgType = damagerContainer.get(attackType, PersistentDataType.STRING);
        String victimProtectionType = victimContainer.get(protectionType, PersistentDataType.STRING);

        return switch (damagerDmgType) {
            case "Режущий" -> switch (victimProtectionType) {
                case "Нет" -> Math.round((defaultDmg * 1.05) * 100) / 100.0;
                case "Лёгкий" -> Math.round((defaultDmg * 0.6) * 100) / 100.0;
                case "Средний" -> Math.round((defaultDmg * 1.75) * 100) / 100.0;
                case "Осадный" -> Math.round((defaultDmg * 0.5) * 100) / 100.0;
                default -> Math.round(defaultDmg * 100) / 100.0;
            };
            case "Колющий" -> switch (victimProtectionType) {
                case "Нет" -> Math.round((defaultDmg * 1.05) * 100) / 100.0;
                case "Лёгкий" -> Math.round((defaultDmg * 1.75) * 100) / 100.0;
                case "Тяжёлый" -> Math.round((defaultDmg * 0.6) * 100) / 100.0;
                case "Осадный" -> Math.round((defaultDmg * 0.45) * 100) / 100.0;
                default -> Math.round(defaultDmg * 100) / 100.0;
            };
            case "Магический" -> switch (victimProtectionType) {
                case "Нет" -> Math.round((defaultDmg * 1.05) * 100) / 100.0;
                case "Средний" -> Math.round((defaultDmg * 0.6) * 100) / 100.0;
                case "Тяжёлый" -> Math.round((defaultDmg * 1.75) * 100) / 100.0;
                case "Осадный" -> Math.round((defaultDmg * 0.4) * 100) / 100.0;
                default -> Math.round(defaultDmg * 100) / 100.0;
            };
            case "Осадный" -> switch (victimProtectionType) {
                case "Нет", "Лёгкий", "Средний", "Тяжёлый" -> Math.round((defaultDmg * 0.6) * 100) / 100.0;
                case "Осадный" -> Math.round((defaultDmg * 1.6) * 100) / 100.0;
                default -> Math.round(defaultDmg * 100) / 100.0;
            };
            default -> Math.round(defaultDmg * 100) / 100.0;
        };
    }
}
