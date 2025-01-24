package de.jonahd345.simpleplotrating.listener;

import de.jonahd345.simpleplotrating.SimplePlotRating;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ConnectionListener implements Listener {
    private SimplePlotRating plugin;

    public ConnectionListener(SimplePlotRating plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // UPDATE MESSAGE
    }
}
