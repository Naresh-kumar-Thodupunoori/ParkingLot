package strategies;

import interfaces.PricingStrategy;
import models.Ticket;
import enums.VehicleType;

public class PerHourPricingSt implements PricingStrategy {
    
    private static final double BIKE_HOURLY_RATE = 2.0;
    private static final double CAR_HOURLY_RATE = 4.0;
    private static final double AUTO_HOURLY_RATE = 3.5;
    private static final double BUS_HOURLY_RATE = 8.0;
    
    private static final double EV_CHARGING_FEE = 5.0;
    
    private static final double MINIMUM_HOURS = 1.0;
    
    @Override
    public double calculatePrice(Ticket ticket) {
        if (ticket == null) {
            return 0.0;
        }
        
        long durationMinutes = ticket.getParkingDurationInMinutes();
        double durationHours = Math.max(MINIMUM_HOURS, Math.ceil(durationMinutes / 60.0));
        
        double hourlyRate = getHourlyRate(ticket.getVehicle().getVehicleType());
        
        double parkingCost = hourlyRate * durationHours;
        
        double chargingFee = 0.0;
        if (ticket.getVehicle().requiresCharging() && ticket.getParkingSlot().isChargingAvailable()) {
            chargingFee = EV_CHARGING_FEE;
        }
        
        double totalCost = parkingCost + chargingFee;
        
        return Math.round(totalCost * 100.0) / 100.0; 
    }
    
    /**
     * Get hourly rate based on vehicle type
     * @param vehicleType The type of vehicle
     * @return Hourly rate in dollars
     */
    private double getHourlyRate(VehicleType vehicleType) {
        switch (vehicleType) {
            case BIKE:
                return BIKE_HOURLY_RATE;
            case CAR:
                return CAR_HOURLY_RATE;
            case AUTO:
                return AUTO_HOURLY_RATE;
            case BUS:
                return BUS_HOURLY_RATE;
            default:
                return CAR_HOURLY_RATE; 
        }
    }
    
    /**
     * Get price breakdown for transparency
     * @param ticket The parking ticket
     * @return Detailed breakdown as string
     */
    public String getPriceBreakdown(Ticket ticket) {
        if (ticket == null) {
            return "Invalid ticket";
        }
        
        long durationMinutes = ticket.getParkingDurationInMinutes();
        double durationHours = Math.max(MINIMUM_HOURS, Math.ceil(durationMinutes / 60.0));
        double hourlyRate = getHourlyRate(ticket.getVehicle().getVehicleType());
        double parkingCost = hourlyRate * durationHours;
        
        double chargingFee = 0.0;
        if (ticket.getVehicle().requiresCharging() && ticket.getParkingSlot().isChargingAvailable()) {
            chargingFee = EV_CHARGING_FEE;
        }
        
        double totalCost = parkingCost + chargingFee;
        
        StringBuilder breakdown = new StringBuilder();
        breakdown.append("=== SIMPLE PRICING BREAKDOWN ===\n");
        breakdown.append("Vehicle: ").append(ticket.getVehicle().getVehicleNo()).append("\n");
        breakdown.append("Vehicle Type: ").append(ticket.getVehicle().getVehicleType()).append("\n");
        breakdown.append("Duration: ").append(String.format("%.1f hours", durationHours)).append("\n");
        breakdown.append("Hourly Rate: $").append(String.format("%.2f", hourlyRate)).append("\n");
        breakdown.append("Parking Cost: $").append(String.format("%.2f", parkingCost)).append("\n");
        
        if (chargingFee > 0) {
            breakdown.append("EV Charging Fee: $").append(String.format("%.2f", chargingFee)).append("\n");
        }
        
        breakdown.append("TOTAL: $").append(String.format("%.2f", totalCost));
        
        return breakdown.toString();
    }
    
    /**
     * Calculate estimated cost for a given duration (for customer information)
     * @param vehicleType The type of vehicle
     * @param hours Number of hours
     * @param needsCharging Whether EV charging is needed
     * @return Estimated cost
     */
    public double estimateCost(VehicleType vehicleType, double hours, boolean needsCharging) {
        double durationHours = Math.max(MINIMUM_HOURS, Math.ceil(hours));
        double hourlyRate = getHourlyRate(vehicleType);
        double parkingCost = hourlyRate * durationHours;
        double chargingFee = needsCharging ? EV_CHARGING_FEE : 0.0;
        
        return Math.round((parkingCost + chargingFee) * 100.0) / 100.0;
    }
    
    /**
     * Get rate information for display purposes
     * @return String containing current rates
     */
    public String getRateInformation() {
        StringBuilder rates = new StringBuilder();
        rates.append("=== HOURLY RATES ===\n");
        rates.append("Bike: $").append(String.format("%.2f/hour", BIKE_HOURLY_RATE)).append("\n");
        rates.append("Car: $").append(String.format("%.2f/hour", CAR_HOURLY_RATE)).append("\n");
        rates.append("Auto: $").append(String.format("%.2f/hour", AUTO_HOURLY_RATE)).append("\n");
        rates.append("Bus: $").append(String.format("%.2f/hour", BUS_HOURLY_RATE)).append("\n");
        rates.append("EV Charging: $").append(String.format("%.2f/session", EV_CHARGING_FEE)).append("\n");
        rates.append("Minimum charge: ").append(MINIMUM_HOURS).append(" hour(s)");
        
        return rates.toString();
    }
}
