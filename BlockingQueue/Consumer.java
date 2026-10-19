package BlockingQueue;

public class Consumer extends Thread {
    private final BlockingQueue<String> queue;
    private final String name;

    Consumer(BlockingQueue<String> queue , String name){
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run(){
        try{
            while (!Thread.currentThread().isInterrupted()) {
                // Capture size before and after
                int sizeBefore;
                    sizeBefore = queue.size();

                String item = queue.dequeue();

                int sizeAfter;
                    sizeAfter = queue.size();


                System.out.println(name + " consumed: " + item +
                        " | Queue: " + sizeBefore + " → " + sizeAfter);
                Thread.sleep(150);
            }
        } catch(Exception ex){
            System.out.println("Could not consume,error occurred"+ex);
        }
    }
}
