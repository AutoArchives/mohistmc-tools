package com.mohistmc.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Mgazul by MohistMC
 * @date 2023/10/7 0:58:48
 */
public class ConnectionUtil {

    private static final boolean debug = true;

    public static boolean isValid(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static boolean canAccess(String urlStr) {
        HttpURLConnection connection = null;
        try {
            connection = openConn(urlStr);
            if (connection == null) {
                return false;
            }
            connection.connect();
            int responseCode = connection.getResponseCode();
            return responseCode >= 200 && responseCode < 300;
        } catch (IOException e) {
            return false;
        } finally {
            disconnect(connection);
        }
    }

    public static Integer getCode(String urlStr) {
        HttpURLConnection connection = null;
        try {
            connection = openConn(urlStr);
            if (connection == null) {
                return null;
            }
            connection.connect();
            return connection.getResponseCode();
        } catch (IOException e) {
            return null;
        } finally {
            disconnect(connection);
        }
    }

    public static URLConnection getConn(String URL) {
        try {
            URLConnection connection = URI.create(URL).toURL().openConnection();
            connection.setRequestProperty("User-Agent", "MohistMC-Tools/" + Tools.version());
            return connection;
        } catch (IOException e) {
            return null;
        }
    }

    private static HttpURLConnection openConn(String urlStr) {
        URLConnection connection = getConn(urlStr);
        return connection instanceof HttpURLConnection http ? http : null;
    }

    private static void disconnect(HttpURLConnection connection) {
        if (connection != null) {
            connection.disconnect();
        }
    }

    /**
     * 打开一个 GET 连接的输入流。调用方负责关闭返回的流；
     * 若需要在流读取完成后释放底层连接，请对底层 HttpURLConnection 调用 disconnect()。
     */
    public static InputStream getInputStream(String s) throws IOException {
        URLConnection connection = getConn(s);
        if (connection instanceof HttpURLConnection http) {
            http.setConnectTimeout(3000);
            http.setRequestMethod("GET");
            if (http.getResponseCode() == 200) {
                return http.getInputStream();
            }
            http.disconnect();
        }
        return null;
    }

    public static long measureLatency(String urlString) {
        HttpURLConnection connection = null;
        try {
            long start = System.nanoTime();
            connection = openConn(urlString);
            if (connection == null) {
                return Long.MAX_VALUE;
            }
            int responseCode = connection.getResponseCode();
            long end = System.nanoTime();
            // 网络错误也视为不可达
            if (responseCode < 200 || responseCode >= 400) {
                return Long.MAX_VALUE;
            }
            return Duration.ofNanos(end - start).toMillis();
        } catch (Exception e) {
            return Long.MAX_VALUE;
        } finally {
            disconnect(connection);
        }
    }

    public static String fastURL(List<String> urls) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<CompletableFuture<LatencyResult>> futures = urls.stream()
                .map(url -> CompletableFuture.supplyAsync(() -> measureLatency(url), executor)
                        .thenApply(latency -> new LatencyResult(url, latency)))
                .collect(Collectors.toList());

        Optional<LatencyResult> minLatencyResult = futures.stream()
                .map(CompletableFuture::join)
                .min(Comparator.comparing(LatencyResult::latency));

        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        return minLatencyResult.map(LatencyResult::url).orElse(null);
    }

    public static boolean downloadFile(String URL, File f) {
        try {
            URLConnection conn = getConn(URL);
            if (conn == null) {
                return false;
            }
            try (InputStream rbc = conn.getInputStream()) {
                try (java.io.FileOutputStream fos = new java.io.FileOutputStream(f)) {
                    byte[] buffer = new byte[8192];
                    int read;
                    while ((read = rbc.read(buffer)) != -1) {
                        fos.write(buffer, 0, read);
                    }
                }
            }
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

    public static boolean isValidHost(String host) {
        // 分离主机和端口（如果有）
        String[] parts = host.split(":");
        String mainPart = parts[0];
        if (mainPart.isEmpty()) {
            return false;
        }
        int port = parts.length > 1 ? Integer.parseInt(parts[1]) : -1;

        // 验证域名规则（支持国际化域名IDN）
        String domainPattern =
                "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$"; // 基础域名验证
        String ipv4Pattern = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";
        String ipv6Pattern = "^([0-9a-fA-F]{1,4}:){7}([0-9a-fA-F]{1,4})$";

        // 验证主机部分
        boolean validHost = mainPart.matches(domainPattern)
                || mainPart.matches(ipv4Pattern)
                || mainPart.matches(ipv6Pattern);

        // 验证端口（如果存在）
        boolean validPort = (port == -1) || (port > 0 && port <= 65535);

        return validHost && validPort;
    }

    record LatencyResult(String url, long latency) {
    }
}
