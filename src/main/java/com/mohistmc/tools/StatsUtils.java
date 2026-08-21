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

    private static final int BAR_WIDTH = 70;

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
        long usedMemory = totalMemory() - freeMemory();
        // 注意：必须先转 double 再除法，避免整型截断（原实现 (double)(x/100L) 会得到错误结果）
        return usedMemory / (totalMemory() * 0.01);
    }

    public static String getMemoryUsageBar() {
        return buildUsageBar(getMemoryUsage());
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
        Attribute att = (Attribute) list.getFirst();
        Double value = (Double) att.getValue();
        if (value == -1.0) {
            return Double.NaN;
        }
        return (int) (value * 1000.0) / 10.0;
    }

    public static String getCPUUsageBar() throws Exception {
        return buildUsageBar(getProcessCpuLoad());
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
        if (total == 0) {
            return 0.0;
        }
        long used = total - freeDisk();
        return (used * 100.0) / total;
    }

    public static String getDiskUsageBar() {
        return buildUsageBar(getDiskUsage());
    }

    /**
     * 通用用量条构建：前 usage% 用红(§c)，临界点用橙(§6)，其余用绿(§a)。
     * 重复的三段式逻辑统一在此，避免三处复制。
     */
    private static String buildUsageBar(double usagePercent) {
        int usage = (int) Math.ceil(usagePercent * 0.7);
        StringBuilder usagebar = new StringBuilder(BAR_WIDTH);
        for (int i = 0; i < BAR_WIDTH; ++i) {
            if (i < usage) {
                usagebar.append("§c▬");
            } else if (usage == i) {
                usagebar.append("§6▬");
            } else {
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
