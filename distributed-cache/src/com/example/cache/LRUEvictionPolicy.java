package com.example.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Iterator;

public class  LRUEvictionPolicy<K> implements EvictionPolicy<K> {
    private final LinkedHashMap<K, Boolean> accessOrder;

    public LRUEvictionPolicy() {
        this.accessOrder = new LinkedHashMap<>(16, 0.75f, true);
    }

    @Override
    public void keyAccessed(K key) {
        accessOrder.put(key, Boolean.TRUE);
    }

    @Override
    public K evict() {
        Iterator<Map.Entry<K, Boolean>> iterator = accessOrder.entrySet().iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        K oldest = iterator.next().getKey();
        iterator.remove();
        return oldest;
    }

    @Override
    public void remove(K key) {
        accessOrder.remove(key);
    }
}
