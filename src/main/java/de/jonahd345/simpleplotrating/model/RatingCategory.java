package de.jonahd345.simpleplotrating.model;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public enum RatingCategory {
    CELLAR("Keller", Material.IRON_DOOR),
    INTERIOR_DESIGN("Inneneinrichtung", Material.CRAFTING_TABLE),
    GARDEN("Garten", Material.DANDELION),
    EXTERIOR_VIEW("Außenansicht", Material.DANDELION),
    CREATIVITY("Kreativität", Material.COMMAND_BLOCK, true);

    private final String name;
    private final Material material;
    private final boolean isLastOne;

    RatingCategory(String name, Material material, boolean isLastOne) {
        this.name = name;
        this.material = material;
        this.isLastOne = isLastOne;
    }

    RatingCategory(String name, Material material) {
        this(name, material, false);
    }
}