package com.mohistmc.tools;

import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Objects;
import lombok.SneakyThrows;

/**
 * @author Mgazul
 * @date 2025/2/12 14:40
 */
public class SHA256 {

    /**
     * Get the SHA-256 value of this input stream
     *
     * @param is
     * @return
     */
    public static String as(InputStream is) {
        try {
            return String.format("%032x", new BigInteger(1, new DigestInputStream(is, MessageDigest.getInstance("SHA-256")).getMessageDigest().digest())).toLowerCase();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Get the SHA-256 value of the file
     *
     * @param file
     * @return
     */
    public static String as(File file) {
        try {
            return String.format("%032x", new BigInteger(1, MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(file.toPath())))).toLowerCase();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get the SHA-256 value of the specified file or path
     *
     * @param filePath
     * @return
     */
    public static String as(String filePath) {
        return as(new File(filePath));
    }

    /**
     * Check the SHA-256 value of this input stream
     *
     * @param is
     * @param toBeCheckSum
     * @return
     */
    @SneakyThrows
    public static boolean is(InputStream is, String toBeCheckSum) {
        return Objects.equals(as(is), toBeCheckSum);
    }

    /**
     * Check the SHA-256 value of the file
     *
     * @param file
     * @param toBeCheckSum
     * @return
     */
    @SneakyThrows
    public static boolean is(File file, String toBeCheckSum) {
        return Objects.equals(as(file), toBeCheckSum);
    }

    /**
     * Check the SHA-256 value of the specified path file
     *
     * @param path
     * @param toBeCheckSum
     * @return
     */
    @SneakyThrows
    public static boolean is(String path, String toBeCheckSum) {
        return Objects.equals(as(path), toBeCheckSum);
    }
}
