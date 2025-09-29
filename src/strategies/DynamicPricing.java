package strategies;

import interfaces.PricingStrategy;
import models.Ticket;
import enums.SlotType;
import enums.VehicleType;


public class DynamicPricing implements PricingStrategy {
    
    private static final double BIKE_BASE_RATE = 2.0;
    private static final double CAR_BASE_RATE = 5.0;
    private static final double AUTO_BASE_RATE = 4.0;
    private static final double BUS_BASE_RATE = 10.0;
    
    private static final double SMALL_SLOT_MULTIPLIER = 1.0;
    private static final double MEDIUM_SLOT_MULTIPLIER = 1.2;
    private static final double LARGE_SLOT_MULTIPLIER = 1.5;
    
    private static final double EV_CHARGING_RATE = 3.0;
    
    private static final double PEAK_HOUR_MULTIPLIER = 1.5;
    
    // Minimum charge (even for short duration)
    private static final double MINIMUM_CHARGE = 1.0;
    
    @Override
    public double calculatePrice(Ticket ticket) {
        if (ticket == null) {
            return 0.0;
        }
        
        long durationMinutes = ticket.getParkingDurationInMinutes();
        double durationHours = Math.max(1.0, Math.ceil(durationMinutes / 60.0));
        
        double baseRate = getBaseRateByVehicleType(ticket.getVehicle().getVehicleType());
        
        double slotMultiplier = getSlotTypeMultiplier(ticket.getParkingSlot().getSlotType());
        
        double baseCost = baseRate * slotMultiplier * durationHours;
        
        double peakMultiplier = isPeakHour(ticket.getEntryTime().getHour()) ? PEAK_HOUR_MULTIPLIER : 1.0;
        baseCost *= peakMultiplier;
        
        double chargingCost = 0.0;
        if (ticket.getVehicle().requiresCharging() && ticket.getParkingSlot().isChargingAvailable()) {
            chargingCost = EV_CHARGING_RATE * durationHours;
        }
        
        double discountMultiplier = getLongTermDiscountMultiplier(durationHours);
        
        double totalCost = (baseCost + chargingCost) * discountMultiplier;
        
        totalCost = Math.max(totalCost, MINIMUM_CHARGE);
        
        return Math.round(totalCost * 100.0) / 100.0; 
    }
    
    
    private double getBaseRateByVehicleType(VehicleType vehicleType) {
        switch (vehicleType) {
            case BIKE:
                return BIKE_BASE_RATE;
            case CAR:
                return CAR_BASE_RATE;
            case AUTO:
                return AUTO_BASE_RATE;
            case BUS:
                return BUS_BASE_RATE;
            default:
                return CAR_BASE_RATE;
        }
    }
    

    private double getSlotTypeMultiplier(SlotType slotType) {
        switch (slotType) {
            case SMALL:
                return SMALL_SLOT_MULTIPLIER;
            case MEDIUM:
                return MEDIUM_SLOT_MULTIPLIER;
            case LARGE:
                return LARGE_SLOT_MULTIPLIER;
            default:
                return MEDIUM_SLOT_MULTIPLIER;
        }
    }
    
   
    private boolean isPeakHour(int hour) {
        return hour >= 9 && hour <= 18;
    }
    
    
    private double getLongTermDiscountMultiplier(double durationHours) {
        if (durationHours >= 24) {
            return 0.8;
        } else if (durationHours >= 8) {
            return 0.9;
        } else {
            return 1.0; 
        }
    }
    

    public String getPriceBreakdown(Ticket ticket) {
        if (ticket == null) {
            return "Invalid ticket";
        }
        
        long durationMinutes = ticket.getParkingDurationInMinutes();
        double durationHours = Math.max(1.0, Math.ceil(durationMinutes / 60.0));
        
        double baseRate = getBaseRateByVehicleType(ticket.getVehicle().getVehicleType());
        double slotMultiplier = getSlotTypeMultiplier(ticket.getParkingSlot().getSlotType());
        double baseCost = baseRate * slotMultiplier * durationHours;
        
        boolean isPeak = isPeakHour(ticket.getEntryTime().getHour());
        double peakMultiplier = isPeak ? PEAK_HOUR_MULTIPLIER : 1.0;
        baseCost *= peakMultiplier;
        
        double chargingCost = 0.0;
        if (ticket.getVehicle().requiresCharging() && ticket.getParkingSlot().isChargingAvailable()) {
            chargingCost = EV_CHARGING_RATE * durationHours;
        }
        
        double discountMultiplier = getLongTermDiscountMultiplier(durationHours);
        double totalCost = (baseCost + chargingCost) * discountMultiplier;
        totalCost = Math.max(totalCost, MINIMUM_CHARGE);
        
        StringBuilder breakdown = new StringBuilder();
        breakdown.append("=== PRICING BREAKDOWN ===\n");
        breakdown.append("Vehicle: ").append(ticket.getVehicle().getVehicleNo()).append("\n");
        breakdown.append("Duration: ").append(String.format("%.1f hours", durationHours)).append("\n");
        breakdown.append("Base Rate: $").append(String.format("%.2f/hour", baseRate)).append("\n");
        breakdown.append("Slot Type: ").append(ticket.getParkingSlot().getSlotType())
                .append(" (x").append(slotMultiplier).append(")\n");
        if (isPeak) {
            breakdown.append("Peak Hour Multiplier: x").append(PEAK_HOUR_MULTIPLIER).append("\n");
        }
        breakdown.append("Parking Cost: $").append(String.format("%.2f", baseCost)).append("\n");
        if (chargingCost > 0) {
            breakdown.append("EV Charging: $").append(String.format("%.2f", chargingCost)).append("\n");
        }
        if (discountMultiplier < 1.0) {
            breakdown.append("Long-term Discount: ").append(String.format("%.0f%%", (1 - discountMultiplier) * 100)).append("\n");
        }
        breakdown.append("TOTAL: $").append(String.format("%.2f", totalCost));
        
        return breakdown.toString();
    }
}
