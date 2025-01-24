package de.jonahd345.simpleplotrating.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum GuiText {
    // v1.0
    PREFIX("&a&lSIMPLEPLOTRATING &7» "),
    RATING_TITLE("&7Plot rating"),
    RATING_CATEGORY_CELLAR("&7Cellar"),
    RATING_CATEGORY_INTERIOR_DESIGN("&7Interior design"),
    RATING_CATEGORY_GARDEN("&7Garden"),
    RATING_CATEGORY_EXTERIOR_VIEW("&7Exterior view"),
    RATING_CATEGORY_CREATIVITY("&7Creativity"),
    RATING_RATING_ITEM("&fRating: &a%rating%&7/&a5"),
    RATING_CLOSE_ITEM("&cClose"),
    RATING_SKIP_ITEM("&7Skip this category"),
    RATING_NEXT_ITEM("&7Next category"),
    RATING_GO_TO_SUMMARY_ITEM("&7Complete rating"),
    RATING_SUMMARY_TITLE("&7Plot rating summary (&a%rating%&7/&a25&7)"),
    RATING_SUMMARY_SKULL("&2%plot_owner%"),
    RATING_SUMMARY_COMPLETE_ITEM("&7Set rating");

    private final String defaultText;
    @Setter
    private String text;

    GuiText(String defaultText) {
        this.defaultText = defaultText;
    }

    GuiText(String defaultText, String text) {
        this.defaultText = defaultText;
        this.text = text;
    }

    public static String getTextWithPrefix(GuiText guiText) {
        return PREFIX.getText() + guiText.getText();
    }
}
