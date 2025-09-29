package models;

import enums.SlotType;
import enums.SlotStatus;

public class ParkingSlot {
    private String slotId;
    private SlotType slotType;
    private SlotStatus slotStatus;
    private boolean chargingAvailable;
    private Vehicle currentVehicle;
    private int floorNumber;
    private int slotNumber;
    
    public ParkingSlot(String slotId, SlotType slotType, boolean chargingAvailable, 
                      int floorNumber, int slotNumber) {
        this.slotId = slotId;
        this.slotType = slotType;
        this.slotStatus = SlotStatus.EMPTY;
        this.chargingAvailable = chargingAvailable;
        this.currentVehicle = null;
        this.floorNumber = floorNumber;
        this.slotNumber = slotNumber;
    }
    
    // Getters
    public String getSlotId() {
        return slotId;
    }
    
    public SlotType getSlotType() {
        return slotType;
    }
    
    public SlotStatus getSlotStatus() {
        return slotStatus;
    }
    
    public boolean isChargingAvailable() {
        return chargingAvailable;
    }
    
    public Vehicle getCurrentVehicle() {
        return currentVehicle;
    }
    
    public int getFloorNumber() {
        return floorNumber;
    }
    
    public int getSlotNumber() {
        return slotNumber;
    }
    
    // Setters
    public void setSlotStatus(SlotStatus slotStatus) {
        this.slotStatus = slotStatus;
    }
    
    public void setChargingAvailable(boolean chargingAvailable) {
        this.chargingAvailable = chargingAvailable;
    }
    
    public void setCurrentVehicle(Vehicle currentVehicle) {
        this.currentVehicle = currentVehicle;
    }
    
    /**
     * Check if this slot can accommodate the given vehicle
     * @param vehicle The vehicle to check compatibility for
     * @return true if vehicle can be parked in this slot
     */
    public boolean canAccommodate(Vehicle vehicle) {
        // Slot must be empty
        if (this.slotStatus != SlotStatus.EMPTY) {
            return false;
        }
        
        // Check size compatibility
        if (!this.slotType.canFit(vehicle.getVehicleType())) {
            return false;
        }
        
        // Check charging requirement
        if (vehicle.requiresCharging() && !this.chargingAvailable) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Park a vehicle in this slot
     * @param vehicle The vehicle to park
     * @return true if successfully parked, false otherwise
     */
    public boolean parkVehicle(Vehicle vehicle) {
        if (canAccommodate(vehicle)) {
            this.currentVehicle = vehicle;
            this.slotStatus = SlotStatus.FILLED;
            return true;
        }
        return false;
    }
    
    /**
     * Remove vehicle from this slot
     * @return The vehicle that was parked, null if slot was empty
     */
    public Vehicle removeVehicle() {
        Vehicle vehicle = this.currentVehicle;
        this.currentVehicle = null;
        this.slotStatus = SlotStatus.EMPTY;
        return vehicle;
    }
    
    /**
     * Calculate distance from entry gate (simplified as floor difference + slot number)
     * @param entryFloor The floor number of the entry gate
     * @return Distance metric for nearest slot calculation
     */
    public int getDistanceFromEntry(int entryFloor) {
        return Math.abs(this.floorNumber - entryFloor) * 100 + this.slotNumber;
    }
    
    @Override
    public String toString() {
        return "ParkingSlot{" +
                "slotId='" + slotId + '\'' +
                ", slotType=" + slotType +
                ", slotStatus=" + slotStatus +
                ", chargingAvailable=" + chargingAvailable +
                ", floorNumber=" + floorNumber +
                ", slotNumber=" + slotNumber +
                ", currentVehicle=" + (currentVehicle != null ? currentVehicle.getVehicleNo() : "None") +
                '}';
    }
}
