package ClickCounter;

import java.util.ArrayDeque;
import java.util.Deque;

public class ClickCounter {

    Deque<Params> deque;
    private int totalClicks;

    public ClickCounter(){
        this.deque = new ArrayDeque<>();
        this.totalClicks = 0;
    }

    public void recordClick(int timestamp){
        if(!deque.isEmpty() && deque.peekLast().getTimeStamp()==timestamp){
            deque.peekLast().incrementCount();
        }
        else{
            deque.offerLast(new Params(timestamp,1));
        }
        totalClicks++;
    }

    public int getRecentClicks(int timeStamp){
        int windowStart = timeStamp - 299;

        while(!deque.isEmpty() && windowStart > deque.peekFirst().getTimeStamp()){
           Params params =  deque.pollFirst();
           totalClicks-=params.getCount();
        }

        return totalClicks;
    }
}
