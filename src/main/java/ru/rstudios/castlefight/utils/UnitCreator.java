package ru.rstudios.castlefight.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;

import static ru.rstudios.castlefight.CastleFight.plugin;

public class UnitCreator {



    public static void createUnit (String unitOwner, String role, String tower, int level, String team, Location target, Location spawn) {
        if (RoleUtil.getRoleUnitData(role, tower, level) != null) {
            HashMap<String, Object> unitData = RoleUtil.getRoleUnitData(role, tower, level);

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
            NamespacedKey range = new NamespacedKey(plugin, "Range");
            NamespacedKey preferTarget = new NamespacedKey(plugin, "PreferedTarget");

            dataContainer.set(owner, PersistentDataType.STRING, unitOwner);
            dataContainer.set(attackType, PersistentDataType.STRING, unitData.get("AttackType").toString());
            dataContainer.set(protectionType, PersistentDataType.STRING, unitData.get("DefenseType").toString());
            dataContainer.set(range, PersistentDataType.INTEGER, Integer.parseInt(unitData.get("Range").toString()));
            dataContainer.set(preferTarget, PersistentDataType.STRING, "Ближайший");

            LivingEntity livingEntity = (LivingEntity) entity;
            Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(0);
            Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.175);

            livingEntity.setMaxHealth(Integer.parseInt(unitData.get("Health").toString()));
            livingEntity.setHealth(Integer.parseInt(unitData.get("Health").toString()));

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

                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        LivingEntity unitTarget = CountDamageUtil.getTarget(entity);

                        Bukkit.getScheduler().runTask(plugin, () -> {
                            if (entity.isDead()) {
                                return;
                            }
                            currentTarget[0] = unitTarget;
                            if (currentTarget[0] != null && !(entity instanceof Player)) {
                                ((Mob) entity).setTarget(currentTarget[0]);
                            } else if (currentTarget[0] == null) {
                                ((Mob) entity).setTarget(null);
                                ((Mob) entity).getPathfinder().moveTo(target);
                            }
                        });
                    });
                }
            }.runTaskTimer(plugin, 0, 5);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (entity.isDead()) {
                        cancel();
                        return;
                    }

                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        if (currentTarget[0] != null && !currentTarget[0].isDead() && currentTarget[0].getLocation().distance(entity.getLocation()) <= 2) {
                            double damage = CountDamageUtil.getUnitDamage(entity, currentTarget[0], Double.parseDouble(unitData.get("Damage").toString()));
                            Bukkit.getScheduler().runTask(plugin, () -> {
                                if (!currentTarget[0].isDead()) {
                                    currentTarget[0].damage(damage, entity);
                                }
                            });
                        }
                    });
                }
            }.runTaskTimer(plugin, 0, Integer.parseInt(unitData.get("Cooldown").toString()));

        } else {
            ErrorUtil.errorfromconfig(null, "castlefight.errors.unknown-unit-data");
        }
    }
}
