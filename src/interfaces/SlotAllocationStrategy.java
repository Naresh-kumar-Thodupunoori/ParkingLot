package interfaces;

import models.ParkingFloor;
import models.ParkingSlot;
import models.Vehicle;
import java.util.List;

/**
 * Strategy interface for different slot allocation algorithms
 * Follows Strategy Design Pattern for flexible slot assignment
 */
public interface SlotAllocationStrategy {
    /**
     * Allocate a parking slot for the given vehicle from available floors
     * 
     * @param vehicle The vehicle that needs parking
     * @param floors List of parking floors to search for available slots
     * @param entryFloor The floor where the entry gate is located
     * @return The allocated parking slot, or null if no suitable slot is available
     */
    ParkingSlot allocateParkingSlot(Vehicle vehicle, List<ParkingFloor> floors, int entryFloor);
}
