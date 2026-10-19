package InMemCache;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InMemCache<K, V> {

    ConcurrentHashMap<K, CacheEntry<V>> cache;
    private final ExecutorService service;
    private final long defaultTTlInMilliseconds;
    private final int maxSize;


    public InMemCache(long defaultTTlInMilliseconds, int maxSize) {
        this.cache = new ConcurrentHashMap<>();
        this.service = Executors.newFixedThreadPool(5);
        this.defaultTTlInMilliseconds = defaultTTlInMilliseconds;
        this.maxSize = maxSize;

        startCleanUpThread();
    }

    public CompletableFuture<V> get(K key) {
        return CompletableFuture.supplyAsync(() -> {
            CacheEntry<V> entry = cache.get(key);
            if (entry != null && !entry.isExpired()) {
                return entry.value;
            }
            if (entry != null) {
                cache.remove(key);
            }
            return null;
        }, service);
    }

    public CompletableFuture<V> put(K key, V value) {
        return CompletableFuture.supplyAsync(() -> {
            if (cache.size() >= maxSize) {
                evictOldest();
            }
            CacheEntry<V> entry = new CacheEntry<>(value, System.currentTimeMillis() + defaultTTlInMilliseconds);
            CacheEntry<V> oldEntry = cache.put(key, entry);
            return oldEntry != null ? entry.value : null;
        }, service);
    }

    public CompletableFuture<V> remove(K key) {
        return CompletableFuture.supplyAsync(() -> {
            CacheEntry<V> entry = cache.remove(key);
            return entry != null ? entry.value : null;
        });
    }

    public void evictOldest() {
        if (cache.isEmpty())
            return;
        K oldestKey = cache.entrySet().stream()
                .min((e1, e2) -> Long.compare(e1.getValue().expiryTime, e2.getValue().expiryTime))
                .map(Map.Entry::getKey)
                .orElse(null);

        if (oldestKey != null)
            cache.remove(oldestKey);
    }

    public void startCleanUpThread() {

        Thread cleanUpThread = new Thread(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000);
                    cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        cleanUpThread.setDaemon(true);
        cleanUpThread.start();
    }

    public void shutdown() {
        service.shutdown();
    }
}
