package BlockingQueue;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException{
        BlockingQueue<String> queue = new BlockingQueue<String>(5);
        Thread producer1 = new Thread(new Producer(queue,"P1"),"Producer-1");
        Thread producer2 = new Thread(new Producer(queue,"P2"),"Producer-2");

        Thread consumer1 = new Thread(new Consumer(queue,"C1"),"Consumer-1");
        Thread consumer2 = new Thread(new Consumer(queue,"C2"),"Consumer-2");

        producer1.start();;
        producer2.start();

        consumer1.start();
        consumer2.start();
        Long currentTimeStamp = System.currentTimeMillis();
        Thread.sleep(5000);
        Long newTimestamp = System.currentTimeMillis();
        System.out.println("Time service ran"+(newTimestamp-currentTimeStamp));
        producer1.interrupt();
        producer2.interrupt();
        consumer2.interrupt();
        consumer1.interrupt();

        producer2.join();
        producer1.join();
        consumer1.join();
        consumer2.join();

        System.out.println("Demo completed. Final queue size: " + queue.size());


    }
}
