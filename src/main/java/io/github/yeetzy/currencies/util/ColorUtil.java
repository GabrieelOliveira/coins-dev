package io.github.yeetzy.currencies.util;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class ColorUtil {

    public static String colored(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String[] colored(String... messages) {
        for (int i = 0; i < messages.length; i++) {
            messages[i] = colored(messages[i]);
        }

        return messages;
    }

    public static List<String> colored(List<String> description) {
        return description.stream()
                .map(ColorUtil::colored)
                .collect(Collectors.toList());
    }

}
