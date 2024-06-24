package ru.rstudios.castlefight.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

import static ru.rstudios.castlefight.CastleFight.*;
import static ru.rstudios.castlefight.CastleFight.plugin;

public class UnitCreator {



    public void createUnit (String unitOwner, String role, String tower, int level, String team, Location target, Location spawn) {
        if (roleUtil.getRoleUnitData(role, tower, level) != null) {
            HashMap<String, Object> unitData = roleUtil.getRoleUnitData(role, tower, level);

            Entity entity = spawn.getWorld().spawnEntity(spawn, EntityType.valueOf(unitData.get("EntityType").toString()));
            if (team.equals("red")) {
                entity.setCustomName(ChatColor.translateAlternateColorCodes('&', "&c&l" + unitData.get("UnitName").toString()));
            } else {
                entity.setCustomName(ChatColor.translateAlternateColorCodes('&', "&9&l" + unitData.get("UnitName").toString()));
            }
            entity.setCustomNameVisible(true);

            PersistentDataContainer dataContainer = entity.getPersistentDataContainer();

            NamespacedKey owner = new NamespacedKey(plugin, "Owner");
            NamespacedKey attackType = new NamespacedKey(plugin, "AttackType");
            NamespacedKey protectionType = new NamespacedKey(plugin, "ProtectionType");

            dataContainer.set(owner, PersistentDataType.STRING, unitOwner);
            dataContainer.set(attackType, PersistentDataType.STRING, unitData.get("AttackType").toString());
            dataContainer.set(protectionType, PersistentDataType.STRING, unitData.get("DefenseType").toString());

            LivingEntity livingEntity = (LivingEntity) entity;
            Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(0);
            Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.175);

            Damageable damageable = livingEntity;
            damageable.setMaxHealth(Integer.parseInt(unitData.get("Health").toString()));
            damageable.setHealth(Integer.parseInt(unitData.get("Health").toString()));

            ((Mob) entity).setTarget(null);

            ((Mob) entity).getPathfinder().moveTo(target);

            final LivingEntity[] currentTarget = {null};


            new BukkitRunnable() {

                @Override
                public void run() {
                    if (entity.isDead()) {
                        cancel();
                        return;
                    }

                    String entityColorCode = getColorCode(entity.getCustomName());

                    currentTarget[0] = entity.getNearbyEntities(12, 12, 12).stream()
                            .filter(e -> e instanceof LivingEntity && !(e instanceof Player) && !(e instanceof ArmorStand) && e != entity)
                            .map(e -> (LivingEntity) e)
                            .filter(e -> !hasSameColorCode(e, entityColorCode))
                            .min(Comparator.comparingDouble(e -> e.getLocation().distance(entity.getLocation())))
                            .orElse(null);

                    if (currentTarget[0] != null && entity instanceof Mob && !(entity instanceof Player)) {
                        ((Mob) entity).setTarget(currentTarget[0]);
                    } else if (currentTarget[0] == null) {
                        ((Mob) entity).setTarget(null);
                        ((Mob) entity).getPathfinder().moveTo(target);
                    }
                }
            }.runTaskTimer(plugin, 0, 5);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (entity.isDead()) {
                        cancel();
                        return;
                    }

                    if (currentTarget[0] != null && !currentTarget[0].isDead() && currentTarget[0].getLocation().distance(entity.getLocation()) <= 2) {
                        currentTarget[0].damage(countDamageUtil.getUnitDamage(entity, currentTarget[0], Double.parseDouble(unitData.get("Damage").toString())), entity);
                    }
                }
            }.runTaskTimer(plugin, 0, Integer.parseInt(unitData.get("Cooldown").toString()));
        } else {
            errorUtil.errorfromconfig(null, "castlefight.errors.unknown-unit-data");
        }
    }

    private String getColorCode(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        return name.substring(0, 2);
    }

    private boolean hasSameColorCode(LivingEntity entity, String colorCode) {
        String entityName = entity.getCustomName();
        return entityName != null && entityName.startsWith(colorCode);
    }
}
