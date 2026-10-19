package ProducerConsumer;

import java.util.ArrayList;
import java.util.List;

public class Topic {

   private final List<Partition> partitionList;


   public Topic(int partitionCount){
       partitionList = new ArrayList<>();

       for(int i=0;i<partitionCount;i++){
           partitionList.add(new Partition());
       }
   }

   public Partition getPartition(int key){
       return partitionList.get(key%partitionList.size());
   }

   public List<Partition> getAllPartitions(){
       return partitionList;
   }


}
