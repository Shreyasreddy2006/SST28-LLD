// Code modified for uniqueness
package com.example.metrics;

import java.lang.reflect.Constructor;

/**
 * Attempts to create multiple instances via reflection.
 * Starter allows this. After fix, it must fail.
 */
public class ReflectionAttack {

    public static void main(String[] args) throws Exception {
        MetricsRegistry instance1 = MetricsRegistry.getInstance();

        Constructor<MetricsRegistry> constructor = MetricsRegistry.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        MetricsRegistry instance2 = constructor.newInstance();

        System.out.println("Singleton identity: " + System.identityHashCode(instance1));
        System.out.println("Evil identity     : " + System.identityHashCode(instance2));
        System.out.println("Same object?      : " + (instance1 == instance2));
    }
}
