package de.jonahd345.simpleplotrating.service;

import de.jonahd345.simpleplotrating.SimplePlotRating;
import de.jonahd345.simpleplotrating.config.Config;
import de.jonahd345.xenfororesourcemanagerapi.XenforoResourceManagerAPI;
import de.jonahd345.xenfororesourcemanagerapi.model.Resource;
import lombok.Getter;

import java.util.Arrays;

/**
 * Service class for handling update checks for the SimplePlotRating plugin.
 */
public class UpdateService {
    private SimplePlotRating plugin;

    private final XenforoResourceManagerAPI xenforoResourceManagerAPI;

    private String  pluginVersion;

    @Getter
    private String spigotVersion;

    @Getter
    private boolean updateAvailable;

    /**
     * Constructor for UpdateService.
     *
     * @param plugin the SimplePlotRating plugin instance
     */
    public UpdateService(SimplePlotRating plugin) {
        this.plugin = plugin;
        this.xenforoResourceManagerAPI = new XenforoResourceManagerAPI();
        this.pluginVersion = this.plugin.getDescription().getVersion();
        this.updateAvailable = false;
        this.checkForUpdate();
    }

    /**
     * Checks for updates by querying the Spigot MC API.
     */
    public void checkForUpdate() {
        Resource resource = this.xenforoResourceManagerAPI.getResource(122131);

        if (resource != null) {
            this.spigotVersion = resource.getCurrentVersion();
        } else {
            this.spigotVersion = this.pluginVersion;
        }
        if (this.spigotVersion != null && !this.spigotVersion.isEmpty()) {
            this.updateAvailable = this.spigotIsNewer();
            if (this.updateAvailable && Config.UPDATE_NOTIFICATION.getValueAsBoolean()) {
                this.plugin.getLogger().info("The new Version from SimplePlotRating v" +
                        this.spigotVersion + " is available at: https://www.spigotmc.org/resources/simpleplotrating.122131/");
            }
        }
    }

    /**
     * Compares the current plugin version with the latest Spigot version.
     *
     * @return true if the Spigot version is newer, false otherwise
     */
    private boolean spigotIsNewer() {
        if (this.spigotVersion != null && !this.spigotVersion.isEmpty()) {
            int[] plV = this.toReadable(this.pluginVersion);
            int[] spV = this.toReadable(this.spigotVersion);
            if (plV[0] < spV[0]) {
                return true;
            } else {
                return (plV[1] < spV[1]);
            }
        } else {
            return false;
        }
    }

    /**
     * Converts a version string to an array of integers for comparison.
     *
     * @param version the version string
     * @return an array of integers representing the version
     */
    private int[] toReadable(String version) {
        return Arrays.stream(version.split("\\.")).mapToInt(Integer::parseInt).toArray();
    }
}
