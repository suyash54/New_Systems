package ThreadSafeParkingSpot;

import lombok.Getter;
import lombok.Setter;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;


@Getter
@Setter
public class ParkingFloor {
    private final int floorNumber;
    private final int rows,cols;
    private final ParkingSpot[][] grid;
    private final Map<VehicleType, TreeSet<Integer>> freeByType = new EnumMap<>(VehicleType.class);

    @FunctionalInterface
    interface SpotFactory {
        ParkingSpot create(int floor, int row, int col, VehicleType type);
    }

    private static final Map<VehicleType, SpotFactory> factoryMap = new EnumMap<>(VehicleType.class);

    static {
        factoryMap.put(VehicleType.TWO_WHEELER,  TwoWheelerParkingSpot::new);
        factoryMap.put(VehicleType.FOUR_WHEELER, FourWheelerParkingSpot::new);
    }


    ParkingFloor(int floorNumber , int[][] layout){
        this.floorNumber = floorNumber;
        this.rows = layout.length;
        this.cols = layout[0].length;
        this.grid = new ParkingSpot[rows][cols];

        for(VehicleType t: VehicleType.values()){
            freeByType.put(t,new TreeSet<>());
        }

        for(int r=0;r<rows;r++){
            for(int c=0;c<cols;c++){
                int code = layout[r][c];
                VehicleType t = VehicleType.fromCode(code);
                grid[r][c] = factoryMap.get(t).create(floorNumber,r,c,t);
                freeByType.get(t).add(r * cols + c);
            }
        }
    }

    int freeCount(VehicleType t){ return freeByType.get(t).size(); }


    Optional<ParkingSpot> peekLowest(VehicleType t){
        TreeSet<Integer> set = freeByType.get(t);
        if(set.isEmpty())
             return Optional.empty();

        int idx = set.first();
        return Optional.of(grid[idx/cols][idx%cols]);
    }

    void occupy(ParkingSpot s){
        freeByType.get(s.getType()).remove(s.getRow()*cols + s.getCol());
    }

    void vacate(ParkingSpot s){
        freeByType.get(s.getType()).add(s.getRow()*cols + s.getCol());
    }

    ParkingSpot getSpot(int r,int c){
        return (r>=0 && r< rows && c>=0 && c<cols) ? grid[r][c]:null;
    }

}
