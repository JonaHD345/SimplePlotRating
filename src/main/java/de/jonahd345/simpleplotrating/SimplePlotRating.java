package de.jonahd345.simpleplotrating;

import de.jonahd345.simpleplotrating.command.PlotRatingCommand;
import de.jonahd345.simpleplotrating.manager.PlotManager;
import de.jonahd345.simpleplotrating.model.RatingCategory;
import de.jonahd345.simpleplotrating.model.RatingCategoryName;
import de.jonahd345.simpleplotrating.util.Metrics;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public final class SimplePlotRating extends JavaPlugin {
    @Getter
    private PlotManager plotManager;

    @Getter
    private Map<RatingCategoryName, RatingCategory> ratingCategories;

    @Override
    public void onEnable() {
       Metrics metrics = new Metrics(this, 24480);

       this.plotManager = new PlotManager();

       this.ratingCategories = Map.of(
               RatingCategoryName.CELLAR, new RatingCategory("Keller", Material.IRON_DOOR),
               RatingCategoryName.INTERIOR_DESIGN, new RatingCategory("Inneneinrichtung", Material.CRAFTING_TABLE),
               RatingCategoryName.GARDEN, new RatingCategory("Garten", Material.DANDELION),
               RatingCategoryName.EXTERIOR_VIEW, new RatingCategory("Außenansicht", Material.DANDELION),
               RatingCategoryName.CREATIVITY, new RatingCategory("Kreativität", Material.COMMAND_BLOCK, true)
       );

       this.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void init() {
        PluginManager pluginManager = getServer().getPluginManager();

        getCommand("plotrating").setExecutor(new PlotRatingCommand(this));
    }

    public static SimplePlotRating getInstance() {
        return getPlugin(SimplePlotRating.class);
    }
}
