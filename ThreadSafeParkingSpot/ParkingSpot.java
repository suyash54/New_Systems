package ThreadSafeParkingSpot;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ParkingSpot {
    private final String spotId;
    private final int floor,row,col;
    private final VehicleType type;
    private Ticket ticket;

    ParkingSpot(int floor,int row,int col,VehicleType type){
        this.floor = floor;
        this.row = row;
        this.col = col;
        this.type = type;
        this.spotId = floor + "-"  + row + "-" + col;
    }

    boolean isOccupied(){
        return ticket != null;
    }

    void assign(Ticket t){
        this.ticket = t;
    }

    void release(){
        this.ticket = null;
    }
}
