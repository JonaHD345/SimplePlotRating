package de.jonahd345.simpleplotrating.manager;

import com.plotsquared.core.generator.ClassicPlotWorld;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import de.jonahd345.simpleplotrating.SimplePlotRating;
import de.jonahd345.simpleplotrating.model.RatingMaterial;
import de.jonahd345.simpleplotrating.config.SignText;
import de.jonahd345.simpleplotrating.util.LocationConverter;
import de.jonahd345.simpleplotrating.util.StringUtil;
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
     * Resets (removes) the visual rating of a plot by deleting the 3 rating blocks
     * and the rating sign that were placed via {@link #setPlotRating(Plot, int, List, Player)}.
     *
     * <p>How the location is resolved:</p>
     * <ul>
     *   <li>We compute the same base position (plot home + offset).</li>
     *   <li>We align Y to the PlotSquared road height.</li>
     *   <li>We first try the most likely placement layer (roadY + 1).</li>
     *   <li>If not found, we scan upwards until we find a 3-block row that matches rating materials.</li>
     * </ul>
     *
     * <p>Important:</p>
     * <ul>
     *   <li>We do NOT block the server thread: PlotSquared home lookup uses a callback.</li>
     *   <li>All Bukkit world edits are executed on the main thread.</li>
     *   <li>We remove the sign first to avoid potential drops/physics when the supporting blocks disappear.</li>
     * </ul>
     *
     * @param plot the plot whose rating display should be removed
     */
    public void resetPlotRating(Plot plot) {
        if (plot == null) {
            return;
        }

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

                // Locate the rating "middle" block (the center of the 3-block row).
                Location middle = findRatingRowMiddle(base, world);
                if (middle == null) {
                    return; // nothing to reset
                }

                // Sign is placed NORTH of the middle block in setPlotRating()
                BlockFace facing = BlockFace.NORTH;
                Location signLoc = middle.clone().add(facing.getModX(), facing.getModY(), facing.getModZ());
                Block signBlock = world.getBlockAt(signLoc);

                // Remove sign first (prevents drops/odd updates if blocks behind change)
                if (signBlock.getState() instanceof Sign) {
                    setTypeNoPhysics(signBlock, Material.AIR);
                }

                // Remove the 3 rating blocks (left, middle, right)
                setTypeNoPhysics(world.getBlockAt(middle.clone().add(-1, 0, 0)), Material.AIR);
                setTypeNoPhysics(world.getBlockAt(middle), Material.AIR);
                setTypeNoPhysics(world.getBlockAt(middle.clone().add(1, 0, 0)), Material.AIR);
            });
        });
    }

    /**
     * Tries to find the center block ("middle") of the 3-block rating row.
     *
     * <p>First attempt: roadY + 1 (typical "first air above road" when originally placed).</p>
     * <p>Fallback: scans upward to find a row of three blocks that are all rating materials.</p>
     *
     * @param baseAtRoadY base location with Y already aligned to road height
     * @param world       the world
     * @return location of the middle block of the rating row, or null if not found
     */
    private Location findRatingRowMiddle(Location baseAtRoadY, World world) {
        // Build a set of all materials that can be used as rating blocks
        Set<Material> ratingMaterials = new HashSet<>();
        for (RatingMaterial rm : RatingMaterial.values()) {
            ratingMaterials.add(rm.getMaterial());
        }

        int x = baseAtRoadY.getBlockX();
        int z = baseAtRoadY.getBlockZ();

        int minY = world.getMinHeight();
        int maxY = world.getMaxHeight() - 1;

        // 1) Most likely placement layer: one above road height
        int candidateY = clampYToWorld(world, baseAtRoadY.getBlockY() + 1);
        if (isRatingRowAt(world, x, candidateY, z, ratingMaterials)) {
            return new Location(world, x, candidateY, z);
        }

        // 2) Fallback scan upwards (starting at roadY up to world max height)
        int startY = Math.max(minY, baseAtRoadY.getBlockY());
        for (int y = startY; y <= maxY; y++) {
            if (isRatingRowAt(world, x, y, z, ratingMaterials)) {
                return new Location(world, x, y, z);
            }
        }

        return null;
    }

    /**
     * Checks whether at (x,y,z) there is a "rating row":
     * three blocks in a line (x-1,y,z), (x,y,z), (x+1,y,z) and all are rating materials.
     *
     * @param world           the world
     * @param x               center x
     * @param y               y
     * @param z               z
     * @param ratingMaterials allowed rating materials
     * @return true if the pattern matches
     */
    private boolean isRatingRowAt(World world, int x, int y, int z, Set<Material> ratingMaterials) {
        Material left = world.getBlockAt(x - 1, y, z).getType();
        Material mid  = world.getBlockAt(x,     y, z).getType();
        Material right= world.getBlockAt(x + 1, y, z).getType();

        // We only treat it as our rating display if all three blocks are known rating materials
        return ratingMaterials.contains(left)
                && ratingMaterials.contains(mid)
                && ratingMaterials.contains(right);
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
