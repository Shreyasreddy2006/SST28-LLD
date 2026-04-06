package com.example.cache;

import java.util.ArrayList;
import java.util.List;

public class DistributedCache {
    private final List<CacheNode> nodes;
    private final DistributionStrategy strategy;
    private final Database database;

    public DistributedCache(int numberOfNodes, int capacityPerNode, DistributionStrategy strategy,
                            Database database) {
        this.strategy = strategy;
        this.database = database;
        this.nodes = new ArrayList<>();

        for (int i = 0; i < numberOfNodes; i++) {
            EvictionPolicy<String> policy = new LRUEvictionPolicy<>();
            nodes.add(new CacheNode("node-" + i, capacityPerNode, policy));
        }
    }

    public String get(String key) {
        int nodeIndex = strategy.getNode(key, nodes.size());
        CacheNode node = nodes.get(nodeIndex);

        String value = node.get(key);
        if (value != null) {
            System.out.println("Cache HIT for key '" + key + "' on " + node.getNodeId());
            return value;
        }

        System.out.println("Cache MISS for key '" + key + "' on " + node.getNodeId());
        value = database.get(key);
        if (value != null) {
            node.put(key, value);
        }
        return value;
    }

    public void put(String key, String value) {
        int nodeIndex = strategy.getNode(key, nodes.size());
        CacheNode node = nodes.get(nodeIndex);

        node.put(key, value);
        database.put(key, value);
        System.out.println("PUT key '" + key + "' on " + node.getNodeId());
    }

    public List<CacheNode> getNodes() {
        return nodes;
    }
}
