package com.example.cache;

import java.util.HashMap;
import java.util.Map;

public class CacheNode {
    private final String nodeId;
    private final int capacity;
    private final Map<String, String> store;
    private final  EvictionPolicy<String> evictionPolicy;

    public  CacheNode(String nodeId, int capacity, EvictionPolicy<String> evictionPolicy) {
        this.nodeId = nodeId;
        this.capacity = capacity;
        this.store = new HashMap<>();
        this.evictionPolicy = evictionPolicy;
    }

    public  String get(String key) {
        if (!store.containsKey(key)) {
            return null;
        }
        evictionPolicy.keyAccessed(key);
        return store.get(key);
    }

    public  void put(String key, String value) {
        if (store.containsKey(key)) {
            store.put(key, value);
            evictionPolicy.keyAccessed(key);
            return;
        }

        if (store.size() >= capacity) {
            String  evictedKey = evictionPolicy.evict();
            if (evictedKey != null) {
                store.remove(evictedKey);
                System.out.println("[" + nodeId + "] Evicted key: " + evictedKey);
            }
        }

        store.put(key, value);
        evictionPolicy.keyAccessed(key);
    }

    public String  getNodeId() {
        return nodeId;
    }

    public int size() {
        return store.size();
    }
}
