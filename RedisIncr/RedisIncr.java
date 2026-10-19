package RedisIncr;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RedisIncr {

    private static final ConcurrentHashMap<String, AtomicInteger> store = new ConcurrentHashMap<>();

    public static CompletableFuture<Integer> incrAsync(String key) {
        return CompletableFuture.supplyAsync(() ->
            store.computeIfAbsent(key, k -> new AtomicInteger(0)).incrementAndGet()
        );
    }

    public static void main(String[] args) {
        RedisIncr redis = new RedisIncr();

        ExecutorService executor = Executors.newFixedThreadPool(5);

        CompletableFuture<Void> task1 = CompletableFuture.runAsync(() -> runTask(redis), executor);
        CompletableFuture<Void> task2 = CompletableFuture.runAsync(() -> runTask(redis), executor);
        CompletableFuture<Void> task3 = CompletableFuture.runAsync(() -> runTask(redis), executor);

        CompletableFuture.allOf(task1,task2,task3).join();

        executor.shutdown();

    }

    public static void runTask(RedisIncr redis){
        for(int i=0;i<100;i++){
            redis.incrAsync("counter").thenAccept(value ->
                    System.out.println(Thread.currentThread().getName() + "->" + value));
        }
    }
}
