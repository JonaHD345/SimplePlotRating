package de.jonahd345.simpleplotrating;

import de.jonahd345.simpleplotrating.manager.PlotManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimplePlotRating extends JavaPlugin {
    @Getter
    private PlotManager plotManager;

    @Override
    public void onEnable() {
       plotManager = new PlotManager();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
