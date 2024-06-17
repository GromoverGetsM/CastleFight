package ru.rstudios.castlefight.utils;

import org.bukkit.*;

public class ParticleUtil {

    public void createParticleBlock(Location location) {
        World world = location.getWorld();
        if (world == null) return;

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        Particle.DustOptions dustOptions = new Particle.DustOptions(org.bukkit.Color.RED, 1);

        for (double dx = 0; dx <= 1; dx += 0.1) {
            world.spawnParticle(Particle.REDSTONE, new Location(world, x + dx, y + 1, z), 1, dustOptions);
            world.spawnParticle(Particle.REDSTONE, new Location(world, x + dx, y + 1, z + 1), 1, dustOptions);
        }

        for (double dz = 0; dz <= 1; dz += 0.1) {
            world.spawnParticle(Particle.REDSTONE, new Location(world, x, y + 1, z + dz), 1, dustOptions);
            world.spawnParticle(Particle.REDSTONE, new Location(world, x + 1, y + 1, z + dz), 1, dustOptions);
        }
    }

    public void createParticleMultiblock3x3(Location location) {
        Location center = location.clone().add(0.5, 1, 0.5);

        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1.0F);

        for (double x = -1; x <= 1; x += 0.1) {
            for (double z = -1; z <= 1; z += 0.1) {
                center.getWorld().spawnParticle(Particle.REDSTONE, center.clone().add(x, 0, -1), 1, dustOptions);
                center.getWorld().spawnParticle(Particle.REDSTONE, center.clone().add(x, 0, 1), 1, dustOptions);
                center.getWorld().spawnParticle(Particle.REDSTONE, center.clone().add(-1, 0, z), 1, dustOptions);
                center.getWorld().spawnParticle(Particle.REDSTONE, center.clone().add(1, 0, z), 1, dustOptions);

                center.getWorld().spawnParticle(Particle.REDSTONE, center.clone().add(x, 2, -1), 1, dustOptions);
                center.getWorld().spawnParticle(Particle.REDSTONE, center.clone().add(x, 2, 1), 1, dustOptions);
                center.getWorld().spawnParticle(Particle.REDSTONE, center.clone().add(-1, 2, z), 1, dustOptions);
                center.getWorld().spawnParticle(Particle.REDSTONE, center.clone().add(1, 2, z), 1, dustOptions);
            }
        }

        for (double y = 0; y <= 2; y += 0.1) {
            center.getWorld().spawnParticle(Particle.REDSTONE, center.clone().add(-1, y, -1), 1, dustOptions);
            center.getWorld().spawnParticle(Particle.REDSTONE, center.clone().add(1, y, -1), 1, dustOptions);
            center.getWorld().spawnParticle(Particle.REDSTONE, center.clone().add(-1, y, 1), 1, dustOptions);
            center.getWorld().spawnParticle(Particle.REDSTONE, center.clone().add(1, y, 1), 1, dustOptions);
        }
    }
}
