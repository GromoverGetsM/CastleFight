package ru.rstudios.castlefight.listeners;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

import static ru.rstudios.castlefight.CastleFight.messagesUtil;
import static ru.rstudios.castlefight.CastleFight.plugin;

public class EntityDamageListener implements Listener {

    private final Map<LivingEntity, LivingEntity> lastDamager = new HashMap<>();

    @EventHandler
    public void onEntityDamaged (EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL && entity.getCustomName().startsWith("&:")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity) {
            LivingEntity victim = (LivingEntity) event.getEntity();
            LivingEntity damager = (LivingEntity) event.getDamager();
            lastDamager.put(victim, damager);
        }
    }

    @EventHandler
    public void onEntityDied (EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        LivingEntity damager = lastDamager.remove(entity);

        if (damager != null) {
            if (damager instanceof Player) {
                ((Player) damager).sendActionBar(messagesUtil.messageString("castlefight.income.unitkilledenemy"));
            } else {
                if (!damager.getPersistentDataContainer().isEmpty()) {
                    NamespacedKey unitOwner = new NamespacedKey(plugin, "Owner");
                    PersistentDataContainer container = damager.getPersistentDataContainer();
                    String owner = container.get(unitOwner, PersistentDataType.STRING);
                    if (owner != null && !owner.equalsIgnoreCase("none") && Bukkit.getPlayer(owner).isOnline() && Bukkit.getPlayer(owner).getWorld() == damager.getWorld()) {
                        Player player = Bukkit.getPlayer(owner);
                        player.sendActionBar(messagesUtil.messageString("castlefight.income.unitkilledenemy"));
                    }
                }
            }
        }
    }


}
