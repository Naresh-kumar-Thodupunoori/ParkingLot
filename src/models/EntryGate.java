package models;

import interfaces.SlotAllocationStrategy;
import java.util.List;

/**
 * Represents an entry gate in the parking lot
 * Follows Single Responsibility Principle - handles vehicle entry operations only
 */
public class EntryGate {
    private String entryGateId;
    private int floor;
    private SlotAllocationStrategy slotAllocationStrategy;
    
    public EntryGate(String entryGateId, int floor, SlotAllocationStrategy slotAllocationStrategy) {
        this.entryGateId = entryGateId;
        this.floor = floor;
        this.slotAllocationStrategy = slotAllocationStrategy;
    }
    
    // Getters
    public String getEntryGateId() {
        return entryGateId;
    }
    
    public int getFloor() {
        return floor;
    }
    
    public SlotAllocationStrategy getSlotAllocationStrategy() {
        return slotAllocationStrategy;
    }
    
    // Setters
    public void setEntryGateId(String entryGateId) {
        this.entryGateId = entryGateId;
    }
    
    public void setFloor(int floor) {
        this.floor = floor;
    }
    
    public void setSlotAllocationStrategy(SlotAllocationStrategy slotAllocationStrategy) {
        this.slotAllocationStrategy = slotAllocationStrategy;
    }
    
    /**
     * Generate a parking ticket for a vehicle
     * Follows Open/Closed Principle - uses strategy pattern for slot allocation
     * 
     * @param vehicle The vehicle requesting parking
     * @param floors List of available parking floors
     * @return Generated ticket if parking is successful, null otherwise
     */
    public Ticket generateTicket(Vehicle vehicle, List<ParkingFloor> floors) {
        // Use strategy pattern to find and allocate a suitable parking slot
        ParkingSlot allocatedSlot = slotAllocationStrategy.allocateParkingSlot(vehicle, floors, this.floor);
        
        if (allocatedSlot == null) {
            System.out.println("No suitable parking slot available for vehicle: " + vehicle.getVehicleNo());
            return null;
        }
        
        // Park the vehicle in the allocated slot
        if (allocatedSlot.parkVehicle(vehicle)) {
            // Generate and return the ticket
            Ticket ticket = new Ticket(vehicle, allocatedSlot, this.entryGateId);
            System.out.println("Vehicle " + vehicle.getVehicleNo() + " parked in slot " + 
                             allocatedSlot.getSlotId() + " on floor " + allocatedSlot.getFloorNumber());
            return ticket;
        } else {
            System.out.println("Failed to park vehicle: " + vehicle.getVehicleNo());
            return null;
        }
    }
    
    /**
     * Check if parking lot has capacity for the vehicle type
     * @param vehicle The vehicle to check capacity for
     * @param floors List of parking floors
     * @return true if capacity is available
     */
    public boolean hasCapacity(Vehicle vehicle, List<ParkingFloor> floors) {
        return slotAllocationStrategy.allocateParkingSlot(vehicle, floors, this.floor) != null;
    }
    
    @Override
    public String toString() {
        return "EntryGate{" +
                "entryGateId='" + entryGateId + '\'' +
                ", floor=" + floor +
                ", slotAllocationStrategy=" + slotAllocationStrategy.getClass().getSimpleName() +
                '}';
    }
}
