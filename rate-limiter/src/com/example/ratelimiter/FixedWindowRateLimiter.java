package com.example.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedWindowRateLimiter implements RateLimiter {
    private final RateLimitConfig config;
    private final ConcurrentHashMap<String, WindowData> windows = new ConcurrentHashMap<>();

    public FixedWindowRateLimiter(RateLimitConfig config) {
        this.config = config;
    }

    @Override
    public boolean allowRequest(String key) {
        long now = System.currentTimeMillis();
        long currentWindow = now / config.getWindowSizeMillis();

        windows.compute(key, (k, existing) -> {
            if (existing == null || existing.windowId != currentWindow) {
                return new WindowData(currentWindow);
            }
            return existing;
        });

        WindowData data = windows.get(key);
        if (data.windowId != currentWindow) {
            windows.put(key, new WindowData(currentWindow));
            data = windows.get(key);
        }

        int count = data.counter.incrementAndGet();
        return count <= config.getMaxRequests();
    }

    private static class WindowData {
        final long windowId;
        final AtomicInteger counter;

        WindowData(long windowId) {
            this.windowId = windowId;
            this.counter = new AtomicInteger(0);
        }
    }
}
