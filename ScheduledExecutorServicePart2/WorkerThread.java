package ScheduledExecutorServicePart2;

import NewScheduledThreadPoolExecutorService.NewScheduledThreadPoolExecutorService;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

public class WorkerThread extends Thread{

    private final DelayQueue<ScheduledTask<?>> queue;
    private final ScheduledThreadPoolExecutorService executorService;

    WorkerThread(String name,
                 DelayQueue<ScheduledTask<?>> queue,
                 ScheduledThreadPoolExecutorService executor) {
        super(name);
        this.queue = queue;
        this.executorService = executor;
    }

    @Override
    public void run(){
        if(!executorService.isShutdown()){
            try{
                ScheduledTask<?> task = queue.take();
                task.execute();
            } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
               return;
            } catch(Throwable t){
                System.err.println("[" + getName() + "] unexpected worker error: " + t);
            }
        }
    }
}
