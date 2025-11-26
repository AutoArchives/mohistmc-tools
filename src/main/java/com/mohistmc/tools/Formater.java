
package com.mohistmc.tools;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * @author Mgazul
 * @date 2025/11/23 01:17
 */
public class Formater {

    public static String formatValue(double value) {
        boolean isWholeNumber = value == Math.round(value);
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        formatSymbols.setDecimalSeparator('.');
        String pattern = isWholeNumber ? "######.###" : "##,###0.00";
        DecimalFormat df = new DecimalFormat(pattern, formatSymbols);
        return df.format(value);
    }

    public static String formatDecimal(double value) {
        boolean isWholeNumber = value == Math.round(value);
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        formatSymbols.setDecimalSeparator('.');
        String pattern = isWholeNumber ? "######.###" : "#####0.00";
        DecimalFormat df = new DecimalFormat(pattern, formatSymbols);
        return df.format(value);
    }

    public static String formatInteger(double value) {
        boolean isWholeNumber = value == Math.round(value);
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        formatSymbols.setDecimalSeparator('.');
        String pattern = isWholeNumber ? "#########" : "#####0";
        DecimalFormat df = new DecimalFormat(pattern, formatSymbols);
        return df.format(value);
    }
}