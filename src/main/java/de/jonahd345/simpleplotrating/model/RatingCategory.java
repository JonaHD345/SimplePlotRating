package de.jonahd345.simpleplotrating.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

/**
 * Enum representing different rating categories, each associated with a GUI text and a material.
 */
@Getter
public enum RatingCategory {
    CELLAR(GuiText.RATING_CATEGORY_CELLAR, Material.IRON_DOOR),
    INTERIOR_DESIGN(GuiText.RATING_CATEGORY_INTERIOR_DESIGN, Material.PAINTING),
    GARDEN(GuiText.RATING_CATEGORY_GARDEN, Material.OXEYE_DAISY),
    EXTERIOR_VIEW(GuiText.RATING_CATEGORY_EXTERIOR_VIEW, Material.NETHER_STAR),
    CREATIVITY(GuiText.RATING_CATEGORY_CREATIVITY, Material.STRUCTURE_BLOCK, true);

    /**
     * The GUI text associated with the rating category.
     */
    private final GuiText guiText;
    /**
     * The material associated with the rating category.
     */
    private final Material material;
    /**
     * Flag indicating if this is the last rating category.
     */
    private final boolean isLastOne;

    /**
     * Constructor for RatingCategory enum.
     *
     * @param guiText the GUI text associated with the rating category
     * @param material the material associated with the rating category
     * @param isLastOne flag indicating if this is the last rating category
     */
    RatingCategory(GuiText guiText, Material material, boolean isLastOne) {
        this.guiText = guiText;
        this.material = material;
        this.isLastOne = isLastOne;
    }

    /**
     * Constructor for RatingCategory enum with default isLastOne value.
     *
     * @param guiText the GUI text associated with the rating category
     * @param material the material associated with the rating category
     */
    RatingCategory(GuiText guiText, Material material) {
        this(guiText, material, false);
    }
}