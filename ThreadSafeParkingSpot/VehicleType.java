package ThreadSafeParkingSpot;

public enum VehicleType {
    TWO_WHEELER(2),
    FOUR_WHEELER(4);
    final int code;

    VehicleType(int code){
        this.code = code;
    }

    static VehicleType fromCode(int c){
        for(VehicleType v: values()) if(v.code == c) return v;
        throw new IllegalArgumentException("unkown vehicle type: " + c );
    }
}
