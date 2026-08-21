package com.mohistmc.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/10 19:24:26
 */
public class FileUtils {

    public static List<String> readFileFromJar(ClassLoader classLoader, String path) {
        InputStream in = classLoader.getResourceAsStream(path);
        if (in == null) {
            return Collections.emptyList();
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public static void deleteFolders(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteFolders(f);
                }
            }
        }
        file.delete();
    }

    public static boolean fileExists(File f, String fName) {
        if (!f.exists()) {
            return false;
        }
        // JarFile 必须关闭，否则句柄泄漏
        try (JarFile jf = new JarFile(f)) {
            JarEntry entry = jf.getJarEntry(fName);
            return entry != null;
        } catch (IOException e) {
            return false;
        }
    }

    public static void fileWriterMethod(String filepath, String content) {
        try (FileWriter fileWriter = new FileWriter(filepath)) {
            fileWriter.append(content);
        } catch (IOException e) {
            // 原实现静默吞掉异常，至少记录到标准错误，便于排查
            e.printStackTrace();
        }
    }
}
