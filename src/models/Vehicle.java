package models;

import enums.VehicleType;
import enums.FuelType;

public class Vehicle {
    private String vehicleNo;
    private VehicleType type;
    private FuelType fuelType;
    
    public Vehicle(String vehicleNo, VehicleType type, FuelType fuelType) {
        this.vehicleNo = vehicleNo;
        this.type = type;
        this.fuelType = fuelType;
    }
    
    public String getVehicleNo() { return vehicleNo; }
    public VehicleType getVehicleType() { return type; }
    public FuelType getFuelType() { return fuelType; }
    
    public void setVehicleNo(String vehicleNo) { this.vehicleNo = vehicleNo; }
    public void setVehicleType(VehicleType type) { this.type = type; }
    public void setFuelType(FuelType fuelType) { this.fuelType = fuelType; }
    
    public boolean needsCharging() {
        return fuelType == FuelType.ELECTRIC || fuelType == FuelType.HYBRID;
    }
    
    public boolean requiresCharging() {
        return needsCharging(); // alias for backward compatibility
    }
    
    @Override
    public String toString() {
        return vehicleNo + " (" + type + ", " + fuelType + ")";
    }
}