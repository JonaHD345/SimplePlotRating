package de.jonahd345.simpleplotrating.command;

import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import de.jonahd345.simpleplotrating.SimplePlotRating;
import de.jonahd345.simpleplotrating.gui.RatingGui;
import de.jonahd345.simpleplotrating.model.Message;
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
            sender.sendMessage(this.plugin.getMessageService().getMessageWithPrefix(Message.NO_PLAYER));
            return true;
        }
        Player player = (Player) sender;

        if(!(player.hasPermission("simpleplotrating.command.plotrating"))) {
            player.sendMessage(this.plugin.getMessageService().getMessageWithPrefix(Message.NO_PERMISSION));
            return true;
        }
        PlotPlayer<?> plotPlayer = this.plugin.getPlotRatingManager().getPlotApi().wrapPlayer(player.getUniqueId());

        if (plotPlayer != null) {
            Plot plot = plotPlayer.getCurrentPlot();

            if (plot == null) {
                player.sendMessage(this.plugin.getMessageService().getMessageWithPrefix(Message.NO_PLOT));
                return true;
            }
            if (!plot.hasOwner()) {
                player.sendMessage(this.plugin.getMessageService().getMessageWithPrefix(Message.NO_OWNER));
                return true;
            }
            new RatingGui(this.plugin, player, plot).showGui();
            player.sendMessage(this.plugin.getMessageService().getMessageWithPrefix(Message.OPEN_GUI));
        }
        return true;
    }
}
