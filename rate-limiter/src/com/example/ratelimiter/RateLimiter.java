package com.example.ratelimiter;

public interface RateLimiter {
    boolean  allowRequest(String key);
}
