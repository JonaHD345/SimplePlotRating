package de.jonahd345.simpleplotrating.model;

import lombok.Getter;
import lombok.Setter;

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

    private final String defaultMessage;
    @Setter
    private String message;

    Message(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    Message(String defaultMessage, String message) {
        this.defaultMessage = defaultMessage;
        this.message = message;
    }

    public static String getMessageWithPrefix(Message message) {
        return PREFIX.getMessage() + message.getMessage();
    }
}
