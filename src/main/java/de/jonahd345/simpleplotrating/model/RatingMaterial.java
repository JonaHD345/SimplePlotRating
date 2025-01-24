package de.jonahd345.simpleplotrating.model;

import lombok.Getter;
import org.bukkit.Material;

/**
 * Enum representing different materials and their associated weights for rating purposes.
 */
@Getter
public enum RatingMaterial {
    DRAGON_EGG(Material.DRAGON_EGG, 8.33),      // 25/3
    BEACON(Material.BEACON, 6.66),              // 20/3
    DIAMOND_BLOCK(Material.DIAMOND_BLOCK, 5),   // 15/3
    IRON_BLOCK(Material.IRON_BLOCK, 3.33),      // 10/3
    DIRT(Material.DIRT, 1.66);                  //  5/3

    /**
     * The material associated with the rating.
     */
    private final Material material;
    /**
     * The weight of the material used for rating.
     */
    private final double weight;

    /**
     * Constructor for RatingMaterial enum.
     *
     * @param material the material associated with the rating
     * @param weight the weight of the material used for rating
     */
    RatingMaterial(Material material, double weight) {
        this.material = material;
        this.weight = weight;
    }

    /**
     * Finds the best material based on the given rating.
     *
     * @param rating the rating value
     * @return the best matching RatingMaterial
     */
    public static RatingMaterial findBestMaterial(double rating) {
        for (RatingMaterial ratingMaterial : values()) {
            if (rating >= ratingMaterial.getWeight()) {
                return ratingMaterial;
            }
        }
        return DIRT;
    }
}
