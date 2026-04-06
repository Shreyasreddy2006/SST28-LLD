package com.example.ratelimiter;

public class ExternalServiceGateway {
    private final RateLimiter rateLimiter;

    public ExternalServiceGateway(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    public String callExternalApi(String clientKey, String requestData) {
        if (!rateLimiter.allowRequest(clientKey)) {
            return "REJECTED: Rate limit exceeded for " + clientKey;
        }
        return simulateExternalCall(requestData);
    }

    private String simulateExternalCall(String requestData) {
        return "SUCCESS: External API responded for -> " + requestData;
    }
}
