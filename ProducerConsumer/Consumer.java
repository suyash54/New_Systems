package ProducerConsumer;

public class Consumer extends  Thread{

    private final Broker broker;
    private final String topic;
    private final int partition;

    public Consumer(Broker broker,String topic,int partition ){
        this.broker = broker;
        this.topic = topic;
        this.partition = partition;
    }

    @Override
    public void run(){

        while(!Thread.currentThread().isInterrupted()){
            try{
                Message m = broker.poll(topic,partition);

                System.out.println(Thread.currentThread().getName()+" consumed "
                        +" " + m.getPayload());
            } catch(InterruptedException ex){
                Thread.currentThread().interrupt();
                break;
            } catch(Exception ex){
                throw new RuntimeException(ex);
            }
        }
    }
}
