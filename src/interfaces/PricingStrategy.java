package interfaces;

import models.Ticket;

/**
 * Strategy interface for different pricing calculations
 * Follows Strategy Design Pattern for flexible pricing models
 */
public interface PricingStrategy {
    /**
     * Calculate the price for parking based on the ticket information
     * 
     * @param ticket The parking ticket containing entry time, slot info, etc.
     * @return The calculated price for parking
     */
    double calculatePrice(Ticket ticket);
}
