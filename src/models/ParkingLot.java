package models;

import interfaces.SlotAllocationStrategy;
import interfaces.PricingStrategy;
import enums.Payment;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Main ParkingLot class that orchestrates the entire parking system
 * Follows Single Responsibility Principle - manages overall parking lot operations
 * Follows Dependency Inversion Principle - depends on abstractions (interfaces)
 */
public class ParkingLot {
    private List<ParkingFloor> floors;
    private List<EntryGate> entryGates;
    private List<ExitGate> exitGates;
    private SlotAllocationStrategy slotAllocationStrategy;
    private PricingStrategy pricingStrategy;
    private Map<String, Ticket> activeTickets; // vehicleNo -> Ticket mapping
    
    public ParkingLot(SlotAllocationStrategy slotAllocationStrategy, PricingStrategy pricingStrategy) {
        this.floors = new ArrayList<>();
        this.entryGates = new ArrayList<>();
        this.exitGates = new ArrayList<>();
        this.slotAllocationStrategy = slotAllocationStrategy;
        this.pricingStrategy = pricingStrategy;
        this.activeTickets = new HashMap<>();
    }
    
    // Getters
    public List<ParkingFloor> getFloors() {
        return new ArrayList<>(floors);
    }
    
    public List<EntryGate> getEntryGates() {
        return new ArrayList<>(entryGates);
    }
    
    public List<ExitGate> getExitGates() {
        return new ArrayList<>(exitGates);
    }
    
