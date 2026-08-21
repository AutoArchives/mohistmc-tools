package com.mohistmc.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class JarMerger {

    public static void mergeJars(File jarFile1, File jarFile2, File outputJar) throws IOException {
        // 局部 Set 而非静态字段：避免多次调用 / 并发调用时状态互相污染
        Set<String> processedEntries = new HashSet<>();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(outputJar))) {
            addJarContents(jarFile1, jos, processedEntries);
            addJarContents(jarFile2, jos, processedEntries);
        }
    }

    private static void addJarContents(File jarFile, JarOutputStream jos, Set<String> processedEntries) throws IOException {
        // JarFile 用 try-with-resources 保证关闭
        try (JarFile jar = new JarFile(jarFile)) {
            for (JarEntry entry : jar.stream().toList()) {
                if (entry.isDirectory() || processedEntries.contains(entry.getName())) {
                    continue;
                }
                processedEntries.add(entry.getName());
                // 新 JarEntry 需复制关键属性，避免部分工具无法读取
                JarEntry newEntry = new JarEntry(entry.getName());
                newEntry.setTime(entry.getTime());
                newEntry.setSize(entry.getSize());
                newEntry.setCompressedSize(entry.getCompressedSize());
                newEntry.setCrc(entry.getCrc());
                jos.putNextEntry(newEntry);
                try (InputStream is = jar.getInputStream(entry)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        jos.write(buffer, 0, bytesRead);
                    }
                }
                jos.closeEntry();
            }
        }
    }
}
