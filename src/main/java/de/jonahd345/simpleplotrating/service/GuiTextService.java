package de.jonahd345.simpleplotrating.service;

import de.jonahd345.simpleplotrating.SimplePlotRating;
import de.jonahd345.simpleplotrating.model.GuiText;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class GuiTextService {
    private SimplePlotRating plugin;

    private File file;

    private FileConfiguration yamlConfiguration;

    public GuiTextService(SimplePlotRating plugin) {
        this.plugin = plugin;
        this.file = new File("plugins/" + this.plugin.getName() + "/guitexts.yml");
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.file);
    }

    public void loadTexts() {
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.file);
        boolean hasFileChanges = false;

        for (GuiText text : GuiText.values()) {
            if (!(this.file.exists()) || this.yamlConfiguration.getString(text.name().toLowerCase()) == null) {
                this.yamlConfiguration.set(text.name(), text.getDefaultText());
                hasFileChanges = true;
            }
            text.setText(this.translateColorCodes(this.yamlConfiguration.getString(text.name().toLowerCase())));
        }
        if (hasFileChanges) {
            this.saveFile();
        }
    }

    private void saveFile() {
        try {
            this.yamlConfiguration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String translateColorCodes(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
