package de.jonahd345.simpleplotrating.manager;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.generator.ClassicPlotWorld;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import de.jonahd345.simpleplotrating.SimplePlotRating;
import de.jonahd345.simpleplotrating.model.RatingMaterial;
import de.jonahd345.simpleplotrating.config.SignText;
import de.jonahd345.simpleplotrating.util.LocationConverter;
import de.jonahd345.simpleplotrating.util.StringUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Manager class for handling plot ratings in the SimplePlotRating plugin.
 */
public class PlotRatingManager {
    private SimplePlotRating plugin;

    /**
     * Constructor for PlotRatingManager.
     * Initializes the PlotAPI instance.
     */
    public PlotRatingManager(SimplePlotRating plugin) {
        this.plugin = plugin;
    }

    /**
     * Sets the rating for a plot by placing blocks and a sign with the rating information.
     *
     * The Y level of the base position is aligned to the PlotSquared road height (street height).
     * The blocks/sign are then placed at the first air block above that road height.
     *
     * @param plot   the plot to be rated
     * @param rating the rating value
     * @param blocks the list of materials to be placed (must contain exactly 3 materials)
     * @param player the player who is rating the plot
     */
    public void setPlotRating(Plot plot, int rating, List<Material> blocks, Player player) {
        if (plot == null || player == null || blocks == null || blocks.size() != 3) {
            return;
        }

        // Do NOT block the server thread: continue inside the callback
        plot.getDefaultHome(home -> {
            if (home == null) {
                return;
            }

            // Bukkit world/block edits must happen on the main thread
            Bukkit.getScheduler().runTask(plugin, () -> {
                Location base = LocationConverter.toBukkitLocation(home).add(6, 0, -1);
                World world = base.getWorld();
                if (world == null) {
                    return;
                }

                // Align base Y to PlotSquared road (street) height
                int roadY = getRoadHeightY(plot, base.getBlockY());
                roadY = clampYToWorld(world, roadY);
                base.setY(roadY);

                // Find the first air block above the road height (so we don't overwrite the road)
                Location placeAt = findFirstAirAbove(base, world);
                if (placeAt == null) {
                    return;
                }

                // Place 3 rating blocks centered at 'placeAt'
                setTypeNoPhysics(placeAt.clone().add(-1, 0, 0).getBlock(), blocks.get(0));
                setTypeNoPhysics(placeAt.getBlock(), blocks.get(1));
                setTypeNoPhysics(placeAt.clone().add(1, 0, 0).getBlock(), blocks.get(2));

                // Place the sign with rating info
                placeRatingSign(placeAt, rating, player);
            });
        });
    }

    /**
     * Returns the PlotSquared road (street) height as an absolute Y level.
     *
     * If the plot area is a classic/hybrid plot world, PlotSquared exposes the road height via ClassicPlotWorld.ROAD_HEIGHT.
     * Otherwise, we fall back to the provided fallbackY.
     *
     * @param plot      the plot
     * @param fallbackY fallback Y level if the plot area doesn't expose a road height
     * @return absolute road height Y
     */
    private int getRoadHeightY(Plot plot, int fallbackY) {
        PlotArea area = plot.getArea();
        if (area instanceof ClassicPlotWorld) {
            return ((ClassicPlotWorld) area).ROAD_HEIGHT;
        }
        return fallbackY;
    }

    /**
     * Finds the first air block at or above the given start location, with a hard cap at world max height.
     *
     * @param start start location (will not be modified)
     * @param world the world
     * @return a new location pointing to the first air block, or null if none found until max height
     */
    private Location findFirstAirAbove(Location start, World world) {
        Location loc = start.clone();
        int maxY = world.getMaxHeight() - 1;

        while (loc.getBlockY() < maxY && loc.getBlock().getType() != Material.AIR) {
            loc.add(0, 1, 0);
        }

        return (loc.getBlock().getType() == Material.AIR) ? loc : null;
    }

