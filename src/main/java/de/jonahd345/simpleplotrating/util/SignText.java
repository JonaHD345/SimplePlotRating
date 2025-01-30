package de.jonahd345.simpleplotrating.util;

import lombok.Getter;
import lombok.Setter;

/**
 * Enum representing the text for signs in the SimplePlotRating plugin.
 * Each enum constant has a default text and a customizable text.
 */
@Getter
public enum SignText {
    // v1.0
    RATING_LINE_1("&0Rated by"),
    RATING_LINE_2("&a%rated_player%"),
    RATING_LINE_3("&0on %date_day%.%date_month%.%date_year%"),
    RATING_LINE_4("&a%rating%/25 points");

    /**
     * The default text for the sign line.
     */
    private final String defaultText;
    /**
     * The customizable text for the sign line.
     */
    @Setter
    private String text;

    /**
     * Constructor for SignText enum.
     *
     * @param defaultText the default text for the sign line
     */
    SignText(String defaultText) {
        this.defaultText = defaultText;
    }

    /**
     * Constructor for SignText enum with customizable text.
     *
     * @param defaultText the default text for the sign line
     * @param text the customizable text for the sign line
     */
    SignText(String defaultText, String text) {
        this.defaultText = defaultText;
        this.text = text;
    }
}
