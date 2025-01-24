package de.jonahd345.simpleplotrating;

import de.jonahd345.simpleplotrating.command.PlotRatingCommand;
import de.jonahd345.simpleplotrating.listener.ConnectionListener;
import de.jonahd345.simpleplotrating.manager.PlotRatingManager;
import de.jonahd345.simpleplotrating.service.GuiTextService;
import de.jonahd345.simpleplotrating.service.MessageService;
import de.jonahd345.simpleplotrating.util.Metrics;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class SimplePlotRating extends JavaPlugin {
    @Getter
    private MessageService messageService;

    @Getter
    private GuiTextService guiTextService;

    @Getter
    private PlotRatingManager plotRatingManager;

    @Override
    public void onEnable() {
       Metrics metrics = new Metrics(this, 24480);

       this.messageService = new MessageService(this);
       this.messageService.loadMessages();
       this.guiTextService = new GuiTextService(this);
       this.guiTextService.loadTexts();

       this.plotRatingManager = new PlotRatingManager();

       this.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void init() {
        PluginManager pluginManager = getServer().getPluginManager();

        // Listeners
        pluginManager.registerEvents(new ConnectionListener(this), this);
        // Commands
        getCommand("plotrating").setExecutor(new PlotRatingCommand(this));
    }

    public static SimplePlotRating getInstance() {
        return getPlugin(SimplePlotRating.class);
    }
}
