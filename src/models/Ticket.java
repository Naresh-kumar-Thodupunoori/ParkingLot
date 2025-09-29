package models;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a parking ticket issued at entry
 * Follows Single Responsibility Principle - manages ticket information only
 */
public class Ticket {
    private UUID ticketId;
    private Vehicle vehicle;
    private ParkingSlot parkingSlot;
    private LocalDateTime entryTime;
    private String entryGateId;
    
    public Ticket(Vehicle vehicle, ParkingSlot parkingSlot, String entryGateId) {
        this.ticketId = UUID.randomUUID();
        this.vehicle = vehicle;
        this.parkingSlot = parkingSlot;
        this.entryTime = LocalDateTime.now();
        this.entryGateId = entryGateId;
    }
    
    // Getters
    public UUID getTicketId() {
        return ticketId;
    }
    
    public Vehicle getVehicle() {
        return vehicle;
    }
    
    public ParkingSlot getParkingSlot() {
        return parkingSlot;
    }
    
    public LocalDateTime getEntryTime() {
        return entryTime;
    }
    
    public String getEntryGateId() {
        return entryGateId;
    }
    
    // Setters
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    
    public void setParkingSlot(ParkingSlot parkingSlot) {
        this.parkingSlot = parkingSlot;
    }
    
    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }
    
    public void setEntryGateId(String entryGateId) {
        this.entryGateId = entryGateId;
    }
    
    /**
     * Get the parking duration in hours
     * @return Duration in hours from entry time to now
     */
    public long getParkingDurationInHours() {
        return java.time.Duration.between(entryTime, LocalDateTime.now()).toHours();
    }
    
    /**
     * Get the parking duration in minutes
     * @return Duration in minutes from entry time to now
     */
    public long getParkingDurationInMinutes() {
        return java.time.Duration.between(entryTime, LocalDateTime.now()).toMinutes();
    }
    
    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", vehicle=" + vehicle.getVehicleNo() +
                ", parkingSlot=" + parkingSlot.getSlotId() +
                ", entryTime=" + entryTime +
                ", entryGateId='" + entryGateId + '\'' +
                '}';
    }
}
