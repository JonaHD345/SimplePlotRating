package de.jonahd345.simpleplotrating.service;

import de.jonahd345.simpleplotrating.SimplePlotRating;
import de.jonahd345.simpleplotrating.model.GuiText;
import de.jonahd345.simpleplotrating.util.FileUtil;
import de.jonahd345.simpleplotrating.util.StringUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

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
                this.yamlConfiguration.set(text.name().toLowerCase(), text.getDefaultText());
                hasFileChanges = true;
                // set Text's text to his default text and skip the next line, because by new mess yamlConfiguration.getString is null
                text.setText(StringUtil.translateColorCodes(text.getDefaultText()));
                continue;
            }
            text.setText(StringUtil.translateColorCodes(this.yamlConfiguration.getString(text.name().toLowerCase())));
        }
        if (hasFileChanges) {
            FileUtil.saveFile(this.yamlConfiguration, this.file);
        }
    }
}
