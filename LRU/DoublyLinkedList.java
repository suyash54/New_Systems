package LRU;

public class DoublyLinkedList {

    private final LinkedNode head;
    private final LinkedNode tail;

    public DoublyLinkedList(){

        head = new LinkedNode(-1,-1);
        tail = new LinkedNode(-1,-1);

        head.next = tail;
        tail.prev = head;
    }

    public void addFirst(LinkedNode node){
        node.next = head.next;
        head.next.prev = node;

        node.prev = head;
        head.next = node;
    }

    public void remove(LinkedNode node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    public LinkedNode removeLast(){
        if(tail.prev == head)
            return null;

        LinkedNode last = tail.prev;
        remove(last);
        return last;
    }

    public void moveToFront(LinkedNode node){
        remove(node);
        addFirst(node);
    }
}