    /**
     * Sets a block type without physics updates (prevents unnecessary neighbor updates).
     *
     * @param block    target block
     * @param material material to set
     */
    private void setTypeNoPhysics(Block block, Material material) {
        if (block.getType() != material) {
            block.setType(material, false);
        }
    }

    /**
     * Clamps a Y value to the world min/max bounds.
     *
     * @param world the world
     * @param y     desired Y
     * @return clamped Y
     */
    private int clampYToWorld(World world, int y) {
        int min = world.getMinHeight();
        int max = world.getMaxHeight() - 1;
        return Math.max(min, Math.min(max, y));
    }

    /**
     * Calculates the list of materials to be used for the rating based on the rating value.
     *
     * @param rating the rating value
     * @return the list of materials
     */
    public List<Material> calculateBlocks(int rating) {
        List<Material> result = new ArrayList<>();
        double remainingRating = rating;

        for (int i = 0; i < 3; i++) {
            RatingMaterial bestMaterial = RatingMaterial.findBestMaterial(remainingRating / (3 - i));
            result.add(bestMaterial.getMaterial());
            remainingRating = remainingRating - bestMaterial.getWeight();
        }
        sortMaterialList(result);
        return result;
    }

    /**
     * Places a sign with the rating information at the specified location.
     *
     * @param blockLocation the location to place the sign
     * @param rating the rating value
     * @param player the player who is rating the plot
     */
    private void placeRatingSign(Location blockLocation, int rating, Player player) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String date = LocalDate.now().format(formatter);
        World world = blockLocation.getWorld();

        if (world != null) {
            BlockFace facing = BlockFace.NORTH;
            Location signLocation = blockLocation.clone().add(facing.getModX(), facing.getModY(), facing.getModZ());
            Block signBlock = world.getBlockAt(signLocation);

            signBlock.setType(Material.OAK_WALL_SIGN);
            if (signBlock.getState() instanceof Sign) {
                Sign sign = (Sign) signBlock.getState();

                sign.getSide(Side.FRONT).setLine(0, this.replaceSignPlaceholder(SignText.RATING_LINE_1.getText(), rating, player, date));
                sign.getSide(Side.FRONT).setLine(1, this.replaceSignPlaceholder(SignText.RATING_LINE_2.getText(), rating, player, date));
                sign.getSide(Side.FRONT).setLine(2, this.replaceSignPlaceholder(SignText.RATING_LINE_3.getText(), rating, player, date));
                sign.getSide(Side.FRONT).setLine(3, this.replaceSignPlaceholder(SignText.RATING_LINE_4.getText(), rating, player, date));
                sign.update();
            }
        }
    }

    /**
     * Sorts the list of materials, ensuring that the unique material is placed in the middle.
     *
     * @param materials the list of materials to be sorted
     */
    private void sortMaterialList(List<Material> materials) {
        Map<Material, Integer> materialCount = new HashMap<>();
        Material uniqueMaterial = null;

        for (Material material : materials) {
            materialCount.put(material, materialCount.getOrDefault(material, 0) + 1);
        }
        for (Map.Entry<Material, Integer> entry : materialCount.entrySet()) {
            if (entry.getValue() == 1) {
                uniqueMaterial = entry.getKey();
                break;
            }
        }
        if (uniqueMaterial == null) {
            return;
        }
        materials.remove(uniqueMaterial);
        materials.add(1, uniqueMaterial);
    }

    /**
     * Replaces placeholders in the sign text with actual values.
     *
     * @param text the text with placeholders
     * @param rating the rating value
     * @param player the player who is rating the plot
     * @param date the current date
     * @return the text with placeholders replaced
     */
    private String replaceSignPlaceholder(String text, int rating, Player player, String date) {
        return StringUtil.replacePlaceholder(text, Map.of("%rating%", String.valueOf(rating), "%rated_player%", player.getName(),
                "%date_day%", date.split("\\.")[0], "%date_month%", date.split("\\.")[1], "%date_year%", date.split("\\.")[2]));
    }
}
