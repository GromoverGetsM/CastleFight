package ru.rstudios.castlefight.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static ru.rstudios.castlefight.CastleFight.*;

public class spawnCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 4) {
                Location spawn = player.getLocation();

                if (roleUtil.getRoleUnitData(args[0], args[1], Integer.parseInt(args[2])) != null) {
                    HashMap<String, Object> unitData = roleUtil.getRoleUnitData(args[0], args[1], Integer.parseInt(args[2]));

                    Entity entity = player.getWorld().spawnEntity(spawn, EntityType.valueOf(unitData.get("EntityType").toString()));
                    if (args[3].equals("red")) {
                        entity.setCustomName(ChatColor.translateAlternateColorCodes('&', "&c&l" + unitData.get("UnitName").toString()));
                    } else {
                        entity.setCustomName(ChatColor.translateAlternateColorCodes('&', "&9&l" + unitData.get("UnitName").toString()));
                    }
                    entity.setCustomNameVisible(true);

                    PersistentDataContainer dataContainer = entity.getPersistentDataContainer();

                    NamespacedKey owner = new NamespacedKey(plugin, "Owner");
                    NamespacedKey attackType = new NamespacedKey(plugin, "AttackType");
                    NamespacedKey protectionType = new NamespacedKey(plugin, "ProtectionType");

                    dataContainer.set(owner, PersistentDataType.STRING, sender.getName());
                    dataContainer.set(attackType, PersistentDataType.STRING, unitData.get("AttackType").toString());
                    dataContainer.set(protectionType, PersistentDataType.STRING, unitData.get("DefenseType").toString());

                    LivingEntity livingEntity = (LivingEntity) entity;
                    Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(0);
                    Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.175);

                    Damageable damageable = livingEntity;
                    damageable.setMaxHealth(Integer.parseInt(unitData.get("Health").toString()));
                    damageable.setHealth(Integer.parseInt(unitData.get("Health").toString()));

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
                                    .filter(e -> e instanceof LivingEntity && !(e instanceof Player) && e != entity)
                                    .map(e -> (LivingEntity) e)
                                    .filter(e -> !hasSameColorCode(e, entityColorCode))
                                    .min(Comparator.comparingDouble(e -> e.getLocation().distance(entity.getLocation())))
                                    .orElse(null);

                            if (currentTarget[0] != null && entity instanceof Mob && !(entity instanceof Player)) {
                                ((Mob) entity).setTarget(currentTarget[0]);
                            } else if (currentTarget[0] == null) {
                                ((Mob) entity).setTarget(null);
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
                                sender.sendMessage("Нанесено урона юнитом " + entity + " по врагу " + currentTarget[0] + ": " + countDamageUtil.getUnitDamage(entity, currentTarget[0], Double.parseDouble(unitData.get("Damage").toString())) + " ед.");
                            }
                        }
                    }.runTaskTimer(plugin, 0, Integer.parseInt(unitData.get("Cooldown").toString()));
                }
            } else {
                errorUtil.errorfromconfig(player, "castlefight.errors.invalid-args");
            }
        } else {
            errorUtil.errorfromconfig(null, "castlefight.errors.only-player");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        List<String> pArgs = new ArrayList<>();
        switch (args.length) {
            case 1:
                pArgs.add("elfs");
                return pArgs;
            case 2:
                pArgs.add("spiderslair");
                return pArgs;
            case 3:
                pArgs.add("1");
                pArgs.add("2");
                pArgs.add("3");
                pArgs.add("4");
                return pArgs;
            case 4:
                pArgs.add("blue");
                pArgs.add("red");
            default:
                return pArgs;
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
