package ru.rstudios.castlefight.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static ru.rstudios.castlefight.CastleFight.*;

public class CountDamageUtil {

    public Double getUnitDamage (Entity damager, Entity victim, Double defaultDmg) {
        NamespacedKey attackType = new NamespacedKey(plugin, "AttackType");
        NamespacedKey protectionType = new NamespacedKey(plugin, "ProtectionType");
        PersistentDataContainer damagerContainer = damager.getPersistentDataContainer();
        PersistentDataContainer victimContainer = victim.getPersistentDataContainer();

        String damagerDmgType = damagerContainer.get(attackType, PersistentDataType.STRING);
        String victimProtectionType = victimContainer.get(protectionType, PersistentDataType.STRING);

        switch (damagerDmgType) {
            case "Режущий":
                switch (victimProtectionType) {
                    case "Нет":
                        return Math.round((defaultDmg * 1.05) * 100) / 100.0;
                    case "Лёгкий":
                        return Math.round((defaultDmg * 0.6) * 100) / 100.0;
                    case "Средний":
                        return Math.round((defaultDmg * 1.75) * 100) / 100.0;
                    case "Тяжёлый":
                        return Math.round(defaultDmg * 100) / 100.0;
                    case "Осадный":
                        return Math.round((defaultDmg * 0.5) * 100) / 100.0;
                    default:
                        return Math.round(defaultDmg * 100) / 100.0;
                }
            case "Колющий":
                switch (victimProtectionType) {
                    case "Нет":
                        return Math.round((defaultDmg * 1.05) * 100) / 100.0;
                    case "Лёгкий":
                        return Math.round((defaultDmg * 1.75) * 100) / 100.0;
                    case "Средний":
                        return Math.round(defaultDmg * 100) / 100.0;
                    case "Тяжёлый":
                        return Math.round((defaultDmg * 0.6) * 100) / 100.0;
                    case "Осадный":
                        return Math.round((defaultDmg * 0.45) * 100) / 100.0;
                    default:
                        return Math.round(defaultDmg * 100) / 100.0;
                }
            case "Магический":
                switch (victimProtectionType) {
                    case "Нет":
                        return Math.round((defaultDmg * 1.05) * 100) / 100.0;
                    case "Лёгкий":
                        return Math.round(defaultDmg * 100) / 100.0;
                    case "Средний":
                        return Math.round((defaultDmg * 0.6) * 100) / 100.0;
                    case "Тяжёлый":
                        return Math.round((defaultDmg * 1.75) * 100) / 100.0;
                    case "Осадный":
                        return Math.round((defaultDmg * 0.4) * 100) / 100.0;
                    default:
                        return Math.round(defaultDmg * 100) / 100.0;
                }
            case "Осадный":
                switch (victimProtectionType) {
                    case "Нет":
                        return Math.round((defaultDmg * 0.6) * 100) / 100.0;
                    case "Лёгкий":
                        return Math.round((defaultDmg * 0.6) * 100) / 100.0;
                    case "Средний":
                        return Math.round((defaultDmg * 0.6) * 100) / 100.0;
                    case "Тяжёлый":
                        return Math.round((defaultDmg * 0.6) * 100) / 100.0;
                    case "Осадный":
                        return Math.round((defaultDmg * 1.6) * 100) / 100.0;
                    default:
                        return Math.round(defaultDmg * 100) / 100.0;
                }
            case "Хаос":
                return Math.round(defaultDmg * 100) / 100.0;
            default:
                return Math.round(defaultDmg * 100) / 100.0;
        }
    }
}
