package de.jonahd345.simpleplotrating.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Enum representing various GUI text elements for the SimplePlotRating application.
 * Each enum constant holds a default text value and an optional custom text value.
 */
@Getter
public enum GuiText {
    // v1.0
    PREFIX("&a&lSIMPLEPLOTRATING &8» "),
    RATING_TITLE("&8Plot rating"),
    RATING_CATEGORY_CELLAR("&7Cellar"),
    RATING_CATEGORY_INTERIOR_DESIGN("&7Interior design"),
    RATING_CATEGORY_GARDEN("&7Garden"),
    RATING_CATEGORY_EXTERIOR_VIEW("&7Exterior view"),
    RATING_CATEGORY_CREATIVITY("&7Creativity"),
    RATING_RATING_ITEM("&fRating: &a%rating%&7/&a5"),
    RATING_CLOSE_ITEM("&cClose"),
    RATING_SKIP_ITEM("&7Skip this category"),
    RATING_NEXT_ITEM("&aNext category"),
    RATING_GO_TO_SUMMARY_ITEM("&7Complete rating"),
    RATING_SUMMARY_TITLE("&8Plot rating summary (&a%rating%&8/&a25&8)"),
    RATING_SUMMARY_SKULL("&2%plot_owner%"),
    RATING_SUMMARY_COMPLETE_ITEM("&7Set rating");

    /**
     * The default text.
     */
    private final String defaultText;
    /**
     * The customizable text.
     */
    @Setter
    private String text;

    /**
     * The customizable message.
     */
    GuiText(String defaultText) {
        this.defaultText = defaultText;
    }

    /**
     * Constructor for GuiText enum with default text and custom text.
     *
     * @param defaultText the default text for the GUI element
     * @param text the custom text for the GUI element
     */
    GuiText(String defaultText, String text) {
        this.defaultText = defaultText;
        this.text = text;
    }

    /**
     * Gets the text of the specified GuiText enum constant with the prefix.
     *
     * @param guiText the GuiText enum constant
     * @return the text with the prefix
     */
    public static String getTextWithPrefix(GuiText guiText) {
        return PREFIX.getText() + guiText.getText();
    }
}
