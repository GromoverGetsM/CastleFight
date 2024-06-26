package ru.rstudios.castlefight.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import ru.rstudios.castlefight.utils.ParticleUtil;

import static ru.rstudios.castlefight.CastleFight.plugin;

public class ParticleTask implements Runnable {

    @Override
    public void run() {
        // Игроки в мейне, обработка таргетблока в асинке
        for (Player player : Bukkit.getOnlinePlayers()) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                Block targetBlock = player.getTargetBlockExact(5);

                if (targetBlock != null && !targetBlock.getType().isAir()) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        if (player.getInventory().getItemInMainHand().getType() != Material.GOLDEN_AXE) {
                            ParticleUtil.createParticleBlock(targetBlock.getLocation());
                        } else {
                            ParticleUtil.createParticleMultiblock3x3(targetBlock.getLocation());
                        }
                    });
                }
            });
        }
    }
}