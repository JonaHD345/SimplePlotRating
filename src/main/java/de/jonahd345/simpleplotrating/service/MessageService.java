package de.jonahd345.simpleplotrating.service;

import de.jonahd345.simpleplotrating.SimplePlotRating;
import de.jonahd345.simpleplotrating.model.Message;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MessageService {
    private SimplePlotRating plugin;

    private File file;

    private FileConfiguration yamlConfiguration;

    @Getter
    private Map<Message, String> messages;

    public MessageService(SimplePlotRating plugin) {
        this.plugin = plugin;
        this.file = new File("plugins/" + this.plugin.getName() + "/messages.yml");
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.file);
        this.messages = new HashMap<>();
    }

    public void loadMessages() {
        this.messages.clear();
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.file);
        boolean hasFileChanges = false;

        for (Message message : Message.values()) {
            if (!(this.file.exists()) || this.yamlConfiguration.getString(message.name().toLowerCase()) == null) {
                this.yamlConfiguration.set(message.name(), message.getDefaultMessage());
                hasFileChanges = true;
            }
            this.messages.put(message, this.translateColorCodes(this.yamlConfiguration.getString(message.name().toLowerCase())));
        }
        if (hasFileChanges) {
            this.saveFile();
        }
    }

    public String getMessage(Message message) {
        return this.messages.get(message);
    }

    public String getMessageWithPrefix(Message message) {
        return this.messages.get(Message.PREFIX) + this.messages.get(message);
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
