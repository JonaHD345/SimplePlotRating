package de.jonahd345.simpleplotrating.util;

import java.util.Map;

public class StringUtil {
    /**
     * Replaces placeholders in the input string with their corresponding values from the provided map.
     *
     * @param input the input string containing placeholders
     * @param placeholders a map where keys are placeholders to be replaced and values are the replacement strings
     * @return the input string with all placeholders replaced by their corresponding values
     */
    public static String replacePlaceholder(String input, Map<String, String> placeholders) {
        for (String placeholder : placeholders.keySet()) {
            // replace placeholder with value
            input = input.replace(placeholder, placeholders.get(placeholder));
        }
        return input;
    }
}
