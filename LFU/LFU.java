package LFU;


import LRU.LRU;

import java.util.HashMap;
import java.util.Map;

public class LFU {


   private final Map<Integer, LinkedNode> keyMap;
   private final Map<Integer,DoublyLinkedList> freqMap;

   private final int size = 0;
   private final int capacity;
   private  int minFreq = 0;


   public LFU(int capacity){
       this.capacity = capacity;
       freqMap = new HashMap<>();
       keyMap = new HashMap<>();
   }

   public void put(int key,int val){

       if(keyMap.containsKey(key)){
           LinkedNode node = keyMap.get(key);
           node.value = val;
           updateFrequency(node);
           return;
       }

       if(keyMap.size() == capacity){
           DoublyLinkedList list = freqMap.get(minFreq);
           LinkedNode node = list.removeLast();
           keyMap.remove(node.key);
       }

       minFreq  = 1;
       DoublyLinkedList list = freqMap.getOrDefault(1,new DoublyLinkedList());

       LinkedNode node = new LinkedNode(key,val);
       list.addFirst(node);

       freqMap.put(1,list);
       keyMap.put(key,node);
   }

   public int get(int key){
       if(!keyMap.containsKey(key)){
           return -1;
       }

       LinkedNode node = keyMap.get(key);
       updateFrequency(node);

       return node.value;
   }

   public void updateFrequency(LinkedNode node){
       int oldFrequency = node.freq;
       DoublyLinkedList oldList = freqMap.get(oldFrequency);

       oldList.remove(node);

       if(oldFrequency == minFreq && oldList.isEmpty()){
           minFreq++;
       }

       node.freq++;

       DoublyLinkedList newList = freqMap.getOrDefault(node.freq,new DoublyLinkedList());

       newList.addFirst(node);

       freqMap.put(node.freq,newList);

   }

    public static void main(String[] args){
        LFU lru = new LFU(5);

        System.out.println("value for 6"+lru.get(6));
        System.out.println("Inser 5, 7");
        lru.put(5,7);
        lru.put(6,8);
        lru.put(7,9);
        lru.put(10,12);
        lru.put(17,18);
        lru.put(19,20);
        System.out.println("Value for key 5"+lru.get(5));
        System.out.println("Value for key 19 "+lru.get(19));
    }

}
