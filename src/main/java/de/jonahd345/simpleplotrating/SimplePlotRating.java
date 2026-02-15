package de.jonahd345.simpleplotrating;

import com.plotsquared.core.PlotAPI;
import de.jonahd345.simpleplotrating.command.PlotRatingAdminCommand;
import de.jonahd345.simpleplotrating.command.PlotRatingCommand;
import de.jonahd345.simpleplotrating.listener.ConnectionListener;
import de.jonahd345.simpleplotrating.listener.PlotListener;
import de.jonahd345.simpleplotrating.manager.PlotRatingManager;
import de.jonahd345.simpleplotrating.service.ConfigService;
import de.jonahd345.simpleplotrating.service.UpdateService;
import de.jonahd345.simpleplotrating.util.Metrics;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class SimplePlotRating extends JavaPlugin {
    @Getter
    private BukkitAudiences adventure;

    @Getter
    private PlotAPI plotApi;

    @Getter
    private UpdateService updateService;

    @Getter
    private ConfigService configService;

    @Getter
    private PlotRatingManager plotRatingManager;

    @Override
    public void onEnable() {
       if (getServer().getPluginManager().getPlugin("PlotSquared") == null) {
           getLogger().severe("PlotSquared is not installed! This plugin is required for SimplePlotRating to work!");
           getServer().getPluginManager().disablePlugin(this);
           return;
       }
       this.adventure = BukkitAudiences.create(this);

       this.plotApi = new PlotAPI();

       Metrics metrics = new Metrics(this, 24480);

       this.updateService = new UpdateService(this);
       this.configService = new ConfigService(this);
       this.configService.loadConfig();

       this.plotRatingManager = new PlotRatingManager(this);

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
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    private void init() {
        PluginManager pluginManager = getServer().getPluginManager();

        // Listeners
        pluginManager.registerEvents(new ConnectionListener(this), this);
        // PS
        this.plotApi.registerListener(new PlotListener(this));

        // Commands
        getCommand("plotrating").setExecutor(new PlotRatingCommand(this));
        getCommand("plotratingadmin").setExecutor(new PlotRatingAdminCommand(this));
    }

    public static SimplePlotRating getInstance() {
        return getPlugin(SimplePlotRating.class);
    }
}
