package de.jonahd345.simpleplotrating.util;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    /**
     * Saves the provided YamlConfiguration to the specified file.
     *
     * @param yamlConfiguration the YamlConfiguration to save
     * @param file the file to save the YamlConfiguration to
     */
    public static void saveFile(FileConfiguration yamlConfiguration, File file) {
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
