package ru.rstudios.castlefight.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static ru.rstudios.castlefight.CastleFight.*;

public class FileUtil {

    public static void createNewFile (String folder, String name) throws IOException {
        File uFile = new File(plugin.getDataFolder(), folder);
        if (!uFile.exists() || !uFile.isDirectory()) {
            if (!uFile.mkdirs()) {
                ErrorUtil.criterrorfromconfig(null, "castlefight.errors.cannot-create-folder");
            }
        }

        File file = new File(uFile, name);
        if (!file.exists() || !file.isFile()) {
            if (!file.createNewFile()) {
                ErrorUtil.criterrorfromconfig(null, "castlefight.errors.cannot-create-file");
            }
        }
    }

    public static void createNewFile (File folder, String name) throws IOException {
        if (folder.exists() && folder.isDirectory()) {
            File file = new File(folder, name);
            if (!file.exists() || !file.isFile()) {
                if (!file.createNewFile()) {
                    ErrorUtil.criterrorfromconfig(null, "castlefight.errors.cannot-create-file");
                }
            }
        } else {
            if (folder.mkdirs()) {
                File file = new File(folder, name);
                if (!file.exists() || !file.isFile()) {
                    if (!file.createNewFile()) {
                        ErrorUtil.criterrorfromconfig(null, "castlefight.errors.cannot-create-file");
                    }
                }
            } else {
                ErrorUtil.criterrorfromconfig(null, "castlefight.errors.cannot-create-folder");
            }
        }
    }

    public static void createStarterFolder (String folderName) {
        File folder = new File(plugin.getDataFolder(), folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public static void copyFilesTo (File from, File to) {
        try {
            File[] toCopy = from.listFiles();

            if (!to.exists() && !to.mkdirs()) {
                ErrorUtil.criterrorfromconfig(null, "castlefight.errors.cannot-create-folder");
                return;
            }

            if (toCopy != null) {

                for (File copyFile : toCopy) {
                    if (copyFile.isFile()) {
                        org.apache.commons.io.FileUtils.copyFileToDirectory(copyFile, to);
                    } else {
                        org.apache.commons.io.FileUtils.copyDirectoryToDirectory(copyFile, to);
                    }
                }
            }

        } catch (IOException e) {
            ErrorUtil.criterror(null, e.getLocalizedMessage());
        }
    }
    public static FileConfiguration loadFile (String fileName) {
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), fileName));
    }

    public static FileConfiguration loadUnusualFolderFile (String fileName, String folder) {
        File uFolder = new File(plugin.getDataFolder(), folder);
        if (uFolder.exists() && !uFolder.isFile()) {
            return YamlConfiguration.loadConfiguration(new File(uFolder, fileName));
        } else {
            ErrorUtil.criterrorfromconfig(null, "castlefight.errors.folder-not-found");
            return null;
        }
    }

    public static void save (String fileName) throws IOException {
        FileUtil.loadFile(fileName).save(new File(plugin.getDataFolder(), fileName));
    }

    public static void saveUnusualConfig (String fileName, boolean needsToReplace) {
        plugin.saveResource(fileName, needsToReplace);
    }

    public static boolean deleteWorld(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteWorld(file);
                } else {
                    file.delete();
                }
            }
        }

        return (path.delete());
    }
}
