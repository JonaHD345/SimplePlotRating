package de.jonahd345.simpleplotrating.command;

import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import de.jonahd345.simpleplotrating.SimplePlotRating;
import de.jonahd345.simpleplotrating.gui.RatingGui;
import de.jonahd345.simpleplotrating.util.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This class handles the /plotrating command.
 * It implements CommandExecutor to handle command execution.
 */
public class PlotRatingCommand implements CommandExecutor {
    /**
     * Instance of the SimplePlotRating
     */
    private SimplePlotRating plugin;

    /**
     * Constructor to initialize the plugin instance.
     *
     * @param plugin the SimplePlotRating plugin instance
     */
    public PlotRatingCommand(SimplePlotRating plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the execution of the /plotrating command.
     *
     * @param sender  the sender of the command
     * @param command the command that was executed
     * @param s       the alias of the command
     * @param args    the arguments passed to the command
     * @return true if the command was handled successfully, false otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Message.getMessageWithPrefix(Message.NO_PLAYER));
            return true;
        }
        Player player = (Player) sender;

        if(!(player.hasPermission("simpleplotrating.command.plotrating") || player.hasPermission("simpleplotrating.admin"))) {
            player.sendMessage(Message.getMessageWithPrefix(Message.NO_PERMISSION));
            return true;
        }
        PlotPlayer<?> plotPlayer = this.plugin.getPlotRatingManager().getPlotApi().wrapPlayer(player.getUniqueId());

        if (plotPlayer != null) {
            Plot plot = plotPlayer.getCurrentPlot();

            if (plot == null) {
                player.sendMessage(Message.getMessageWithPrefix(Message.NO_PLOT));
                return true;
            }
            if (!plot.hasOwner()) {
                player.sendMessage(Message.getMessageWithPrefix(Message.NO_OWNER));
                return true;
            }
            new RatingGui(this.plugin, player, plot).showGui();
            player.sendMessage(Message.getMessageWithPrefix(Message.OPEN_GUI));
        }
        return true;
    }
}
