package enums;

public enum SlotType {
    SMALL(1),
    MEDIUM(2),
    LARGE(4);
    
    private final int capacity;
    
    SlotType(int capacity) {
        this.capacity = capacity;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public boolean canFit(VehicleType vehicleType) {
        return this.capacity >= vehicleType.getSizeUnits();
    }
}
