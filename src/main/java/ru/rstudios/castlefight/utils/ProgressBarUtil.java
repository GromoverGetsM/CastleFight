package ru.rstudios.castlefight.utils;

public class ProgressBarUtil {
    public static String createProgressBar(double progress, int totalBars) {
        int progressBars = (int) (totalBars * progress);

        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < totalBars; i++) {
            if (i < progressBars) {
                bar.append("§a█");
            } else {
                bar.append("§c█");
            }
        }

        return bar.toString();
    }
}