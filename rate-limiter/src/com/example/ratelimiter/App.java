package com.example.ratelimiter;

public class App {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Rate Limiter Demo ===\n");

        RateLimitConfig config = new RateLimitConfig(5, 60000);

        System.out.println("--- Testing with Fixed Window ---");
        RateLimiter fixedWindow = RateLimiterFactory.create(
                RateLimiterFactory.Algorithm.FIXED_WINDOW, config);
        runDemo(fixedWindow);

        System.out.println("\n--- Testing with Sliding Window ---");
        RateLimiter slidingWindow = RateLimiterFactory.create(
                RateLimiterFactory.Algorithm.SLIDING_WINDOW, config);
        runDemo(slidingWindow);

        System.out.println("\n--- Mixed requests (some internal, some external) ---");
        RateLimiter limiter = RateLimiterFactory.create(
                RateLimiterFactory.Algorithm.FIXED_WINDOW,
                new RateLimitConfig(3, 60000));
        ExternalServiceGateway gateway = new ExternalServiceGateway(limiter);
        InternalService service = new InternalService(gateway);

        String[] requests = {
                "INTERNAL: fetch cache",
                "EXTERNAL: call payment api",
                "EXTERNAL: call payment api",
                "INTERNAL: read from db",
                "EXTERNAL: call payment api",
                "EXTERNAL: call payment api",
                "EXTERNAL: call payment api"
        };

        for  (String req : requests) {
            String result = service.handleRequest("tenant-A", req);
            System.out.println("  Request: \"" + req + "\" -> " + result);
        }
    }

    private  static void runDemo(RateLimiter limiter) {
        ExternalServiceGateway gateway = new ExternalServiceGateway(limiter);
        String tenant = "T1";

        for  (int i = 1; i <= 8; i++) {
            String result = gateway.callExternalApi(tenant, "request-" + i);
            System.out.println("  Call " + i + ": " + result);
        }
    }
}
