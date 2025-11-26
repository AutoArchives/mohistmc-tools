package com.mohistmc.tools;

import java.io.File;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.Attribute;
import javax.management.ObjectName;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

/**
 * @author Mgazul
 * @date 2025/11/23 01:52
 */
public class StatsUtils {

    public static long freeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public static long maxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public static long totalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public static double getMemoryUsage() {
        long used_memory = totalMemory() - freeMemory();
        double percent = (double)(totalMemory() / 100L);
        return used_memory / percent;
    }

    public static String getMemoryUsageBar() {
        StringBuilder usagebar = new StringBuilder();
        double round_usage = getMemoryUsage() * 0.7;
        int usage = (int)Math.ceil(round_usage);
        for (int i = 0; i < 70; ++i) {
            if (i < usage) {
                usagebar.append("§c▬");
            }
            else if (usage == i) {
                usagebar.append("§6▬");
            }
            else {
                usagebar.append("§a▬");
            }
        }
        return usagebar.toString();
    }

    public static double LoadAverange() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        double loadAverage = osBean.getSystemLoadAverage();
        if (loadAverage < 0) {
            return 0.0;
        }
        return loadAverage;
    }

    public static double getProcessCpuLoad() throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
        AttributeList list = mbs.getAttributes(name, new String[] { "ProcessCpuLoad" });
        if (list.isEmpty()) {
            return Double.NaN;
        }
        Attribute att = (Attribute) list.get(0);
        Double value = (Double)att.getValue();
        if (value == -1.0) {
            return Double.NaN;
        }
        return (int)(value * 1000.0) / 10.0;
    }

    public static String getCPUUsageBar() throws Exception {
        StringBuilder usagebar = new StringBuilder();
        double round_usage = getProcessCpuLoad() * 0.7;
        int usage = (int)Math.ceil(round_usage);
        for (int i = 0; i < 70; ++i) {
            if (i < usage) {
                usagebar.append("§c▬");
            }
            else if (usage == i) {
                usagebar.append("§6▬");
            }
            else {
                usagebar.append("§a▬");
            }
        }
        return usagebar.toString();
    }

    public static long totalDisk() {
        File f = new File("/");
        return f.getTotalSpace();
    }

    public static long freeDisk() {
        File f = new File("/");
        return f.getFreeSpace();
    }

    public static long usableDisk() {
        File f = new File("/");
        return f.getUsableSpace();
    }

    public static double getDiskUsage() {
        long total = totalDisk();
        if (total == 0) return 0.0;
        long used_memory = total - freeDisk();
        return (used_memory * 100.0) / total;
    }

    public static String getDiskUsageBar() {
        StringBuilder usagebar = new StringBuilder();
        double round_usage = getDiskUsage() * 0.7;
        int usage = (int)Math.ceil(round_usage);
        for (int i = 0; i < 70; ++i) {
            if (i < usage) {
                usagebar.append("§c▬");
            }
            else if (usage == i) {
                usagebar.append("§6▬");
            }
            else {
                usagebar.append("§a▬");
            }
        }
        return usagebar.toString();
    }

    public static double BytesToMegaBytes(long bytes) {
        return round(bytes / (1024.0 * 1024.0));
    }

    public static double BytesToGigaBytes(long bytes) {
        return round(bytes / (1024.0 * 1024.0 * 1024.0));
    }

    static double round(double value) {
        return Math.round(value * Math.pow(10.0, 3)) / Math.pow(10.0, 3);
    }
}
