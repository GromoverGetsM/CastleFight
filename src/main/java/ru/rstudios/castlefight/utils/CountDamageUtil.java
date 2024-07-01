package ru.rstudios.castlefight.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Comparator;

import static ru.rstudios.castlefight.CastleFight.plugin;

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

    public static LivingEntity getTarget (Entity entity) {
        NamespacedKey range = new NamespacedKey(plugin, "Range");
        NamespacedKey preferTarget = new NamespacedKey(plugin, "PreferedTarget");

        PersistentDataContainer pdc = entity.getPersistentDataContainer();

        int eRange = pdc.get(range, PersistentDataType.INTEGER);
        String preferedTarget = pdc.get(preferTarget, PersistentDataType.STRING);
        LivingEntity target = null;

        switch (preferedTarget) {
            case "Ближайший":
                target = entity.getNearbyEntities(eRange, eRange, eRange).stream()
                        .filter(e -> e instanceof LivingEntity && !(e instanceof Player) && !(e instanceof ArmorStand) && e != entity)
                        .map(e -> (LivingEntity) e)
                        .filter(e -> !hasSameColorCode(e, getColorCode(entity.getCustomName())))
                        .min(Comparator.comparingDouble(e -> e.getLocation().distance(entity.getLocation())))
                        .orElse(null);
                break;
            case "Дальнейший":
                target = entity.getNearbyEntities(eRange, eRange, eRange).stream()
                        .filter(e -> e instanceof LivingEntity && !(e instanceof Player) && !(e instanceof ArmorStand) && e != entity)
                        .map(e -> (LivingEntity) e)
                        .filter(e -> !hasSameColorCode(e, getColorCode(entity.getCustomName())))
                        .max(Comparator.comparingDouble(e -> e.getLocation().distance(entity.getLocation())))
                        .orElse(null);
                break;
            case "Слабейший":
                target = entity.getNearbyEntities(eRange, eRange, eRange).stream()
                        .filter(e -> e instanceof LivingEntity && !(e instanceof Player) && !(e instanceof ArmorStand) && e != entity)
                        .map(e -> (LivingEntity) e)
                        .filter(e -> !hasSameColorCode(e, getColorCode(entity.getCustomName())))
                        .min(Comparator.comparingDouble(Damageable::getHealth))
                        .orElse(null);
                break;
            case "Сильнейший":
                target = entity.getNearbyEntities(eRange, eRange, eRange).stream()
                        .filter(e -> e instanceof LivingEntity && !(e instanceof Player) && !(e instanceof ArmorStand) && e != entity)
                        .map(e -> (LivingEntity) e)
                        .filter(e -> !hasSameColorCode(e, getColorCode(entity.getCustomName())))
                        .max(Comparator.comparingDouble(Damageable::getHealth))
                        .orElse(null);
                break;
        }

        return target;
    }

    private static String getColorCode(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        return name.substring(0, 2);
    }

    private static boolean hasSameColorCode(LivingEntity entity, String colorCode) {
        String entityName = entity.getCustomName();
        return entityName != null && entityName.startsWith(colorCode);
    }
}
