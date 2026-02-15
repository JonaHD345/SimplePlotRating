package de.jonahd345.simpleplotrating.listener;

import com.google.common.eventbus.Subscribe;
import com.plotsquared.core.events.post.PostPlotDeleteEvent;
import de.jonahd345.simpleplotrating.SimplePlotRating;
import de.jonahd345.simpleplotrating.config.Config;

public class PlotListener {
    private SimplePlotRating plugin;

    public PlotListener(SimplePlotRating plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPostPlotDelete(PostPlotDeleteEvent e) {
        // Reset rating of the plot if the config option is enabled
        if (Config.DELETE_RATING_ON_PLOT_DELETION.getValueAsBoolean()) {
            this.plugin.getPlotRatingManager().resetPlotRating(e.getPlot());
        }
    }
}
