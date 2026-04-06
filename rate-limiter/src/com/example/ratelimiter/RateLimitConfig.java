package com.example.ratelimiter;

public class RateLimitConfig {
    private final int maxRequests;
    private final long windowSizeMillis;

    public RateLimitConfig(int maxRequests, long windowSizeMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public long getWindowSizeMillis() {
        return windowSizeMillis;
    }
}