    public SlotAllocationStrategy getSlotAllocationStrategy() {
        return slotAllocationStrategy;
    }
    
    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }
    
    // Setters
    public void setSlotAllocationStrategy(SlotAllocationStrategy slotAllocationStrategy) {
        this.slotAllocationStrategy = slotAllocationStrategy;
        // Update all entry gates with new strategy
        for (EntryGate gate : entryGates) {
            gate.setSlotAllocationStrategy(slotAllocationStrategy);
        }
    }
    
    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
        // Update all exit gates with new strategy
        for (ExitGate gate : exitGates) {
            gate.setPricingStrategy(pricingStrategy);
        }
    }
    
    /**
     * Add a parking floor to the parking lot
     * @param floor The parking floor to add
     */
    public void addFloor(ParkingFloor floor) {
        floors.add(floor);
        System.out.println("Floor " + floor.getFloorId() + " added to parking lot");
    }
    
    /**
     * Add an entry gate to the parking lot
     * @param entryGate The entry gate to add
     */
    public void addEntryGate(EntryGate entryGate) {
        entryGates.add(entryGate);
        System.out.println("Entry gate " + entryGate.getEntryGateId() + " added");
    }
    
    /**
     * Add an exit gate to the parking lot
     * @param exitGate The exit gate to add
     */
    public void addExitGate(ExitGate exitGate) {
        exitGates.add(exitGate);
        System.out.println("Exit gate " + exitGate.getExitGateId() + " added");
    }
    
    /**
     * Park a vehicle using a specific entry gate
     * @param vehicle The vehicle to park
     * @param entryGateId The ID of the entry gate
     * @return The generated ticket if successful, null otherwise
     */
    public Ticket parkVehicle(Vehicle vehicle, String entryGateId) {
        // Find the entry gate
        EntryGate entryGate = findEntryGate(entryGateId);
        if (entryGate == null) {
            System.out.println("Entry gate not found: " + entryGateId);
            return null;
        }
        
        // Check if vehicle is already parked
        if (activeTickets.containsKey(vehicle.getVehicleNo())) {
            System.out.println("Vehicle " + vehicle.getVehicleNo() + " is already parked");
            return null;
        }
        
        // Generate ticket using entry gate
        Ticket ticket = entryGate.generateTicket(vehicle, floors);
        
        if (ticket != null) {
            // Store active ticket
            activeTickets.put(vehicle.getVehicleNo(), ticket);
            System.out.println("Ticket generated: " + ticket.getTicketId());
        }
        
        return ticket;
    }
    
    /**
     * Process vehicle exit using a specific exit gate
     * @param vehicleNo The vehicle number
     * @param exitGateId The ID of the exit gate
     * @param paymentMethod The payment method
     * @return true if exit is successful
     */
    public boolean exitVehicle(String vehicleNo, String exitGateId, Payment paymentMethod) {
        // Find the exit gate
        ExitGate exitGate = findExitGate(exitGateId);
        if (exitGate == null) {
            System.out.println("Exit gate not found: " + exitGateId);
            return false;
        }
        
        // Find the active ticket
        Ticket ticket = activeTickets.get(vehicleNo);
        if (ticket == null) {
            System.out.println("No active ticket found for vehicle: " + vehicleNo);
            return false;
        }
        
        // Process checkout
        boolean success = exitGate.checkout(ticket, paymentMethod);
        
        if (success) {
            // Remove from active tickets
            activeTickets.remove(vehicleNo);
            System.out.println("Vehicle " + vehicleNo + " successfully exited");
        }
        
        return success;
    }
    
    /**
     * Find entry gate by ID
     * @param entryGateId The entry gate ID
     * @return The entry gate or null if not found
     */
    private EntryGate findEntryGate(String entryGateId) {
        return entryGates.stream()
                .filter(gate -> gate.getEntryGateId().equals(entryGateId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Find exit gate by ID
     * @param exitGateId The exit gate ID
     * @return The exit gate or null if not found
     */
    private ExitGate findExitGate(String exitGateId) {
        return exitGates.stream()
                .filter(gate -> gate.getExitGateId().equals(exitGateId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Get parking lot capacity statistics
     * @return Capacity information as string
     */
    public String getCapacityInfo() {
        int totalSlots = floors.stream().mapToInt(floor -> floor.getParkingSlots().size()).sum();
        int availableSlots = floors.stream().mapToInt(ParkingFloor::getTotalAvailableSlots).sum();
        int occupiedSlots = floors.stream().mapToInt(ParkingFloor::getTotalOccupiedSlots).sum();
        
        StringBuilder info = new StringBuilder();
        info.append("=== Parking Lot Capacity ===\n");
        info.append("Total Slots: ").append(totalSlots).append("\n");
        info.append("Available Slots: ").append(availableSlots).append("\n");
        info.append("Occupied Slots: ").append(occupiedSlots).append("\n");
        info.append("Occupancy Rate: ").append(String.format("%.1f%%", 
                (occupiedSlots * 100.0) / totalSlots)).append("\n");
        
        info.append("\nFloor-wise Details:\n");
        for (ParkingFloor floor : floors) {
            info.append("Floor ").append(floor.getFloorId()).append(": ")
                .append(floor.getTotalAvailableSlots()).append("/")
                .append(floor.getParkingSlots().size()).append(" available\n");
        }
        
        return info.toString();
    }
    
    /**
     * Print detailed status of all floors
     */
    public void printDetailedStatus() {
        System.out.println(getCapacityInfo());
        System.out.println("Active Tickets: " + activeTickets.size());
        System.out.println("Entry Gates: " + entryGates.size());
        System.out.println("Exit Gates: " + exitGates.size());
        
        System.out.println("\n=== Floor Details ===");
        for (ParkingFloor floor : floors) {
            floor.printSlots();
        }
    }
    
    /**
     * Check if parking lot is full
     * @return true if no slots are available
     */
    public boolean isFull() {
        return floors.stream().noneMatch(floor -> floor.getTotalAvailableSlots() > 0);
    }
    
    /**
     * Get ticket for a vehicle
     * @param vehicleNo The vehicle number
     * @return The active ticket or null if not found
     */
    public Ticket getTicket(String vehicleNo) {
        return activeTickets.get(vehicleNo);
    }
    
    /**
     * Initialize parking lot with default configuration
     */
    public void initializeDefault() {
        // Create floors (Ground floor and 3 parking floors)
        for (int i = 0; i <= 3; i++) {
            ParkingFloor floor = new ParkingFloor(i);
            if (i == 0) {
                // Ground floor - fewer slots, more for gates
                floor.initializeSlots(5, 5, 2, 30.0);
            } else {
                // Regular parking floors
                floor.initializeSlots(10, 8, 4, 25.0);
            }
            addFloor(floor);
        }
        
        // Create entry gates on ground floor
        EntryGate entryGate1 = new EntryGate("ENTRY_01", 0, slotAllocationStrategy);
        EntryGate entryGate2 = new EntryGate("ENTRY_02", 0, slotAllocationStrategy);
        addEntryGate(entryGate1);
        addEntryGate(entryGate2);
        
        // Create exit gates on ground floor
        ExitGate exitGate1 = new ExitGate("EXIT_01", 0, pricingStrategy);
        ExitGate exitGate2 = new ExitGate("EXIT_02", 0, pricingStrategy);
        addExitGate(exitGate1);
        addExitGate(exitGate2);
        
        System.out.println("Parking lot initialized with default configuration");
        System.out.println(getCapacityInfo());
    }
}
