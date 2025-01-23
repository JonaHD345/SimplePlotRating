package de.jonahd345.simpleplotrating.command;

import com.plotsquared.core.location.Location;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import de.jonahd345.simpleplotrating.SimplePlotRating;
import de.jonahd345.simpleplotrating.gui.RatingGui;
import de.jonahd345.simpleplotrating.util.LocationConverter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class PlotRateCommand implements CommandExecutor {
    private SimplePlotRating plugin;

    public PlotRateCommand(SimplePlotRating plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("");
            return true;
        }
        Player player = (Player) sender;
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

        if (args.length != 1) {
            player.sendMessage("");
            return true;
        }
        RatingGui ratingGui = new RatingGui(this.plugin);

        ratingGui.showGui(player);
        int rating = ratingGui.getRating();

        if (rating < 0 || rating > 25) {
            return true;
        }
        CompletableFuture<Location> future = new CompletableFuture<>();

        plot.getDefaultHome(future::complete);
        Location entrance = future.join();

        if (entrance != null) {
            this.plugin.getPlotManager().setPlotRating(LocationConverter.toBukkitLocation(entrance), rating, player);
        } else {
            player.sendMessage("");
        }
        player.sendMessage("");
        return true;
    }
}
