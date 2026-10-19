package ProducerConsumer;

public class Partition {

    private final MessageLog log;

    public Partition(){
        log = new MessageLog();
    }

    public long publish(String msg){
       return log.append(msg);
    }

    public Message read() throws InterruptedException{
        return log.read();
    }
}
