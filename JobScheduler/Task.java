package JobScheduler;

import java.util.function.Function;

public class Task implements Worker{

    private final Function<JobData,Integer> function;
    private final JobData jobData;

    Task(Function<JobData,Integer> function , JobData jobData){

        this.function = function;
        this.jobData = jobData;
    }

    @Override
    public void performTask() {

        System.out.println("Output: "+ function.apply(jobData));
        for(int i=0;i<1000000000;i++){
            i++;
        }
    }
}
