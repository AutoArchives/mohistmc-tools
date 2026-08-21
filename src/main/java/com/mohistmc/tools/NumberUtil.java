package com.mohistmc.tools;

/**
 * Utils for casting number types to other number types
 */
public class NumberUtil {
    public static Integer toInt(Object object) {
        if (object instanceof Number) {
            return ((Number) object).intValue();
        }

        try {
            return Integer.parseInt(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
            return null;
        }
    }

    public static Double toDouble(Object object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }

        try {
            return Double.parseDouble(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
            return null;
        }
    }

    public static Long toLong(Object object) {
        if (object instanceof Number) {
            return ((Number) object).longValue();
        }

        try {
            return Long.parseLong(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
            return null;
        }
    }

    public static Float toFloat(Object object) {
        if (object instanceof Number) {
            return ((Number) object).floatValue();
        }

        try {
            return Float.parseFloat(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
            return null;
        }
    }

    public static Byte toByte(Object object) {
        if (object instanceof Number) {
            return ((Number) object).byteValue();
        }

        try {
            return Byte.parseByte(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {
            return null;
        }
    }

    public static Short toShort(Object object) {
        if (object instanceof Number) {
            return ((Number) object).shortValue();
        }

        try {
            return Short.parseShort(object.toString());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static String getSize(int size) {
        if (size >= 1048576) {
            return String.format("%.2f MB", size / 1048576.0);
        } else if (size >= 1024) {
            return String.format("%.2f KB", size / 1024.0);
        }
        return size + " B";
    }
}
