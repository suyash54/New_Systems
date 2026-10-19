package LRU;

public class LinkedNode {

    int key;
    int value;
    LinkedNode prev;
    LinkedNode next;

    LinkedNode(int key,int value){
        this.key = key;
        this.value = value;
    }
}
