package ProducerConsumer;

import java.util.concurrent.ConcurrentHashMap;

public class Broker {

    public final ConcurrentHashMap<String,Topic> topics = new ConcurrentHashMap<>();

    public void createTopic(String name ,int partitions){
        topics.put(name,new Topic(partitions));
    }

    public void publish(String topic,int key,String msg){

        Topic t = topics.get(topic);
        Partition p = t.getPartition(key);
        p.publish(msg);

    }

    public Message poll(String topic,int partition) throws InterruptedException{

        Topic t = topics.get(topic);

        return t.getAllPartitions()
                .get(partition)
                .read();
    }
}