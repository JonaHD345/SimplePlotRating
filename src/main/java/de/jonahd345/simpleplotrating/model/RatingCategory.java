package de.jonahd345.simpleplotrating.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

@Getter
public enum RatingCategory {
    CELLAR(GuiText.RATING_CATEGORY_CELLAR, Material.IRON_DOOR),
    INTERIOR_DESIGN(GuiText.RATING_CATEGORY_INTERIOR_DESIGN, Material.CRAFTING_TABLE),
    GARDEN(GuiText.RATING_CATEGORY_GARDEN, Material.DANDELION),
    EXTERIOR_VIEW(GuiText.RATING_CATEGORY_EXTERIOR_VIEW, Material.NETHER_STAR),
    CREATIVITY(GuiText.RATING_CATEGORY_CREATIVITY, Material.COMMAND_BLOCK, true);

    private final GuiText guiText;
    private final Material material;
    private final boolean isLastOne;

    RatingCategory(GuiText guiText, Material material, boolean isLastOne) {
        this.guiText = guiText;
        this.material = material;
        this.isLastOne = isLastOne;
    }

    RatingCategory(GuiText guiText, Material material) {
        this(guiText, material, false);
    }
}