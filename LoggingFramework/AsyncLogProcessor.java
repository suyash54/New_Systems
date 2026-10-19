package LoggingFramework;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncLogProcessor extends AbstractLogProcessor{

    private final ExecutorService executorService;

    public AsyncLogProcessor(){
        this(1);
    }

    public AsyncLogProcessor(int workers) {
        this.executorService = Executors.newFixedThreadPool(workers, new ThreadFactory() {

            private final AtomicInteger n = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r){
                Thread t = new Thread(r,"log-worker-"+n.getAndIncrement());
                t.setDaemon(true);
                return t;
            }
        });
    }


    @Override
    public void process(LogMessage message, List<LogAppender> appenderList){
        try{
            executorService.execute(() -> dispatch(message,appenderList));
        } catch (RuntimeException e) {
            dispatch(message,appenderList);
        }
    }

    @Override
    public void shutdown(){
        executorService.shutdown();
        try{
            if(!executorService.awaitTermination(5, TimeUnit.SECONDS)){
                executorService.shutdown();
            }
        } catch(InterruptedException ex){
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
