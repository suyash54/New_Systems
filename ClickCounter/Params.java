package ClickCounter;

public class Params {
    int timeStamp;
    int count;

    Params(int timeStamp,int count){
        this.timeStamp = timeStamp;
        this.count = count;
    }

   public int getTimeStamp(){
        return timeStamp;
    }

    public int getCount(){
        return this.count;
    }
    public void incrementCount() {
        this.count++;
    }
}
