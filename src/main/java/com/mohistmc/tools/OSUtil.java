package com.mohistmc.tools;

public class OSUtil {

    private static OS os = null;

    public static OS getOS() {
        if (os == null) {
            String operSys = System.getProperty("os.name").toLowerCase();
            if (operSys.contains("win")) {
                os = OS.WINDOWS;
            } else if (operSys.contains("nix") || operSys.contains("nux") || operSys.contains("aix") || operSys.contains("bsd")) {
                os = OS.LINUX;
            } else if (operSys.contains("mac")) {
                os = OS.MAC;
            } else if (operSys.contains("sunos")) {
                os = OS.SOLARIS;
            } else {
                // 未识别时给出明确默认值，避免调用方拿到 null 触发 NPE
                os = OS.UNKNOWN;
            }
        }
        return os;
    }

    public enum OS {
        WINDOWS, LINUX, MAC, SOLARIS, UNKNOWN;

        public boolean isWindows() {
            return this == WINDOWS;
        }
    }
}
