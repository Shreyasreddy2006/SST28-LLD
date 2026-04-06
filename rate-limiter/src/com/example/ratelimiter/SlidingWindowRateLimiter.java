package com.example.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class SlidingWindowRateLimiter implements RateLimiter {
    private final RateLimitConfig config;
    private final ConcurrentHashMap<String, ConcurrentLinkedDeque<Long>> requestLogs = new ConcurrentHashMap<>();

    public SlidingWindowRateLimiter(RateLimitConfig config) {
        this.config = config;
    }

    @Override
    public synchronized boolean allowRequest(String key) {
        long now = System.currentTimeMillis();
        long windowStart = now - config.getWindowSizeMillis();

        requestLogs.putIfAbsent(key, new ConcurrentLinkedDeque<>());
        ConcurrentLinkedDeque<Long> timestamps = requestLogs.get(key);

        while (!timestamps.isEmpty() && timestamps.peekFirst() <= windowStart) {
            timestamps.pollFirst();
        }

        if (timestamps.size() < config.getMaxRequests()) {
            timestamps.addLast(now);
            return true;
        }

        return false;
    }
}
