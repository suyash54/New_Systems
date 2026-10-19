package LFU;

public class DoublyLinkedList {
    private LinkedNode head;
    private LinkedNode tail;

    public DoublyLinkedList(){
        this.head = new LinkedNode(-1,-1);
        this.tail = new LinkedNode(-1,-1);

        head.next = tail;
        tail.prev = head;
    }

    public void remove(LinkedNode node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    public void addFirst(LinkedNode node){
        node.next = head.next;
        head.next = node;
        node.prev = head;
        node.next.prev = node;
    }

    public LinkedNode removeLast(){
        if(isEmpty())
             return null;

        LinkedNode last = tail.prev;
        remove(last);
        return last;
    }

    Boolean isEmpty(){
        return head.next == tail;
    }
}
