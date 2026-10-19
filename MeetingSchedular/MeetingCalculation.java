package MeetingSchedular;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class MeetingCalculation {

    public int mostBooked(int n, List<Meetings> meetingsList){

        meetingsList.sort(Comparator.comparingInt(Meetings::getStartTime));

        PriorityQueue<Rooms> free = new PriorityQueue<>(Comparator.comparingInt(Rooms::getRoomId));

        PriorityQueue<BusyRooms> busyRooms = new PriorityQueue<>(Comparator.comparingInt(BusyRooms::getEndTime));

        for(int i=0;i<n;i++){
            free.add(new Rooms(i));
        }

        for(Meetings meeting: meetingsList){
            int startTime = meeting.startTime;
            int endTime = meeting.endTime;

            while(!busyRooms.isEmpty() && startTime >= busyRooms.peek().endTime){
                BusyRooms room = busyRooms.poll();
                free.add(room.getRoom());
            }

            if(!free.isEmpty()){
                Rooms room = free.poll();
                room.incrementUsage();
                busyRooms.add(new BusyRooms(endTime,room));
            }
            else{
                BusyRooms room = busyRooms.poll();
                Rooms newRoom = room.getRoom();

                int newTime = room.getEndTime() + (meeting.getEndTime() - meeting.getStartTime());
                newRoom.incrementUsage();

                busyRooms.add(new BusyRooms(newTime,newRoom));
            }
        }

        return getMaxUsageRoom(free,busyRooms,n);
    }

    public int getMaxUsageRoom(  PriorityQueue<Rooms> free ,PriorityQueue<BusyRooms> busyRooms,int n ){
        int[] count = new int[n];

        for(Rooms room: free){
            count[room.getRoomId()] = room.getMeetingCounts();
        }

        for(BusyRooms getBusyRooms: busyRooms ){
            count[getBusyRooms.getRoom().getRoomId()] = getBusyRooms.getRoom().getMeetingCounts();
        }

        int ans = -1,cnt = Integer.MIN_VALUE;

        for(int i=0;i<n;i++){
            if(count[i]>cnt){
                ans = i;
                cnt = count[i];
            }
        }
        return ans;
    }
}
