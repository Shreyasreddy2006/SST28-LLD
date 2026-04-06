package com.example.ratelimiter;

public class RateLimiterTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Running Rate Limiter Tests ===\n");

        testFixedWindowAllowsWithinLimit();
        testFixedWindowBlocksOverLimit();
        testSlidingWindowAllowsWithinLimit();
        testSlidingWindowBlocksOverLimit();
        testDifferentKeysHaveSeparateLimits();
        testFactoryCreatesCorrectType();
        testInternalServiceSkipsRateLimiting();
        testInternalServiceChecksRateLimitForExternal();
        testSlidingWindowResetsAfterWindowPasses();
        testThreadSafety();

        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }

    static void testFixedWindowAllowsWithinLimit() {
        RateLimitConfig config = new RateLimitConfig(3, 60000);
        RateLimiter limiter = new FixedWindowRateLimiter(config);

        boolean first = limiter.allowRequest("user1");
        boolean second = limiter.allowRequest("user1");
        boolean third = limiter.allowRequest("user1");

        check("FixedWindow allows requests within limit",
                first && second && third);
    }

    static void testFixedWindowBlocksOverLimit() {
        RateLimitConfig config = new RateLimitConfig(2, 60000);
        RateLimiter limiter = new FixedWindowRateLimiter(config);

        limiter.allowRequest("user1");
        limiter.allowRequest("user1");
        boolean third = limiter.allowRequest("user1");

        check("FixedWindow blocks requests over limit", !third);
    }

    static void testSlidingWindowAllowsWithinLimit() {
        RateLimitConfig config = new RateLimitConfig(3, 60000);
        RateLimiter limiter = new SlidingWindowRateLimiter(config);

        boolean first = limiter.allowRequest("user1");
        boolean second = limiter.allowRequest("user1");
        boolean third = limiter.allowRequest("user1");

        check("SlidingWindow allows requests within limit",
                first && second && third);
    }

    static void testSlidingWindowBlocksOverLimit() {
        RateLimitConfig config = new RateLimitConfig(2, 60000);
        RateLimiter limiter = new SlidingWindowRateLimiter(config);

        limiter.allowRequest("user1");
        limiter.allowRequest("user1");
        boolean third = limiter.allowRequest("user1");

        check("SlidingWindow blocks requests over limit", !third);
    }

    static void testDifferentKeysHaveSeparateLimits() {
        RateLimitConfig config = new RateLimitConfig(1, 60000);
        RateLimiter limiter = new FixedWindowRateLimiter(config);

        boolean userA = limiter.allowRequest("userA");
        boolean userB = limiter.allowRequest("userB");

        check("Different keys have separate limits",
                userA && userB);
    }

    static void testFactoryCreatesCorrectType() {
        RateLimitConfig config = new RateLimitConfig(5, 60000);

        RateLimiter fixed = RateLimiterFactory.create(
                RateLimiterFactory.Algorithm.FIXED_WINDOW, config);
        RateLimiter sliding = RateLimiterFactory.create(
                RateLimiterFactory.Algorithm.SLIDING_WINDOW, config);

        check("Factory creates FixedWindowRateLimiter",
                fixed instanceof FixedWindowRateLimiter);
        check("Factory creates SlidingWindowRateLimiter",
                sliding instanceof SlidingWindowRateLimiter);
    }

    static void testInternalServiceSkipsRateLimiting() {
        RateLimitConfig config = new RateLimitConfig(1, 60000);
        RateLimiter limiter = new FixedWindowRateLimiter(config);
        ExternalServiceGateway gateway = new ExternalServiceGateway(limiter);
        InternalService service = new InternalService(gateway);

        String result1 = service.handleRequest("user1", "INTERNAL: get data");
        String result2 = service.handleRequest("user1", "INTERNAL: get more data");

        check("Internal requests skip rate limiting",
                result1.contains("internally") && result2.contains("internally"));
    }

    static void testInternalServiceChecksRateLimitForExternal() {
        RateLimitConfig config = new RateLimitConfig(1, 60000);
        RateLimiter limiter = new FixedWindowRateLimiter(config);
        ExternalServiceGateway gateway = new ExternalServiceGateway(limiter);
        InternalService service = new InternalService(gateway);

        String first = service.handleRequest("user1", "EXTERNAL: call api");
        String second = service.handleRequest("user1", "EXTERNAL: call api");

        check("External requests are rate limited",
                first.contains("SUCCESS") && second.contains("REJECTED"));
    }

    static void testSlidingWindowResetsAfterWindowPasses() throws InterruptedException {
        RateLimitConfig config = new RateLimitConfig(2, 500);
        RateLimiter limiter = new SlidingWindowRateLimiter(config);

        limiter.allowRequest("user1");
        limiter.allowRequest("user1");
        boolean blocked = limiter.allowRequest("user1");

        Thread.sleep(600);

        boolean afterReset = limiter.allowRequest("user1");

        check("SlidingWindow resets after window passes",
                !blocked && afterReset);
    }

    static void testThreadSafety() throws InterruptedException {
        RateLimitConfig config = new RateLimitConfig(100, 60000);
        RateLimiter limiter = new SlidingWindowRateLimiter(config);

        int threadCount = 10;
        int requestsPerThread = 20;
        Thread[] threads = new Thread[threadCount];
        int[] allowedCounts = new int[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int idx = i;
            threads[i] = new Thread(() -> {
                int count = 0;
                for (int j = 0; j < requestsPerThread; j++) {
                    if (limiter.allowRequest("shared-key")) {
                        count++;
                    }
                }
                allowedCounts[idx] = count;
            });
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        int totalAllowed = 0;
        for (int c : allowedCounts) totalAllowed += c;

        check("Thread safety: total allowed <= max (" + totalAllowed + " <= 100)",
                totalAllowed <= 100);
    }

    private static void check(String testName, boolean condition) {
        if (condition) {
            System.out.println("  PASS: " + testName);
            passed++;
        } else {
            System.out.println("  FAIL: " + testName);
            failed++;
        }
    }
}
