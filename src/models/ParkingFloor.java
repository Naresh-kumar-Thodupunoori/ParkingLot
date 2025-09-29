package models;

import enums.SlotType;
import enums.SlotStatus;
import enums.VehicleType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a parking floor in the parking lot
 * Follows Single Responsibility Principle - manages floor operations and slot collections
 */
public class ParkingFloor {
    private int floorId;
    private List<ParkingSlot> parkingSlots;
    private Map<SlotType, List<ParkingSlot>> slotsByType;
    
    public ParkingFloor(int floorId) {
        this.floorId = floorId;
        this.parkingSlots = new ArrayList<>();
        this.slotsByType = new HashMap<>();
        initializeSlotsByType();
    }
    
    private void initializeSlotsByType() {
        for (SlotType slotType : SlotType.values()) {
            slotsByType.put(slotType, new ArrayList<>());
        }
    }
    
    // Getters
    public int getFloorId() {
        return floorId;
    }
    
    public List<ParkingSlot> getParkingSlots() {
        return new ArrayList<>(parkingSlots); // Return copy to maintain encapsulation
    }
    
    /**
     * Add a parking slot to this floor
     * @param slot The parking slot to add
     */
    public void addParkingSlot(ParkingSlot slot) {
        if (slot.getFloorNumber() != this.floorId) {
            throw new IllegalArgumentException("Slot floor number must match this floor ID");
        }
        
        parkingSlots.add(slot);
        slotsByType.get(slot.getSlotType()).add(slot);
    }
    
    /**
     * Get available slots for a specific vehicle type
     * Follows Liskov Substitution Principle - smaller vehicles can use larger slots
     * 
     * @param vehicleType The type of vehicle
     * @return List of available compatible slots
     */
    public List<ParkingSlot> getAvailableSlots(VehicleType vehicleType) {
        List<ParkingSlot> availableSlots = new ArrayList<>();
        
        for (ParkingSlot slot : parkingSlots) {
            if (slot.getSlotStatus() == SlotStatus.EMPTY && 
                slot.getSlotType().canFit(vehicleType)) {
                availableSlots.add(slot);
            }
        }
        
        return availableSlots;
    }
    
    /**
     * Get available slots for a specific vehicle (considering charging requirements)
     * @param vehicle The vehicle object
     * @return List of available compatible slots
     */
    public List<ParkingSlot> getAvailableSlots(Vehicle vehicle) {
        List<ParkingSlot> availableSlots = new ArrayList<>();
        
        for (ParkingSlot slot : parkingSlots) {
            if (slot.canAccommodate(vehicle)) {
                availableSlots.add(slot);
            }
        }
        
        return availableSlots;
    }
    
    /**
     * Get slots by type
     * @param slotType The type of slot
     * @return List of slots of the specified type
     */
    public List<ParkingSlot> getSlotsByType(SlotType slotType) {
        return new ArrayList<>(slotsByType.get(slotType));
    }
    
    /**
     * Get count of available slots by type
     * @param slotType The type of slot
     * @return Number of available slots
     */
    public int getAvailableSlotsCount(SlotType slotType) {
        return (int) slotsByType.get(slotType).stream()
                .filter(slot -> slot.getSlotStatus() == SlotStatus.EMPTY)
                .count();
    }
    
    /**
     * Get total count of available slots on this floor
     * @return Total number of available slots
     */
    public int getTotalAvailableSlots() {
        return (int) parkingSlots.stream()
                .filter(slot -> slot.getSlotStatus() == SlotStatus.EMPTY)
                .count();
    }
    
    /**
     * Get total count of occupied slots on this floor
     * @return Total number of occupied slots
     */
    public int getTotalOccupiedSlots() {
        return (int) parkingSlots.stream()
                .filter(slot -> slot.getSlotStatus() == SlotStatus.FILLED)
                .count();
    }
    
    /**
     * Check if floor has capacity for the given vehicle type
     * @param vehicleType The vehicle type to check
     * @return true if capacity is available
     */
    public boolean hasCapacity(VehicleType vehicleType) {
        return !getAvailableSlots(vehicleType).isEmpty();
    }
    
    /**
     * Check if floor has capacity for the given vehicle (including charging requirements)
     * @param vehicle The vehicle to check
     * @return true if capacity is available
     */
    public boolean hasCapacity(Vehicle vehicle) {
        return !getAvailableSlots(vehicle).isEmpty();
    }
    
    /**
     * Print the status of all slots on this floor
     */
    public void printSlots() {
        System.out.println("=== Floor " + floorId + " Status ===");
        System.out.println("Total Slots: " + parkingSlots.size());
        System.out.println("Available: " + getTotalAvailableSlots());
        System.out.println("Occupied: " + getTotalOccupiedSlots());
        
        System.out.println("\nSlots by Type:");
        for (SlotType type : SlotType.values()) {
            int available = getAvailableSlotsCount(type);
            int total = slotsByType.get(type).size();
            System.out.println(type + ": " + available + "/" + total + " available");
        }
        
        System.out.println("\nSlot Details:");
        for (ParkingSlot slot : parkingSlots) {
            System.out.println("  " + slot);
        }
        System.out.println("================================");
    }
    
    /**
     * Initialize floor with default slot configuration
     * @param smallSlots Number of small slots
     * @param mediumSlots Number of medium slots  
     * @param largeSlots Number of large slots
     * @param chargingSlotsPercentage Percentage of slots with charging capability
     */
    public void initializeSlots(int smallSlots, int mediumSlots, int largeSlots, 
                               double chargingSlotsPercentage) {
        int slotNumber = 1;
        
        // Add small slots
        for (int i = 0; i < smallSlots; i++) {
            boolean hasCharging = Math.random() < (chargingSlotsPercentage / 100.0);
            String slotId = "F" + floorId + "S" + slotNumber;
            ParkingSlot slot = new ParkingSlot(slotId, SlotType.SMALL, hasCharging, floorId, slotNumber);
            addParkingSlot(slot);
            slotNumber++;
        }
        
        // Add medium slots
        for (int i = 0; i < mediumSlots; i++) {
            boolean hasCharging = Math.random() < (chargingSlotsPercentage / 100.0);
            String slotId = "F" + floorId + "M" + slotNumber;
            ParkingSlot slot = new ParkingSlot(slotId, SlotType.MEDIUM, hasCharging, floorId, slotNumber);
            addParkingSlot(slot);
            slotNumber++;
        }
        
        // Add large slots
        for (int i = 0; i < largeSlots; i++) {
            boolean hasCharging = Math.random() < (chargingSlotsPercentage / 100.0);
            String slotId = "F" + floorId + "L" + slotNumber;
            ParkingSlot slot = new ParkingSlot(slotId, SlotType.LARGE, hasCharging, floorId, slotNumber);
            addParkingSlot(slot);
            slotNumber++;
        }
    }
    
    @Override
    public String toString() {
        return "ParkingFloor{" +
                "floorId=" + floorId +
                ", totalSlots=" + parkingSlots.size() +
                ", availableSlots=" + getTotalAvailableSlots() +
                ", occupiedSlots=" + getTotalOccupiedSlots() +
                '}';
    }
}
