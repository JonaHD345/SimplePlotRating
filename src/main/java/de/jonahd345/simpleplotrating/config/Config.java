package de.jonahd345.simpleplotrating.config;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Config {
    UPDATE_NOTIFICATION(true);

    private final Object defaultValue;
    @Setter
    private Object value;

    Config(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getDefaultValueAsBoolean() {
        return Boolean.valueOf(this.defaultValue.toString());
    }

    public Boolean getValueAsBoolean() {
        return Boolean.valueOf(this.value.toString());
    }

    public Double getDefaultValueAsDouble() {
        return Double.valueOf(this.defaultValue.toString());
    }

    public Double getValueAsDouble() {
        try { return Double.valueOf(this.value.toString()); } catch (NumberFormatException e) { return this.getDefaultValueAsDouble(); }
    }
}
