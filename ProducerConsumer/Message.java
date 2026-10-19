package ProducerConsumer;

import java.util.concurrent.atomic.AtomicLong;

public class Message {

    private final Long offset;
    private final String payload;

    public Message(Long offset, String payload){
        this.offset = offset;
        this.payload = payload;
    }

    public String getPayload(){
        return this.payload;
    }

}
