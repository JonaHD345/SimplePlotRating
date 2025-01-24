package de.jonahd345.simpleplotrating.listener;

import de.jonahd345.simpleplotrating.SimplePlotRating;
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
        // UPDATE MESSAGE
    }
}
