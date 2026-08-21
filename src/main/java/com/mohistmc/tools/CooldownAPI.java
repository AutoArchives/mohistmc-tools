package com.mohistmc.tools;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 冷却时间工具。构造时传入：冷却起始时间(old) 与 冷却结束时间(now)。
 */
public record CooldownAPI(LocalDateTime start, LocalDateTime end) {

    /**
     * 当前时间是否已经过了冷却结束时间。
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(end);
    }

    /**
     * 距离冷却结束还剩余多少秒（已结束则返回 0）。
     */
    public long timeLeft() {
        Duration duration = Duration.between(LocalDateTime.now(), end);
        long seconds = duration.getSeconds();
        return Math.max(seconds, 0);
    }
}
