package JobScheduler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class MultithreadedScheculer {

  private static final int ALLOWED_MAX_JOB = 3;
  private static  int jobsInProgress = 0;
  private static final ReentrantLock lock = new ReentrantLock();

  public static void main(String[] args){

    List<Job> jobList = new ArrayList<>();
    List<CompletableFuture<Void>> futures = new CopyOnWriteArrayList<>();

    Function<JobData,Integer> function = (op) -> op.getX() * op.getY();
    JobData jobData = new JobData(10,20);

    Worker task1 = new Task(function,jobData);
    Worker task2 = new Task((op) -> op.getY() + op.getX(),jobData);
    Worker task3 = new Task((op) -> op.getX() % op.getY(),jobData);

    jobList.add(new Job("Job1", 2 , 0, LocalDateTime.now(), task1 ));
    jobList.add(new Job("Job2",2,0,LocalDateTime.now(), task2));
    jobList.add(new Job("Job3", 2 , 0, LocalDateTime.now(), task3 ));
    jobList.add(new Job("Job4",2,0,LocalDateTime.now(), task1));
    jobList.add(new Job("Job5", 2 , 0, LocalDateTime.now(), task3 ));
    jobList.add(new Job("Job6",2,0,LocalDateTime.now(), task2));
    jobList.add(new Job("Job7", 2 , 0, LocalDateTime.now(), task1 ));
    jobList.add(new Job("Job8",2,0,LocalDateTime.now(), task2));
    jobList.add(new Job("Job9", 2 , 0, LocalDateTime.now(), task3 ));
    jobList.add(new Job("Job10",2,0,LocalDateTime.now(), task2));
    jobList.add(new Job("Job11", 2 , 0, LocalDateTime.now(), task1 ));
    jobList.add(new Job("Job12",2,0,LocalDateTime.now(), task3));

    while(!jobList.isEmpty()){

      List<Job> jobsToRemove = new ArrayList<>();

      for(Job job: jobList){

        if(shouldStartJob(job)){
          CompletableFuture<Void> future = executeJob(job);
          futures.add(future);
          jobsToRemove.add(job);
        }
      }

      jobList.removeAll(jobsToRemove);

      futures.removeIf(CompletableFuture::isDone);

      try{
        Thread.sleep(1000);
      } catch(InterruptedException e){
        Thread.currentThread().interrupt();
        break;
      }

    }

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();


  }

  private static boolean shouldStartJob(Job job){

    return (job.getStartTime().equals(LocalDateTime.now()) ||
            job.getStartTime().isBefore(LocalDateTime.now()))
            && (jobsInProgress < ALLOWED_MAX_JOB);
  }

  private static CompletableFuture<Void> executeJob(Job job) {

    return CompletableFuture.runAsync(() -> {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        jobsInProgress++;
      System.out.println("Starting job: " + job.getName());

      lock.lock();
      try {
        job.getTask().performTask();
        System.out.println("Completed job: " + job.getName());
        jobsInProgress--;
      } finally {
        lock.unlock();
      }
    });
  }

}
