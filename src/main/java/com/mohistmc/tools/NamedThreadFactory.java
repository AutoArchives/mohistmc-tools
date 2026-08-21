package com.mohistmc.tools;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Mgazul
 * @create 2019/9/11 20:57
 */
public record NamedThreadFactory(String name) implements ThreadFactory {

    // 每个工厂独立计数；AtomicInteger 保证并发 newThread 时编号唯一
    private static final AtomicInteger GLOBAL_ID = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(name + " - " + GLOBAL_ID.incrementAndGet());
        thread.setPriority(4);
        return thread;
    }
}
