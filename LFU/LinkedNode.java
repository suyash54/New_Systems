package LFU;

public class LinkedNode {

    int key;
    int value;
    LinkedNode next;
    LinkedNode prev;

    int freq = 1;


   public LinkedNode(int key, int value){
       this.key = key;
       this.value = value;
   }
}
