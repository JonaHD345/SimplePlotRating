package de.jonahd345.simpleplotrating.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Enum representing various messages used in the SimplePlotRating plugin.
 * Each enum constant has a default message and a customizable message.
 */
@Getter
public enum Message {
    // v1.0
    PREFIX("&a&lSIMPLEPLOTRATING &8» "),
    NO_PLAYER("&7You are not a player!"),
    NO_PERMISSION("&7No permission!"),
    NO_PLOT("&7You are not on a plot!"),
    NO_OWNER("&7This plot has no owner!"),
    OPEN_GUI("&7Opening rating GUI..."),
    RATING_SET("&7You have rated this plot from &a%plot_owner% &7with &a%rating%&7/&a25 &7points!"),;

    /**
     * The default message.
     */
    private final String defaultMessage;
    /**
     * The customizable message.
     */
    @Setter
    private String message;

    /**
     * Constructor for Message enum.
     *
     * @param defaultMessage the default message
     */
    Message(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    /**
     * Constructor for Message enum with customizable message.
     *
     * @param defaultMessage the default message
     * @param message the customizable message
     */
    Message(String defaultMessage, String message) {
        this.defaultMessage = defaultMessage;
        this.message = message;
    }

    /**
     * Gets the message with the prefix.
     *
     * @param message the message enum constant
     * @return the message with the prefix
     */
    public static String getMessageWithPrefix(Message message) {
        return PREFIX.getMessage() + message.getMessage();
    }

    /**
     * Returns the message of the enum constant.
     *
     * @return the message of the enum
     */
    @Override
    public String toString() {
        return this.message;
    }
}
