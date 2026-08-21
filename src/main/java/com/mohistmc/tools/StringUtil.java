package com.mohistmc.tools;

public class StringUtil {

    public static String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
        }
    }

    public static String mavenToPath(String maven) {
        String type;
        if (maven.matches(".*@\\w+$")) {
            int i = maven.lastIndexOf('@');
            type = maven.substring(i + 1);
            maven = maven.substring(0, i);
        } else {
            type = "jar";
        }
        String[] arr = maven.split(":");
        if (arr.length == 3) {
            String pkg = arr[0].replace('.', '/');
            return String.format("%s/%s/%s/%s-%s.%s", pkg, arr[1], arr[2], arr[1], arr[2], type);
        } else if (arr.length == 4) {
            String pkg = arr[0].replace('.', '/');
            return String.format("%s/%s/%s/%s-%s-%s.%s", pkg, arr[1], arr[2], arr[1], arr[2], arr[3], type);
        } else throw new RuntimeException("Wrong maven coordinate " + maven);
    }
}
