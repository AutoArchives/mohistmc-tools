package com.mohistmc.tools;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * @author Mgazul
 * @date 2025/11/23 01:17
 */
public class Formater {

    private static DecimalFormat decimalFormat(String pattern) {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        formatSymbols.setDecimalSeparator('.');
        return new DecimalFormat(pattern, formatSymbols);
    }

    public static String formatValue(double value) {
        boolean isWholeNumber = value == Math.round(value);
        String pattern = isWholeNumber ? "######.###" : "##,###0.00";
        return decimalFormat(pattern).format(value);
    }

    public static String formatDecimal(double value) {
        boolean isWholeNumber = value == Math.round(value);
        String pattern = isWholeNumber ? "######.###" : "#####0.00";
        return decimalFormat(pattern).format(value);
    }

    public static String formatInteger(double value) {
        boolean isWholeNumber = value == Math.round(value);
        String pattern = isWholeNumber ? "#########" : "#####0";
        return decimalFormat(pattern).format(value);
    }
}
