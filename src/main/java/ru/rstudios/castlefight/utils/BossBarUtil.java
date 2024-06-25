package ru.rstudios.castlefight.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class BossBarUtil {

    private static final HashMap<String, BossBar> bossBarList = new HashMap<>();

    public static void createBossbar (World world, String name, String title, boolean show) {
        BossBar bossBar = Bukkit.createBossBar(title, BarColor.RED, BarStyle.SOLID);
        bossBarList.put(name, bossBar);

        bossBar.setVisible(show);
        bossBar.removeAll();

        for (Player player : world.getPlayers()) {
            bossBar.addPlayer(player);
        }
    }

    public static void createBossbar (World world, String name, String title, BarColor color, BarStyle style, boolean show) {
        BossBar bossBar = Bukkit.createBossBar(title, color, style);
        bossBarList.put(name, bossBar);

        bossBar.setVisible(show);
        bossBar.removeAll();

        for (Player player : world.getPlayers()) {
            bossBar.addPlayer(player);
        }
    }

    public static void createBossbar (World world, String name, String title, BarColor color, BarStyle style, boolean show, double progress) {
        BossBar bossBar = Bukkit.createBossBar(title, color, style);
        bossBarList.put(name, bossBar);

        bossBar.setVisible(show);
        bossBar.setProgress(progress);
        bossBar.removeAll();

        for (Player player : world.getPlayers()) {
            bossBar.addPlayer(player);
        }
    }



    public static void deleteBossbar (String name) {
        BossBar bossBar = bossBarList.get(name);
        bossBar.setVisible(false);
        bossBar.removeAll();
        bossBarList.remove(name);
    }

    public static void showBossbar (World world, String name) {
        BossBar bossBar = bossBarList.get(name);
        bossBar.removeAll();

        for (Player player : world.getPlayers()) {
            bossBar.addPlayer(player);
        }

        bossBar.setVisible(true);
    }

    public static void hideBossbar (World world, String name) {
        BossBar bossBar = bossBarList.get(name);
        bossBar.removeAll();

        for (Player player : world.getPlayers()) {
            bossBar.addPlayer(player);
        }

        bossBar.setVisible(false);
    }

    public static void showBossbarForPlayer (Player player, String name) {
        BossBar bossBar = bossBarList.get(name);
        bossBar.addPlayer(player);
    }

    public static void setProgress (String name, double progress) {
        BossBar bossBar = bossBarList.get(name);
        bossBarList.remove(name);

        bossBar.setProgress(progress);
        bossBarList.put(name, bossBar);
    }

    public static void hideBossbarForPlayer (Player player, String name) {
        BossBar bossBar = bossBarList.get(name);
        bossBar.removePlayer(player);
    }

    public static void setBossBarTitle (String name, String title) {
        BossBar bossBar = bossBarList.get(name);
        bossBarList.remove(name);

        bossBar.setTitle(title);
        bossBarList.put(name, bossBar);
    }

    public static void setBossBarColor (String name, BarColor color) {
        BossBar bossBar = bossBarList.get(name);
        bossBarList.remove(name);

        bossBar.setColor(color);
        bossBarList.put(name, bossBar);
    }

    public static void setBossBarStyle (String name, BarStyle style) {
        BossBar bossBar = bossBarList.get(name);
        bossBarList.remove(name);

        bossBar.setStyle(style);
        bossBarList.put(name, bossBar);
    }
}
