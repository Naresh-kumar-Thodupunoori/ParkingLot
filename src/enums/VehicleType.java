package enums;

public enum VehicleType {
    BIKE(1),
    CAR(2), 
    AUTO(2),
    BUS(4);
    
    private final int size;
    
    VehicleType(int size) {
        this.size = size;
    }
    
    public int getSizeUnits() {
        return size;
    }
}
