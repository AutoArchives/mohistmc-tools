/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2023.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    public static void zipFolder(Path sourceFolderPath, Path zipPath) throws IOException {
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(zipPath));
             Stream<Path> paths = Files.walk(sourceFolderPath)) {
            paths.filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceFolderPath.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException e) {
                            // 包装为 RuntimeException，让调用方感知失败而非静默忽略
                            throw new RuntimeException("Failed to zip entry: " + path, e);
                        }
                    });
        }
    }

    /**
     * 读取文件 / 输入流的全部内容并以字符串返回。
     * 支持 String 路径与 InputStream；参数为 null 时返回 null。
     */
    public static String getFileContent(Object fileInPath) throws IOException {
        if (fileInPath == null) {
            return null;
        }
        BufferedReader br;
        if (fileInPath instanceof String s) {
            br = new BufferedReader(new FileReader(s, StandardCharsets.UTF_8));
        } else if (fileInPath instanceof InputStream inputStream) {
            br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } else {
            return null;
        }

        // try-with-resources 保证 BufferedReader 一定被关闭
        try (BufferedReader reader = br) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            return sb.toString();
        }
    }
}
