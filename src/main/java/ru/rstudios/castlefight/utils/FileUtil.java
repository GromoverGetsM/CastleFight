package ru.rstudios.castlefight.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static ru.rstudios.castlefight.CastleFight.*;

public class FileUtil {

    public void createNewFile (String folder, String name) throws IOException {
        File uFile = new File(plugin.getDataFolder(), folder);
        if (!uFile.exists() || !uFile.isDirectory()) {
            if (!uFile.mkdirs()) {
                errorUtil.criterrorfromconfig(null, "castlefight.errors.cannot-create-folder");
            }
        }

        File file = new File(uFile, name);
        if (!file.exists() || !file.isFile()) {
            if (!file.createNewFile()) {
                errorUtil.criterrorfromconfig(null, "castlefight.errors.cannot-create-file");
            }
        }
    }

    public void createNewFile (File folder, String name) throws IOException {
        if (folder.exists() && folder.isDirectory()) {
            File file = new File(folder, name);
            if (!file.exists() || !file.isFile()) {
                if (!file.createNewFile()) {
                    errorUtil.criterrorfromconfig(null, "castlefight.errors.cannot-create-file");
                }
            }
        } else {
            if (folder.mkdirs()) {
                File file = new File(folder, name);
                if (!file.exists() || !file.isFile()) {
                    if (!file.createNewFile()) {
                        errorUtil.criterrorfromconfig(null, "castlefight.errors.cannot-create-file");
                    }
                }
            } else {
                errorUtil.criterrorfromconfig(null, "castlefight.errors.cannot-create-folder");
            }
        }
    }

    public void createStarterFolder (String folderName) {
        File folder = new File(plugin.getDataFolder(), folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public boolean copyFilesTo (File from, File to) {
        try {
            File[] toCopy = from.listFiles();

            if (!to.exists() && !to.mkdirs()) {
                errorUtil.criterrorfromconfig(null, "castlefight.errors.cannot-create-folder");
                return false;
            }

            if (toCopy != null) {
                int length = toCopy.length;

                for (int i = 0; i < length; ++i) {
                    File copyFile = toCopy[i];
                    if (copyFile.isFile()) {
                        org.apache.commons.io.FileUtils.copyFileToDirectory(copyFile, to);
                    } else {
                        org.apache.commons.io.FileUtils.copyDirectoryToDirectory(copyFile, to);
                    }
                }
            }

            return true;
        } catch (IOException e) {
            errorUtil.criterror(null, e.getLocalizedMessage());
            return false;
        }
    }
    public FileConfiguration loadFile (String fileName) {
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), fileName));
    }

    public FileConfiguration loadUnusualFolderFile (String fileName, String folder) {
        File uFolder = new File(plugin.getDataFolder(), folder);
        if (uFolder.exists() && !uFolder.isFile()) {
            return YamlConfiguration.loadConfiguration(new File(uFolder, fileName));
        } else {
            errorUtil.criterror(null, "Папка плагина " + uFolder + " не существует!");
            return null;
        }
    }

    public void save (String fileName) throws IOException {
        fileUtil.loadFile(fileName).save(new File(plugin.getDataFolder(), fileName));
    }

    public void saveUnusualConfig (String fileName, boolean needsToReplace) {
        plugin.saveResource(fileName, needsToReplace);
    }
}
