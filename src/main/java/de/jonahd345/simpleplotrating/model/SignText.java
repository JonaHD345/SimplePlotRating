package de.jonahd345.simpleplotrating.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum SignText {
    // v1.0
    RATING_LINE_1("&0Rated by"),
    RATING_LINE_2("&a%rated_player%"),
    RATING_LINE_3("&0on %date%"),
    RATING_LINE_4("&a%rating%/25 points");

    private final String defaultText;
    @Setter
    private String text;

    SignText(String defaultText) {
        this.defaultText = defaultText;
    }

    SignText(String defaultText, String text) {
        this.defaultText = defaultText;
        this.text = text;
    }
}
