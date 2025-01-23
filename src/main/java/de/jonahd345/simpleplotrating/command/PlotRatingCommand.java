package de.jonahd345.simpleplotrating.command;

import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import de.jonahd345.simpleplotrating.SimplePlotRating;
import de.jonahd345.simpleplotrating.gui.RatingGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlotRatingCommand implements CommandExecutor {
    private SimplePlotRating plugin;

    public PlotRatingCommand(SimplePlotRating plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("");
            return true;
        }
        Player player = (Player) sender;

        if(!(player.hasPermission(""))) {
            player.sendMessage("");
            return true;
        }
        PlotPlayer<?> plotPlayer = this.plugin.getPlotManager().getPlotApi().wrapPlayer(player.getUniqueId());

        if (plotPlayer == null) {
            player.sendMessage("");
            return true;
        }
        Plot plot = plotPlayer.getCurrentPlot();

        if (plot == null) {
            player.sendMessage("");
            return true;
        }
        if (!plot.hasOwner()) {
            player.sendMessage("");
            return true;
        }
        new RatingGui(this.plugin, player, plot).showGui();
        player.sendMessage("");
        return true;
    }
}
