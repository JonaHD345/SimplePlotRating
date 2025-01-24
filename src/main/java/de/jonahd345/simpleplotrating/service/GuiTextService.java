package de.jonahd345.simpleplotrating.service;

import de.jonahd345.simpleplotrating.SimplePlotRating;
import de.jonahd345.simpleplotrating.model.GuiText;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GuiTextService {
    private SimplePlotRating plugin;

    private File file;

    private FileConfiguration yamlConfiguration;

    @Getter
    private Map<GuiText, String> texts;

    public GuiTextService(SimplePlotRating plugin) {
        this.plugin = plugin;
        this.file = new File("plugins/" + this.plugin.getName() + "/guitexts.yml");
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.file);
        this.texts = new HashMap<>();
    }

    public void loadTexts() {
        this.texts.clear();
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.file);
        boolean hasFileChanges = false;

        for (GuiText text : GuiText.values()) {
            if (!(this.file.exists()) || this.yamlConfiguration.getString(text.name().toLowerCase()) == null) {
                this.yamlConfiguration.set(text.name(), text.getDefaultText());
                hasFileChanges = true;
            }
            this.texts.put(text, this.translateColorCodes(this.yamlConfiguration.getString(text.name().toLowerCase())));
        }
        if (hasFileChanges) {
            this.saveFile();
        }
    }

    public String getMessage(GuiText text) {
        return this.texts.get(text);
    }

    public String getMessageWithPrefix(GuiText text) {
        return this.texts.get(GuiText.PREFIX) + this.texts.get(text);
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
