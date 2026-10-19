package MeetingSchedular;

public class BusyRooms {
    int endTime;
    Rooms room;

    public BusyRooms(int endTime,Rooms room){
        this.endTime = endTime;
        this.room = room;
    }

    public int getEndTime(){
        return this.endTime;
    }

    public Rooms getRoom(){
        return this.room;
    }
}
