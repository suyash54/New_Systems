package ProducerConsumer;

public class Producer extends Thread{

    private final Broker broker;
    private final String topic;
    private final int id;

    public Producer(Broker broker,String topic,int id){
        this.broker = broker;
        this.topic = topic;
        this.id = id;
    }

    @Override
    public void run(){

        for(int i=1;i<=5;i++){
            broker.publish(topic,i,"P"+id+"-msg"+i);
        }
    }
}
