package io.github.yeetzy.currencies.util.number;

import io.github.yeetzy.currencies.economy.data.type.EconomyType;

import java.text.DecimalFormat;

public class NumberFormat {

    private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.#");

    public static String format(double number) {
        return DECIMAL_FORMAT.format(number);
    }

    public static String format(double number, EconomyType economyType) {
        return DECIMAL_FORMAT.format(number)
                .concat(" ")
                .concat(number == 1
                        ? economyType.getDisplayName().toLowerCase()
                        : economyType.getDisplayNamePlural().toLowerCase()
                );
    }

}
