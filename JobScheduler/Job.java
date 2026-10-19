package JobScheduler;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Job {

    private final String name;
    private final int retry;
    private final int timeout;
    private final LocalDateTime startTime;
    private final Worker task;

    Job(String name,int retry,int timeout, LocalDateTime startTime, Worker task){

        this.name = name;
        this.retry = retry;
        this.timeout = timeout;
        this.startTime = startTime;
        this.task = task;
    }

    public String getName() { return this.name;}

    public LocalDateTime getStartTime(){ return this.startTime;}

    public Worker getTask(){ return this.task;}


}
