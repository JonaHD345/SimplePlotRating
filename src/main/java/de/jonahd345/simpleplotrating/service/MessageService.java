package de.jonahd345.simpleplotrating.service;

import de.jonahd345.simpleplotrating.SimplePlotRating;
import de.jonahd345.simpleplotrating.model.Message;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

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
                message.setMessage(this.translateColorCodes(message.getDefaultMessage()));
                continue;
            }
            message.setMessage(this.translateColorCodes(this.yamlConfiguration.getString(message.name().toLowerCase())));
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
        return input == null ? null : ChatColor.translateAlternateColorCodes('&', input);
    }
}
