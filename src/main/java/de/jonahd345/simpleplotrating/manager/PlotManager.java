package de.jonahd345.simpleplotrating.manager;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.plot.Plot;
import de.jonahd345.simpleplotrating.model.RatingMaterial;
import de.jonahd345.simpleplotrating.util.LocationConverter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PlotManager {
    @Getter
    private PlotAPI plotApi;

    public PlotManager() {
        this.plotApi = new PlotAPI();
    }

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

    private void placeRatingSign(Location blockLocation, int rating, Player player) {
        World world = blockLocation.getWorld();

        if (world != null) {
            BlockFace facing = BlockFace.NORTH;
            Location signLocation = blockLocation.clone().add(facing.getModX(), facing.getModY(), facing.getModZ());
            Block signBlock = world.getBlockAt(signLocation);

            signBlock.setType(Material.OAK_WALL_SIGN);
            if (signBlock.getState() instanceof Sign) {
                Sign sign = (Sign) signBlock.getState();

                sign.getSide(Side.FRONT).setLine(0, "");
                sign.getSide(Side.FRONT).setLine(1, "");
                sign.getSide(Side.FRONT).setLine(2, "");
                sign.getSide(Side.FRONT).setLine(3, "");
                sign.update();
            }
        }
    }

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
}
