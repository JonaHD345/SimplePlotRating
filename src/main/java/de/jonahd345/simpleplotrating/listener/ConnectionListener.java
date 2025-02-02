package de.jonahd345.simpleplotrating.listener;

import de.jonahd345.simpleplotrating.SimplePlotRating;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Listener class for handling player connection events.
 */
public class ConnectionListener implements Listener {
    /**
     * Instance of the SimplePlotRating
     */
    private SimplePlotRating plugin;

    /**
     * Constructor for ConnectionListener.
     *
     * @param plugin the SimplePlotRating plugin instance
     */
    public ConnectionListener(SimplePlotRating plugin) {
        this.plugin = plugin;
    }

    /**
     * Event handler for player join events.
     *
     * @param event the player join event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (this.plugin.getUpdateService().isUpdateAvailable()) {
            if (player.hasPermission("simpleplotrating.admin")) {
                player.sendMessage("§7A new Version from §a§lSIMPLEPLOTRATING §7v§a" +
                        this.plugin.getUpdateService().getSpigotVersion().replace(".", "§7.§a") +
                        " §7is available at§8: §2https://www.spigotmc.org/resources/simpleplotrating.122131/");
            }
        }
    }
}
