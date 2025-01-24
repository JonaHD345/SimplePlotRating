package de.jonahd345.simpleplotrating.service;

import de.jonahd345.simpleplotrating.SimplePlotRating;
import de.jonahd345.simpleplotrating.model.Message;
import de.jonahd345.simpleplotrating.util.FileUtil;
import de.jonahd345.simpleplotrating.util.StringUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageService {
    private SimplePlotRating plugin;

    private File file;

    private FileConfiguration yamlConfiguration;

    public MessageService(SimplePlotRating plugin) {
        this.plugin = plugin;
        this.file = new File("plugins/" + this.plugin.getName() + "/messages.yml");
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.file);
    }

    public void loadMessages() {
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.file);
        boolean hasFileChanges = false;

        for (Message message : Message.values()) {
            if (!(this.file.exists()) || this.yamlConfiguration.getString(message.name().toLowerCase()) == null) {
                this.yamlConfiguration.set(message.name().toLowerCase(), message.getDefaultMessage());
                hasFileChanges = true;
                // set Message's message to his default message and skip the next line, because by new mess yamlConfiguration.getString is null
                message.setMessage(StringUtil.translateColorCodes(message.getDefaultMessage()));
                continue;
            }
            message.setMessage(StringUtil.translateColorCodes(this.yamlConfiguration.getString(message.name().toLowerCase())));
        }
        if (hasFileChanges) {
            FileUtil.saveFile(this.yamlConfiguration, this.file);
        }
    }
}
