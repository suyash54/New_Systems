package MeetingSchedular;

public class Rooms {

    int roomId;
    int meetingCounts;

    public Rooms(int roomId){
        this.roomId = roomId;
        this.meetingCounts = 0;
    }

    public void incrementUsage(){
        this.meetingCounts++;
    }

    public int getMeetingCounts(){
        return this.meetingCounts;
    }

    public int getRoomId(){
        return this.roomId;
    }
}
