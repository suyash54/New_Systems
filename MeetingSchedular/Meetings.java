package MeetingSchedular;

public class Meetings {
    int startTime;
    int endTime;

    public Meetings(int startTime,int endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getStartTime(){
        return this.startTime;
    }

    public int getEndTime(){
        return this.endTime;
    }
}
