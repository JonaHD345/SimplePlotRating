package de.jonahd345.simpleplotrating.model;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public enum RatingMaterial {
    DRAGON_EGG(Material.DRAGON_EGG, 8.33),      // 25/3
    BEACON(Material.BEACON, 6.66),              // 20/3
    DIAMOND_BLOCK(Material.DIAMOND_BLOCK, 5),   // 15/3
    IRON_BLOCK(Material.IRON_BLOCK, 3.33),      // 10/3
    DIRT(Material.DIRT, 1.66);                  //  5/3

    private final Material material;
    private final double weight;

    RatingMaterial(Material material, double weight) {
        this.material = material;
        this.weight = weight;
    }

    public static RatingMaterial findBestMaterial(double rating) {
        for (RatingMaterial ratingMaterial : values()) {
            if (rating >= ratingMaterial.getWeight()) {
                return ratingMaterial;
            }
        }
        return DIRT;
    }
}
