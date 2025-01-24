package de.jonahd345.simpleplotrating;

import de.jonahd345.simpleplotrating.command.PlotRatingCommand;
import de.jonahd345.simpleplotrating.listener.ConnectionListener;
import de.jonahd345.simpleplotrating.manager.PlotRatingManager;
import de.jonahd345.simpleplotrating.service.GuiTextService;
import de.jonahd345.simpleplotrating.service.MessageService;
import de.jonahd345.simpleplotrating.service.SignTextService;
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
    private SignTextService signTextService;

    @Getter
    private PlotRatingManager plotRatingManager;

    @Override
    public void onEnable() {
       Metrics metrics = new Metrics(this, 24480);

       this.messageService = new MessageService(this);
       this.messageService.loadMessages();
       this.guiTextService = new GuiTextService(this);
       this.guiTextService.loadTexts();
       this.signTextService = new SignTextService(this);
       this.signTextService.loadTexts();

       this.plotRatingManager = new PlotRatingManager();

       this.init();

       getLogger().info("\n   _____ _                 _      _____  _       _   _____       _   _             \n" +
               "  / ____(_)               | |    |  __ \\| |     | | |  __ \\     | | (_)            \n" +
               " | (___  _ _ __ ___  _ __ | | ___| |__) | | ___ | |_| |__) |__ _| |_ _ _ __   __ _ \n" +
               "  \\___ \\| | '_ ` _ \\| '_ \\| |/ _ \\  ___/| |/ _ \\| __|  _  // _` | __| | '_ \\ / _` |\n" +
               "  ____) | | | | | | | |_) | |  __/ |    | | (_) | |_| | \\ \\ (_| | |_| | | | | (_| |\n" +
               " |_____/|_|_| |_| |_| .__/|_|\\___|_|    |_|\\___/ \\__|_|  \\_\\__,_|\\__|_|_| |_|\\__, |\n" +
               "                    | |                                                       __/ |\n" +
               "                    |_|                                                      |___/ \n\n" +
               "v" + getDescription().getVersion() + " by JonaHD345 (https://jonahd345.de) has been enabled!");
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
