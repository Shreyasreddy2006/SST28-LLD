package com.example.ratelimiter;

public class RateLimiterFactory {

    public enum Algorithm {
        FIXED_WINDOW,
        SLIDING_WINDOW
    }

    public static RateLimiter create(Algorithm algorithm, RateLimitConfig config) {
        switch (algorithm) {
            case  FIXED_WINDOW:
                return new FixedWindowRateLimiter(config);
            case  SLIDING_WINDOW:
                return new SlidingWindowRateLimiter(config);
            default:
                throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        }
    }
}
