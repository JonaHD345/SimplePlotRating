package de.jonahd345.simpleplotrating.model;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public enum RatingMaterial {
    DRAGON_EGG(Material.DRAGON_EGG, 25),
    BEACON(Material.BEACON, 20),
    DIAMOND_BLOCK(Material.DIAMOND_BLOCK, 15),
    IRON_BLOCK(Material.IRON_BLOCK, 10),
    DIRT(Material.DIRT, 5);

    private final Material material;
    private final int weight;

    RatingMaterial(Material material, int weight) {
        this.material = material;
        this.weight = weight;
    }
}
