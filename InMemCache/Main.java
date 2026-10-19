package InMemCache;

public class Main {

    public static void main(String[] args) {
        // Create cache with 1 minute TTL and max 1000 entries
        InMemCache<String, String> cache =
                new InMemCache(60000, 1000);

        // Put with automatic expiration
        cache.put("key1", "value1")
                .thenAccept(oldValue -> System.out.println("Stored successfully"));

        // Get with null if expired
        cache.get("key1")
                .thenAccept(value -> System.out.println("Retrieved: " + value));

        cache.shutdown();
    }
}
