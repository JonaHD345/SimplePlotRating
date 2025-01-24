package de.jonahd345.simpleplotrating.util;
import com.plotsquared.core.location.Location;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class LocationConverter {
    /**
     * Converts a PlotSquared location to a Bukkit location using LocationFetcher.
     *
     * @param plotSquaredLocation the PlotSquared location to convert
     * @return the equivalent Bukkit Location, or null if the world is not found
     */
    public static org.bukkit.Location toBukkitLocation(Location plotSquaredLocation) {
        String worldName = plotSquaredLocation.getWorldName();

        // Fetch the Bukkit world
        World bukkitWorld = Bukkit.getWorld(worldName);

        if (bukkitWorld == null) {
            return null; // Handle cases where the world is not loaded
        }

        // Create and return the Bukkit location
        return new org.bukkit.Location(
                bukkitWorld,
                plotSquaredLocation.getX(),
                plotSquaredLocation.getY(),
                plotSquaredLocation.getZ(),
                plotSquaredLocation.getYaw(),
                plotSquaredLocation.getPitch()
        );
    }
}

