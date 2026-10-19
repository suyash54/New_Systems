package BlockingQueue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue<T> {
    private final Queue<T> queue;
    int capacity;
    private final Object notFull;
    private final Object  notEmpty;
    private final Object lock;

    public BlockingQueue(int capacity){
        this.capacity = capacity;
        this.queue  = new LinkedList<>();
        this.lock = new Object();
        this.notFull = lock;
        this.notEmpty = lock;

    }

    public  void enqueue(T item) throws Exception {
        if (item == null) {
            throw new IllegalArgumentException("Cannot enqueue null item");
        }
        synchronized (lock) {
            while (queue.size() == capacity) {
                lock.wait();
            }
            int sizeBefore = queue.size();
            System.out.println("size enqueue before "+sizeBefore);
            queue.offer(item);
            int sizeAfter = queue.size();
            System.out.println("size enquueu after "+sizeAfter);
            lock.notifyAll();
        }
    }

    public T dequeue() throws Exception{
        synchronized (lock) {
            while (queue.isEmpty()) {
                lock.wait();
            }
            int sizeBefore = queue.size();
            System.out.println("size dequye before "+sizeBefore);
            T item = queue.poll();
            int sizeAfter = queue.size();
            System.out.println("size dequuee after "+sizeAfter);
            lock.notifyAll();
            return item;
        }
    }

    public int size(){
        synchronized (lock) {
            return queue.size();
        }
    }
}
