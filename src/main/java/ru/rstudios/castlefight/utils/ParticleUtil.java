package ru.rstudios.castlefight.utils;

import org.bukkit.*;

import static ru.rstudios.castlefight.CastleFight.plugin;

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
        World world = location.getWorld();
        if (world == null) return;

        Location center = location.clone().add(0.5, 1, 0.5);

        boolean hasObstruction = false;

        for (int x = -2; x <= 2; x++) {
            for (int y = 1; y <= 4; y++) {
                for (int z = -2; z <= 2; z++) {
                    Location checkLocation = location.clone().add(x, y, z);
                    if (world.getBlockAt(checkLocation).getType() != Material.AIR) {
                        hasObstruction = true;
                    }
                }
                if (hasObstruction) break;
            }
            if (hasObstruction) break;
        }

        Particle.DustOptions dustOptions = hasObstruction ? new Particle.DustOptions(Color.RED, 1.0F) : new Particle.DustOptions(Color.GREEN, 1.0F);
        Particle particleType = Particle.REDSTONE;

        for (double x = -1; x <= 1; x += 0.1) {
            for (double z = -1; z <= 1; z += 0.1) {
                world.spawnParticle(particleType, center.clone().add(x, 0, -1), 1, dustOptions);
                world.spawnParticle(particleType, center.clone().add(x, 0, 1), 1, dustOptions);
                world.spawnParticle(particleType, center.clone().add(-1, 0, z), 1, dustOptions);
                world.spawnParticle(particleType, center.clone().add(1, 0, z), 1, dustOptions);

                world.spawnParticle(particleType, center.clone().add(x, 2, -1), 1, dustOptions);
                world.spawnParticle(particleType, center.clone().add(x, 2, 1), 1, dustOptions);
                world.spawnParticle(particleType, center.clone().add(-1, 2, z), 1, dustOptions);
                world.spawnParticle(particleType, center.clone().add(1, 2, z), 1, dustOptions);
            }
        }

        for (double y = 0; y <= 2; y += 0.1) {
            world.spawnParticle(particleType, center.clone().add(-1, y, -1), 1, dustOptions);
            world.spawnParticle(particleType, center.clone().add(1, y, -1), 1, dustOptions);
            world.spawnParticle(particleType, center.clone().add(-1, y, 1), 1, dustOptions);
            world.spawnParticle(particleType, center.clone().add(1, y, 1), 1, dustOptions);
        }
    }
}
