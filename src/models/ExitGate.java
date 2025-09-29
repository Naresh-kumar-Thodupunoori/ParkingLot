package models;

import interfaces.PricingStrategy;
import enums.Payment;

/**
 * Represents an exit gate in the parking lot
 * Follows Single Responsibility Principle - handles vehicle exit operations only
 */
public class ExitGate {
    private String exitGateId;
    private int floor;
    private PricingStrategy pricingStrategy;
    
    public ExitGate(String exitGateId, int floor, PricingStrategy pricingStrategy) {
        this.exitGateId = exitGateId;
        this.floor = floor;
        this.pricingStrategy = pricingStrategy;
    }
    
    // Getters
    public String getExitGateId() {
        return exitGateId;
    }
    
    public int getFloor() {
        return floor;
    }
    
    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }
    
    // Setters
    public void setExitGateId(String exitGateId) {
        this.exitGateId = exitGateId;
    }
    
    public void setFloor(int floor) {
        this.floor = floor;
    }
    
    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }
    
    /**
     * Generate a bill for a parked vehicle using the ticket
     * Follows Open/Closed Principle - uses strategy pattern for pricing
     * 
     * @param ticket The parking ticket
     * @return Generated bill
     */
    public Bill generateBill(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }
        
        // Calculate the total amount using pricing strategy
        double totalAmount = pricingStrategy.calculatePrice(ticket);
        
        // Create and return the bill
        Bill bill = new Bill(ticket, totalAmount, this.exitGateId);
        
        System.out.println("Bill generated for vehicle " + ticket.getVehicle().getVehicleNo() + 
                         " - Amount: $" + String.format("%.2f", totalAmount));
        
        return bill;
    }
    
    /**
     * Process vehicle exit after payment
     * @param bill The bill to process
     * @param paymentMethod The payment method used
     * @return true if exit is successful
     */
    public boolean processExit(Bill bill, Payment paymentMethod) {
        if (bill == null) {
            System.out.println("Invalid bill");
            return false;
        }
        
        if (bill.isPaid()) {
            // Remove vehicle from parking slot
            ParkingSlot slot = bill.getTicket().getParkingSlot();
            Vehicle exitingVehicle = slot.removeVehicle();
            
            if (exitingVehicle != null) {
                System.out.println("Vehicle " + exitingVehicle.getVehicleNo() + 
                                 " successfully exited from slot " + slot.getSlotId());
                return true;
            } else {
                System.out.println("Error: Vehicle not found in the specified slot");
                return false;
            }
        } else {
            // Process payment first
            if (bill.processPayment(paymentMethod)) {
                return processExit(bill, paymentMethod);
            } else {
                System.out.println("Payment failed");
                return false;
            }
        }
    }
    
    /**
     * Complete checkout process - generate bill and process payment
     * @param ticket The parking ticket
     * @param paymentMethod The payment method
     * @return true if checkout is successful
     */
    public boolean checkout(Ticket ticket, Payment paymentMethod) {
        try {
            Bill bill = generateBill(ticket);
            return processExit(bill, paymentMethod);
        } catch (Exception e) {
            System.out.println("Checkout failed: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public String toString() {
        return "ExitGate{" +
                "exitGateId='" + exitGateId + '\'' +
                ", floor=" + floor +
                ", pricingStrategy=" + pricingStrategy.getClass().getSimpleName() +
                '}';
    }
}
