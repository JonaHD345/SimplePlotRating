package de.jonahd345.simpleplotrating.manager;

import com.plotsquared.core.PlotAPI;
import de.jonahd345.simpleplotrating.model.RatingMaterial;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlotManager {
    @Getter
    private PlotAPI plotApi;

    public PlotManager() {
        this.plotApi = new PlotAPI();
    }

    public void setPlotRating(Location entrance, int rating, Player player) {
        List<Material> blocks = calculateBlocks(rating);

        Location center = entrance.clone().add(6, 0, 1); // 4 Blöcke vom Eingang entfernt
        World world = center.getWorld();

        // set blocks
        if (world != null && blocks.size() == 3) {
            world.getBlockAt(center.clone().add(-1, 0, 0)).setType(blocks.get(0));
            world.getBlockAt(center).setType(blocks.get(1));
            world.getBlockAt(center.clone().add(1, 0, 0)).setType(blocks.get(2));
            // place sign
            placeRatingSign(center, rating, player);
        }
    }

    private List<Material> calculateBlocks(int rating) {
        List<Material> result = new ArrayList<>();
        int remainingRating = rating;

        for (int i = 0; i < 3; i++) {
            for (RatingMaterial weight : RatingMaterial.values()) {
                if (i == 0) {
                    result.set(1, weight.getMaterial());
                } else {
                    result.set((i != 1 ? i : i++), weight.getMaterial());
                }
                remainingRating -= weight.getWeight();
            }
        }
        return result;
    }

    private void placeRatingSign(Location middle, int rating, Player player) {
        Location signLocation = middle.clone().add(0, 1, 0);
        World world = signLocation.getWorld();

        if (world != null) {
            Block blockAbove = world.getBlockAt(signLocation);
            blockAbove.setType(Material.OAK_SIGN);

            if (blockAbove.getState() instanceof Sign) {
                Sign sign = (Sign) blockAbove.getState();
                sign.getSide(Side.FRONT).setLine(0, "");
                sign.getSide(Side.FRONT).setLine(1, "");
                sign.getSide(Side.FRONT).setLine(2, "");
                sign.getSide(Side.FRONT).setLine(3, "");
                sign.update();
            }
        }
    }
}
