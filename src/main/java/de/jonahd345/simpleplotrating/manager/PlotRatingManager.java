package de.jonahd345.simpleplotrating.manager;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.plot.Plot;
import de.jonahd345.simpleplotrating.model.RatingMaterial;
import de.jonahd345.simpleplotrating.config.SignText;
import de.jonahd345.simpleplotrating.util.LocationConverter;
import de.jonahd345.simpleplotrating.util.StringUtil;
import lombok.Getter;
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
    /**
     * Current PlotAPI instance.
     */
    @Getter
    private PlotAPI plotApi;

    /**
     * Constructor for PlotRatingManager.
     * Initializes the PlotAPI instance.
     */
    public PlotRatingManager() {
        this.plotApi = new PlotAPI();
    }

    /**
     * Sets the rating for a plot by placing blocks and a sign with the rating information.
     *
     * @param plot the plot to be rated
     * @param rating the rating value
     * @param blocks the list of materials to be placed
     * @param player the player who is rating the plot
     */
    public void setPlotRating(Plot plot, int rating, List<Material> blocks, Player player) {
        CompletableFuture<com.plotsquared.core.location.Location> future = new CompletableFuture<>();

        plot.getDefaultHome(future::complete);
        com.plotsquared.core.location.Location entrance = future.join();

        if (entrance != null) {
            Location center = LocationConverter.toBukkitLocation(entrance).clone().add(6, 0, -1);
            World world = center.getWorld();

            if (world != null) {
                // make sure that nothing has been set on the block
                while (world.getBlockAt(center).getType() != Material.AIR) {
                    center.add(0, 1, 0);
                }
                // set blocks
                if (blocks.size() == 3) {
                    world.getBlockAt(center.clone().add(-1, 0, 0)).setType(blocks.get(0));
                    world.getBlockAt(center).setType(blocks.get(1));
                    world.getBlockAt(center.clone().add(1, 0, 0)).setType(blocks.get(2));
                    // place sign
                    placeRatingSign(center, rating, player);
                }
            }
        }
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
