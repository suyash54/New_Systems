package ProducerConsumer;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class MessageLog {

    BlockingQueue<Message> blockingQueue;
    AtomicLong offset = new AtomicLong();


    public MessageLog(){
        blockingQueue = new LinkedBlockingQueue<>();
    }

    public Long append(String payload){

        long id = offset.getAndIncrement();
        blockingQueue.offer(new Message(id,payload));
        return id;

    }

    public Message read() throws InterruptedException{
       return blockingQueue.take();
    }


}
