package ru.rstudios.castlefight;

import org.bukkit.plugin.java.JavaPlugin;

public final class CastleFight extends JavaPlugin {

    public static JavaPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
