package ru.rstudios.castlefight.utils;

import org.bukkit.Location;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.Locale;

import static ru.rstudios.castlefight.CastleFight.holoUtil;

public class ProgressBarUtil {
    public static void createProgressBar(int passed, int all, double progress, Location leftBottomAngle) {
        int greenBars = (int) (progress * 10);
        int redBars = 10 - greenBars;
        int ticksRemaining = passed - all;
        double secondsRemaining = ticksRemaining / 20.0;

        StringBuilder progressBar = new StringBuilder();
        progressBar.append("§a").append("█".repeat(Math.max(0, greenBars)));
        progressBar.append("§c").append("█".repeat(Math.max(0, redBars)));
        progressBar.append(" §e").append(String.format(Locale.US, "%.1f", secondsRemaining)).append("s");

        List<MetadataValue> holoName = leftBottomAngle.getBlock().getMetadata("holoName");
        if (!holoName.isEmpty()) {
            HoloUtil.setHoloLine(leftBottomAngle.getWorld(), holoName.get(0).asString(), progressBar.toString(), 2);
        }
    }
}