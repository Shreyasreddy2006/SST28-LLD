// Code modified for uniqueness
package com.example.metrics;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Spawns many threads racing on getInstance().
 * Starter is expected to sometimes create >1 instance. After fix, must always be 1.
 */
public class ConcurrencyCheck {

    public static void main(String[] args) throws Exception {
        int threads = 80;
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        CountDownLatch readySignal = new CountDownLatch(threads);
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(threads);

        Set<Integer> identities = ConcurrentHashMap.newKeySet();

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                readySignal.countDown();
                try {
                    startSignal.await();
                    MetricsRegistry r = MetricsRegistry.getInstance();
                    identities.add(System.identityHashCode(r));
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneSignal.countDown();
                }
            });
        }

        readySignal.await(2, TimeUnit.SECONDS);
        startSignal.countDown();
        doneSignal.await(3, TimeUnit.SECONDS);
        executor.shutdownNow();

        System.out.println("Unique instances seen: " + identities.size());
        System.out.println("Identities: " + identities);
    }
}
