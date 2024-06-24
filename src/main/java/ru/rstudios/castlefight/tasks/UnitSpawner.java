package ru.rstudios.castlefight.tasks;

import org.bukkit.Location;
import org.bukkit.metadata.MetadataValue;
import ru.rstudios.castlefight.modules.GameInfo;

import java.util.List;

import static ru.rstudios.castlefight.CastleFight.holoUtil;
import static ru.rstudios.castlefight.CastleFight.unitCreator;

public class UnitSpawner implements Runnable {

    private int ticksElapsed = 0;

    private final String role;
    private final String tower;
    private final int level;
    private final int spawnRate;
    private final Location structureLeftBottom;
    private final String unitOwner;
    private final int ID;

    public UnitSpawner(int ID, String unitOwner, String role, String tower, int level, int spawnRate, Location structureLeftBottom) {
        this.ID = ID;
        this.unitOwner = unitOwner;
        this.role = role;
        this.tower = tower;
        this.level = level;
        this.spawnRate = spawnRate;
        this.structureLeftBottom = structureLeftBottom;
    }

    @Override
    public void run() {
        ticksElapsed++;

        double progress = (double) ticksElapsed / spawnRate;
        updateProgressBar(progress);

        if (ticksElapsed >= spawnRate) {
            ticksElapsed = 0;

            spawnUnitAroundStructure();
        }
    }

    private void updateProgressBar(double progress) {
        int greenBars = (int) (progress * 10);
        int redBars = 10 - greenBars;

        StringBuilder progressBar = new StringBuilder();
        progressBar.append("§a█".repeat(Math.max(0, greenBars)));
        progressBar.append("§c█".repeat(Math.max(0, redBars)));

        List<MetadataValue> holoName = structureLeftBottom.getBlock().getMetadata("holoName");
        if (!holoName.isEmpty()) {
            holoUtil.setHoloLine(structureLeftBottom.getWorld(), holoName.get(0).asString(), progressBar.toString(), 2);
        }
    }

    private void spawnUnitAroundStructure() {
        Location spawnLocation = structureLeftBottom.clone().add(-1, 0, 0);
        Location enemyBase;

        GameInfo gameInfo = new GameInfo(ID);

        if (gameInfo.getPlayerActiveRole(unitOwner).equals("blue")) {
            enemyBase = gameInfo.getBlueBase();
        } else {
            enemyBase = gameInfo.getRedBase();
        }

        unitCreator.createUnit(unitOwner, role, tower, level, gameInfo.getPlayerTeam(unitOwner), enemyBase, spawnLocation);
    }
}
