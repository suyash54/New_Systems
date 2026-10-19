package LRU;

import java.util.HashMap;
import java.util.Map;

public class LRU {

    private final int capacity;
    private final Map<Integer,LinkedNode> keyMap;
    private final DoublyLinkedList dll;


    public LRU(int capacity){
        this.capacity = capacity;
        this.keyMap = new HashMap<>();
        this.dll = new DoublyLinkedList();
    }

    public int get(int key){
        if(!keyMap.containsKey(key))
             return -1;

        LinkedNode node = keyMap.get(key);
        dll.moveToFront(node);

        return node.value;
    }

    public void put(int key, int value){

        if(keyMap.containsKey(key)){
            LinkedNode node = keyMap.get(key);
            node.value = value;
            dll.moveToFront(node);
            return;
        }

        if(keyMap.size()==capacity){
            LinkedNode lru = dll.removeLast();
            keyMap.remove(lru.key);
        }

        LinkedNode newNode = new LinkedNode(key , value);
        dll.addFirst(newNode);
        keyMap.put(key,newNode);
    }

    public static void main(String[] args){
        LRU lru = new LRU(5);

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
