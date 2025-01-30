package de.jonahd345.simpleplotrating.command;

import de.jonahd345.simpleplotrating.SimplePlotRating;
import de.jonahd345.simpleplotrating.util.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the /plotratingadmin command and its tab completion.
 * It implements CommandExecutor to handle command execution and TabCompleter to provide tab completion suggestions.
 */
public class PlotRatingAdminCommand implements CommandExecutor, TabCompleter {
    /**
     * Instance of the SimplePlotRating
     */
    private SimplePlotRating plugin;

    /**
     * Constructor to initialize the plugin instance.
     *
     * @param plugin the SimplePlotRating plugin instance
     */
    public PlotRatingAdminCommand(SimplePlotRating plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the execution of the /plotratingadmin command.
     *
     * @param sender  the sender of the command
     * @param command the command that was executed
     * @param s       the alias of the command
     * @param args    the arguments passed to the command
     * @return true if the command was handled successfully, false otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender.hasPermission("simpleplotrating.command.plotratingadmin"))) {
            sender.sendMessage(Message.getMessageWithPrefix(Message.NO_PERMISSION));
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                this.plugin.getConfigService().loadConfig();
                sender.sendMessage(Message.PREFIX.getMessage() + "§7Config reloaded!");
            }
        } else {
            sender.sendMessage(Message.PREFIX.getMessage()  + "§cUsage: /plotratingadmin reload");
        }
        return false;
    }

    /**
     * Provides tab completion suggestions for the /plotratingadmin command.
     *
     * @param sender  the sender of the command
     * @param command the command that was executed
     * @param s       the alias of the command
     * @param args    the arguments passed to the command
     * @return a list of tab completion suggestions
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> subcommand = new ArrayList<>();
        if (sender.hasPermission("simpleplotrating.command.plotratingadmin") || sender.hasPermission("simpleplotrating.admin")) {
            if (args.length == 1) {
                subcommand.add("reload");
            }
        }
        ArrayList<String> cl = new ArrayList<>();
        String currentarg = args[args.length - 1].toLowerCase();

        for(String s1 : subcommand) {
            String s2 = s1.toLowerCase();
            if(s2.startsWith(currentarg)) {
                cl.add(s1);
            }
        }
        return cl;
    }
}
