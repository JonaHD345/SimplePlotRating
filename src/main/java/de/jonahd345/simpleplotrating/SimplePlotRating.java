package de.jonahd345.simpleplotrating;

import de.jonahd345.simpleplotrating.command.PlotRateCommand;
import de.jonahd345.simpleplotrating.manager.PlotManager;
import de.jonahd345.simpleplotrating.util.Metrics;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimplePlotRating extends JavaPlugin {
    @Getter
    private PlotManager plotManager;

    @Override
    public void onEnable() {
       Metrics metrics = new Metrics(this, 24480);

       this.plotManager = new PlotManager();

       this.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void init() {
        PluginManager pluginManager = getServer().getPluginManager();

        getCommand("plotrate").setExecutor(new PlotRateCommand(this));
    }

    public static SimplePlotRating getInstance() {
        return getPlugin(SimplePlotRating.class);
    }
}
