package ProducerConsumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ProducerConsumerDemo {

    public static  void main(String[] args) throws InterruptedException {

        Broker broker = new Broker();
        broker.createTopic("Topic-1",3);

        ExecutorService service = Executors.newFixedThreadPool(6);

        for(int i=0;i<3;i++){
            service.submit(new Producer(broker,"Topic-1",i));
        }

        for(int i=0;i<3;i++){
            service.submit(new Consumer(broker,"Topic-1",i));
        }

        service.shutdown();
    }
}
