package ru.rstudios.castlefight.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import ru.rstudios.castlefight.utils.ParticleUtil;

public class ParticleTask implements Runnable {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Block targetBlock = player.getTargetBlockExact(5);
            if (targetBlock != null && !targetBlock.getType().isAir()) {
                if (player.getInventory().getItemInMainHand().getType() != Material.GOLDEN_AXE) {
                    ParticleUtil.createParticleBlock(targetBlock.getLocation());
                } else {
                    ParticleUtil.createParticleMultiblock3x3(targetBlock.getLocation());
                }
            }
        }
    }
}
