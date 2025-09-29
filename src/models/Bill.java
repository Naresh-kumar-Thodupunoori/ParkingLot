package models;

import enums.Payment;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a bill generated at exit
 * Follows Single Responsibility Principle - manages billing information only
 */
public class Bill {
    private UUID billId;
    private LocalDateTime exitTime;
    private double totalAmount;
    private Ticket ticket;
    private Payment paymentMethod;
    private boolean isPaid;
    private String exitGateId;
    
    public Bill(Ticket ticket, double totalAmount, String exitGateId) {
        this.billId = UUID.randomUUID();
        this.ticket = ticket;
        this.totalAmount = totalAmount;
        this.exitTime = LocalDateTime.now();
        this.exitGateId = exitGateId;
        this.isPaid = false;
    }
    
    // Getters
    public UUID getBillId() {
        return billId;
    }
    
    public LocalDateTime getExitTime() {
        return exitTime;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public Ticket getTicket() {
        return ticket;
    }
    
    public Payment getPaymentMethod() {
        return paymentMethod;
    }
    
    public boolean isPaid() {
        return isPaid;
    }
    
    public String getExitGateId() {
        return exitGateId;
    }
    
    // Setters
    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public void setPaymentMethod(Payment paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public void setPaid(boolean paid) {
        isPaid = paid;
    }
    
    public void setExitGateId(String exitGateId) {
        this.exitGateId = exitGateId;
    }
    
    /**
     * Process payment for this bill
     * @param paymentMethod The payment method used
     * @return true if payment is successful
     */
    public boolean processPayment(Payment paymentMethod) {
        this.paymentMethod = paymentMethod;
        this.isPaid = true;
        return true; // In real system, this would integrate with payment gateway
    }
    
    /**
     * Get total parking duration from ticket
     * @return Duration in hours
     */
    public long getTotalParkingHours() {
        return java.time.Duration.between(ticket.getEntryTime(), exitTime).toHours();
    }
    
    /**
     * Get breakdown of charges
     * @return String representation of charge breakdown
     */
    public String getChargeBreakdown() {
        StringBuilder breakdown = new StringBuilder();
        breakdown.append("Parking Duration: ").append(getTotalParkingHours()).append(" hours\n");
        breakdown.append("Base Charge: $").append(String.format("%.2f", totalAmount)).append("\n");
        
        if (ticket.getVehicle().requiresCharging() && 
            ticket.getParkingSlot().isChargingAvailable()) {
            breakdown.append("EV Charging: Included\n");
        }
        
        breakdown.append("Total Amount: $").append(String.format("%.2f", totalAmount));
        return breakdown.toString();
    }
    
    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", exitTime=" + exitTime +
                ", totalAmount=" + String.format("%.2f", totalAmount) +
                ", ticket=" + ticket.getTicketId() +
                ", paymentMethod=" + paymentMethod +
                ", isPaid=" + isPaid +
                ", exitGateId='" + exitGateId + '\'' +
                '}';
    }
}
