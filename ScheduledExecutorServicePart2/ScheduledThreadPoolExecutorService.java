package ScheduledExecutorServicePart2;

import java.util.concurrent.Callable;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ScheduledThreadPoolExecutorService {

    private final DelayQueue<ScheduledTask<?>> queue = new DelayQueue<>();
    private final WorkerThread[] workers;
    private final AtomicLong sequencer = new AtomicLong();
    private boolean shutdown = false;



    public ScheduledThreadPoolExecutorService(int poolSize){
        if (poolSize <= 0) {
            throw new IllegalArgumentException("poolSize must be > 0");
        }
        this.workers = new ScheduledExecutorServicePart2.WorkerThread[poolSize];
        for (int i = 0; i < poolSize; i++) {
            workers[i] = new  WorkerThread("scheduler-worker-" + i, queue, this);
            workers[i].start();
        }
    }

    public ScheduledTask<?> schedule(Runnable task, long delay, TimeUnit unit) {
        return submit(Executors.callable(task, null), unit.toNanos(delay), 0, ScheduledExecutorServicePart2.TaskType.ONE_SHOT);
    }

    public <V> ScheduledTask<V> schedule(Callable<V> task, long delay, TimeUnit unit) {
        return submit(task, unit.toNanos(delay), 0, ScheduledExecutorServicePart2.TaskType.ONE_SHOT);
    }

    public ScheduledTask<?> scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        requirePositive(period, "period");
        return submit(Executors.callable(task, null),
                unit.toNanos(initialDelay), unit.toNanos(period), ScheduledExecutorServicePart2.TaskType.FIXED_RATE);
    }

    public ScheduledTask<?> scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit unit) {
        requirePositive(delay, "delay");
        return submit(Executors.callable(task, null),
                unit.toNanos(initialDelay), unit.toNanos(delay), TaskType.FIXED_DELAY);
    }

    private static void requirePositive(long value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be > 0");
        }
    }

    private <V> ScheduledTask<V> submit(Callable<V> callable, long delayNanos, long periodNanos, TaskType type) {
        if (callable == null) {
            throw new NullPointerException("task must not be null");
        }
        if (shutdown) {
            throw new IllegalStateException("Executor has been shut down");
        }
        ScheduledTask<V> task = new ScheduledTask<V>(
                callable,
                System.nanoTime() + delayNanos,
                periodNanos,
                type,
                sequencer.getAndIncrement(),
                this);


        queue.add(task);
        return task;
    }

    void reEnqueue(ScheduledTask<?> task) {
        if (!shutdown) {
            queue.add(task);
        }
    }

    void dequeue(ScheduledTask<?> task) {
        queue.remove(task);
    }

    boolean isShutdown() {
        return shutdown;
    }

    public void shutdown() {
        if (shutdown) {
            return;
        }
        shutdown = true;
        queue.clear();
        for (WorkerThread worker : workers) {
            worker.interrupt();
        }
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        for (WorkerThread worker : workers) {
            long remaining = deadline - System.nanoTime();
            if (remaining <= 0) {
                return false;
            }
            worker.join(TimeUnit.NANOSECONDS.toMillis(remaining) + 1);
            if (worker.isAlive()) {
                return false;
            }
        }
        return true;
    }


    public int pendingTasks() {
        return queue.size();
    }

}
