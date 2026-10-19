package BlockingQueue;

import java.util.concurrent.atomic.AtomicInteger;

public class Producer extends Thread{
    private final BlockingQueue<String> queue;
    private final String name;
    private static final AtomicInteger globalCounter = new AtomicInteger(0);

    Producer(BlockingQueue<String> queue,String name){
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run(){
        try{
            while(!Thread.currentThread().isInterrupted()) {
                int id = globalCounter.incrementAndGet();
                String item = name + "-Item#" + id;

                // Capture size before and after to show accurate state
                int sizeBefore;

                    sizeBefore = queue.size();

                queue.enqueue(item);

                int sizeAfter;

                    sizeAfter = queue.size();

                System.out.println(name + " produced: " + item +
                        " | Queue: " + sizeBefore + " → " + sizeAfter);
                Thread.sleep(100);
            }
        } catch(Exception ex){
            System.out.println("Producer was not able to produce because"+ex);
        }
    }
}
