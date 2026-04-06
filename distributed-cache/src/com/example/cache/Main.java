package com.example.cache;

public class Main {
    public  static void main(String[] args) {
        Database db = new InMemoryDatabase();
        db.put("user:1", "Alice");
        db.put("user:2", "Bob");
        db.put("user:3", "Charlie");
        db.put("user:4", "Dave");
        db.put("user:5", "Eve");

        DistributionStrategy strategy = new ModuloDistribution();
        DistributedCache cache = new DistributedCache(3, 2, strategy, db);

        System.out.println("=== Testing get (cache miss -> loads from DB) ===");
        System.out.println("user:1 -> " + cache.get("user:1"));
        System.out.println("user:2 -> " + cache.get("user:2"));
        System.out.println("user:3 -> " + cache.get("user:3"));

        System.out.println("\n=== Testing get (cache hit) ===");
        System.out.println("user:1 -> " + cache.get("user:1"));
        System.out.println("user:2 -> " + cache.get("user:2"));

        System.out.println("\n=== Testing put ===");
        cache.put("user:6", "Frank");
        cache.put("user:7", "Grace");

        System.out.println("\n=== Testing eviction (capacity is 2 per node) ===");
        cache.put("user:8", "Heidi");
        cache.put("user:9", "Ivan");
        cache.put("user:10", "Judy");

        System.out.println("\n=== Verify eviction by re-fetching ===");
        System.out.println("user:1 -> " + cache.get("user:1"));

        System.out.println("\n=== Node sizes ===");
        for (CacheNode node : cache.getNodes()) {
            System.out.println(node.getNodeId() + " has " + node.size() + " entries");
        }
    }
}
