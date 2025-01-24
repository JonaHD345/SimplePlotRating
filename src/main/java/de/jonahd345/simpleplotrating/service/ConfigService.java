package de.jonahd345.simpleplotrating.service;

import de.jonahd345.simpleplotrating.SimplePlotRating;
import de.jonahd345.simpleplotrating.model.GuiText;
import de.jonahd345.simpleplotrating.model.Message;
import de.jonahd345.simpleplotrating.model.SignText;
import de.jonahd345.simpleplotrating.util.FileUtil;
import de.jonahd345.simpleplotrating.util.StringUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigService {
    private SimplePlotRating plugin;

    private File file;

    private FileConfiguration yamlConfiguration;

    public ConfigService(SimplePlotRating plugin) {
        this.plugin = plugin;
        this.file = new File("plugins/" + this.plugin.getName() + "/config.yml");
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.file);
    }

    public void loadConfig() {
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.file);
        boolean hasFileChanges = false;

        // TODO Config
        // Messages
        for (Message message : Message.values()) {
            if (!(this.file.exists()) || this.yamlConfiguration.getString("messages." + message.name().toLowerCase()) == null) {
                this.yamlConfiguration.set("messages." + message.name().toLowerCase(), message.getDefaultMessage());
                hasFileChanges = true;
                // set Message's message to his default message and skip the next line, because by new mess yamlConfiguration.getString is null
                message.setMessage(StringUtil.translateColorCodes(message.getDefaultMessage()));
                continue;
            }
            message.setMessage(StringUtil.translateColorCodes(this.yamlConfiguration.getString("messages." + message.name().toLowerCase())));
        }
        // Text for Guis
        for (GuiText text : GuiText.values()) {
            if (!(this.file.exists()) || this.yamlConfiguration.getString("guitexts." + text.name().toLowerCase()) == null) {
                this.yamlConfiguration.set("guitexts." + text.name().toLowerCase(), text.getDefaultText());
                hasFileChanges = true;
                // set Text's text to his default text and skip the next line, because by new mess yamlConfiguration.getString is null
                text.setText(StringUtil.translateColorCodes(text.getDefaultText()));
                continue;
            }
            text.setText(StringUtil.translateColorCodes(this.yamlConfiguration.getString("guitexts." + text.name().toLowerCase())));
        }
        // Texts for signs
        for (SignText text : SignText.values()) {
            if (!(this.file.exists()) || this.yamlConfiguration.getString("signtexts." + text.name().toLowerCase()) == null) {
                this.yamlConfiguration.set("signtexts." + text.name().toLowerCase(), text.getDefaultText());
                hasFileChanges = true;
                // set Text's text to his default text and skip the next line, because by new mess yamlConfiguration.getString is null
                text.setText(StringUtil.translateColorCodes(text.getDefaultText()));
                continue;
            }
            text.setText(StringUtil.translateColorCodes(this.yamlConfiguration.getString("signtexts." + text.name().toLowerCase())));
        }
        if (hasFileChanges) {
            FileUtil.saveFile(this.yamlConfiguration, this.file);
        }
    }
}
