package ScheduledExecutorServicePart2;

import NewScheduledThreadPoolExecutorService.NewScheduledThreadPoolExecutorService;

import java.util.concurrent.*;

public class ScheduledTask<V> implements Delayed {



    private final Callable<V> callable;
    private final long period;
    private final TaskType taskType;
    private final long sequenceNumber;
    private final ScheduledThreadPoolExecutorService executor;
    private volatile long triggerTime;

    private volatile boolean cancelled;
    private volatile boolean done;
    private volatile V result;
    private volatile Throwable failure;


    private final CountDownLatch completion = new CountDownLatch(1);

    ScheduledTask(Callable<V> callable,
                  long triggerTime,
                  long period,
                  TaskType type,
                  long sequenceNumber,
                  ScheduledThreadPoolExecutorService executor) {
        this.callable = callable;
        this.triggerTime = triggerTime;
        this.period = period;
        this.taskType = type;
        this.sequenceNumber = sequenceNumber;
        this.executor = executor;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(triggerTime - System.nanoTime(),TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if(o==this)
            return 0;

       ScheduledTask<?> that = (ScheduledTask<?>) o;
       long diff = this.triggerTime - that.triggerTime;
        if (diff < 0) return -1;
        if (diff > 0) return 1;
        return Long.compare(this.sequenceNumber, that.sequenceNumber);
     }


     void execute(){

        if(cancelled || executor.isShutdown()){
            return;
        }

        boolean succeeded = runOnce();

        if(!taskType.isPeriodic()){
            finish();
            return;
        }

         if (!succeeded || cancelled || executor.isShutdown()) {
             finish();
             return;
         }

         if(taskType == TaskType.FIXED_RATE){

             triggerTime+=period;
             long now = System.nanoTime();

             if(triggerTime-now<0){
                 long behind = now - triggerTime;
                 triggerTime+= ((behind/period) + 1) * period;
             }
         }
         else{
             triggerTime+=System.nanoTime()+period;
         }
         executor.reEnqueue(this);
     }

     private boolean runOnce(){
        try{
            result = callable.call();
            return true;
        } catch(Throwable t){
            failure = t;
            System.err.println("[" + Thread.currentThread().getName()
                    + "] task #" + sequenceNumber + " failed: " + t);
            return false;
        }
     }

    private void finish() {
        done = true;
        completion.countDown();
    }

    public boolean cancel(){
        if(cancelled || done)
            return false;

        cancelled = true;
        finish();
        executor.dequeue(this);
        return true;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public boolean isDone() {
        return done;
    }

    public boolean isPeriodic() {
        return taskType.isPeriodic();
    }

    public TaskType getType() {
        return taskType;
    }

    public V get() throws InterruptedException, ExecutionException {
        completion.await();
        return reportResult();
    }

    public V get(Long timeOut,TimeUnit unit) throws InterruptedException,ExecutionException,TimeoutException{
        if(!completion.await(timeOut,unit)){
            throw new TimeoutException("task #" + sequenceNumber + " did not complete in time");
        }
        return reportResult();
    }

    private V reportResult() throws ExecutionException {
        if (cancelled) {
            throw new CancellationException("task #" + sequenceNumber + " was cancelled");
        }
        if (failure != null) {
            throw new ExecutionException(failure);
        }
        return result;
    }

    @Override
    public String toString() {
        return "ScheduledTask#" + sequenceNumber + "{" + taskType
                + ", dueIn=" + getDelay(TimeUnit.MILLISECONDS) + "ms}";
    }

}
