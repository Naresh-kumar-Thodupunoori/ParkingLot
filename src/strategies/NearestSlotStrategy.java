package strategies;

import interfaces.SlotAllocationStrategy;
import models.ParkingFloor;
import models.ParkingSlot;
import models.Vehicle;
import java.util.List;

public class NearestSlotStrategy implements SlotAllocationStrategy {
    
    @Override
    public ParkingSlot allocateParkingSlot(Vehicle vehicle, List<ParkingFloor> floors, int entryFloor) {
        if (vehicle == null || floors == null || floors.isEmpty()) {
            return null;
        }
        
        ParkingSlot bestSlot = null;
        int shortestDistance = Integer.MAX_VALUE;
        
        // Find the closest available slot
        for (ParkingFloor floor : floors) {
            List<ParkingSlot> availableSlots = floor.getAvailableSlots(vehicle);
            
            for (ParkingSlot slot : availableSlots) {
                int distance = slot.getDistanceFromEntry(entryFloor);
                
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    bestSlot = slot;
                }
            }
        }
        
        if (bestSlot != null) {
            System.out.println("Nearest slot found: " + bestSlot.getSlotId() + 
                             " on floor " + bestSlot.getFloorNumber() + 
                             " (Distance: " + shortestDistance + ")");
        }
        
        return bestSlot;
    }
}
