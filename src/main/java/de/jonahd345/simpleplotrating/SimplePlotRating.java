package de.jonahd345.simpleplotrating;

import de.jonahd345.simpleplotrating.command.PlotRatingCommand;
import de.jonahd345.simpleplotrating.listener.ConnectionListener;
import de.jonahd345.simpleplotrating.manager.PlotRatingManager;
import de.jonahd345.simpleplotrating.model.RatingCategory;
import de.jonahd345.simpleplotrating.service.MessageService;
import de.jonahd345.simpleplotrating.util.Metrics;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class SimplePlotRating extends JavaPlugin {
    @Getter
    private MessageService messageService;

    @Getter
    private PlotRatingManager plotRatingManager;

    @Getter
    private List<RatingCategory> ratingCategories;

    @Override
    public void onEnable() {
       Metrics metrics = new Metrics(this, 24480);

       this.messageService = new MessageService(this);
       this.messageService.loadMessages();

       this.plotRatingManager = new PlotRatingManager();

        this.ratingCategories = List.of(new RatingCategory("", Material.IRON_DOOR),new RatingCategory("", Material.CRAFTING_TABLE),
                new RatingCategory("", Material.DANDELION), new RatingCategory("", Material.NETHER_STAR),
                new RatingCategory("", Material.COMMAND_BLOCK, true));

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
