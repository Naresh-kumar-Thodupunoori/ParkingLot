import models.*;
import strategies.*;
import enums.*;
import java.util.Scanner;

public class Main {
    private static ParkingLot parkingLot;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("🚗 Welcome to Smart Parking System 🚗");
        System.out.println("=====================================");
        
        setupParkingLot();
        
        boolean running = true;
        while (running) {
            showMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1: parkVehicle(); break;
                case 2: exitVehicle(); break;
                case 3: checkStatus(); break;
                case 4: showRates(); break;
                case 5: 
                    System.out.println("Thanks for using our parking system! 👋");
                    running = false;
                    break;
                default:
                    System.out.println("❌ Invalid choice. Try again!");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    private static void setupParkingLot() {
        parkingLot = new ParkingLot(new NearestSlotStrategy(), new DynamicPricing());
        parkingLot.initializeDefault();
        
        System.out.println("✅ Parking system ready!");
        System.out.println(parkingLot.getCapacityInfo());
    }
    
    private static void showMenu() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("         PARKING MENU");
        System.out.println("=".repeat(35));
        System.out.println("1. 🚗 Park My Vehicle");
        System.out.println("2. 🚪 Exit & Pay");
        System.out.println("3. 📊 Check Available Spots");
        System.out.println("4. 💰 View Parking Rates");
        System.out.println("5. 🚪 Exit System");
        System.out.println("=".repeat(35));
    }
    
    private static int getChoice() {
        while (true) {
            try {
                System.out.print("Your choice: ");
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number!");
            }
        }
    }
    
    private static void parkVehicle() {
        System.out.println("\n🚗 PARKING YOUR VEHICLE");
        System.out.println("------------------------");
        
        System.out.print("Vehicle number: ");
        String vehicleNo = scanner.nextLine().trim().toUpperCase();
        
        if (parkingLot.getTicket(vehicleNo) != null) {
            System.out.println("❌ This vehicle is already parked!");
            return;
        }
        
        VehicleType vehicleType = selectVehicleType();
        FuelType fuelType = selectFuelType();
        String entryGate = selectGate("entry");
        
        Vehicle vehicle = new Vehicle(vehicleNo, vehicleType, fuelType);
        Ticket ticket = parkingLot.parkVehicle(vehicle, entryGate);
        
        if (ticket != null) {
            System.out.println("\n✅ Successfully parked!");
            System.out.println("🎫 Ticket ID: " + ticket.getTicketId());
            System.out.println("🅿️  Your spot: " + ticket.getParkingSlot().getSlotId());
            System.out.println("🏢 Floor: " + ticket.getParkingSlot().getFloorNumber());
            if (ticket.getParkingSlot().isChargingAvailable()) {
                System.out.println("⚡ Bonus: EV charging available!");
            }
            System.out.println("🕐 Parked at: " + ticket.getEntryTime());
            System.out.println("\n💡 Keep your vehicle number safe for exit!");
        } else {
            System.out.println("❌ Sorry, no suitable parking spot available right now.");
        }
    }
    
    private static void exitVehicle() {
        System.out.println("\n🚪 VEHICLE EXIT & PAYMENT");
        System.out.println("-------------------------");
        
        System.out.print("Enter vehicle number: ");
        String vehicleNo = scanner.nextLine().trim().toUpperCase();
        
        Ticket ticket = parkingLot.getTicket(vehicleNo);
        if (ticket == null) {
            System.out.println("❌ No parking record found for: " + vehicleNo);
            return;
        }
        
        long minutes = ticket.getParkingDurationInMinutes();
        System.out.println("\n📋 Your parking details:");
        System.out.println("Vehicle: " + vehicleNo);
        System.out.println("Spot: " + ticket.getParkingSlot().getSlotId());
        System.out.println("Duration: " + minutes + " minutes");
        
        DynamicPricing pricing = new DynamicPricing();
        double amount = pricing.calculatePrice(ticket);
        
        System.out.println("\n💰 Your bill: $" + String.format("%.2f", amount));
        System.out.print("Proceed with payment? (y/n): ");
        
        if (!scanner.nextLine().toLowerCase().startsWith("y")) {
            System.out.println("Payment cancelled. Vehicle remains parked.");
            return;
        }
        
        Payment paymentMethod = selectPaymentMethod();
        String exitGate = selectGate("exit");
        
        if (parkingLot.exitVehicle(vehicleNo, exitGate, paymentMethod)) {
            System.out.println("\n✅ Payment successful! Have a great day! 🌟");
            System.out.println("Paid: $" + String.format("%.2f", amount) + " via " + paymentMethod);
        } else {
            System.out.println("❌ Something went wrong. Please contact support.");
        }
    }
    
    private static void checkStatus() {
        System.out.println(parkingLot.getCapacityInfo());
    }
    
    private static void showRates() {
        System.out.println("\n💰 CURRENT PARKING RATES");
        System.out.println("Bike: $2/hour | Car: $4/hour | Auto: $3.5/hour | Bus: $8/hour");
        System.out.println("🔥 Peak hours (9 AM - 6 PM): +50% extra");
        System.out.println("🎉 Long term parking: Up to 20% discount");
        System.out.println("⚡ EV charging: +$3/hour (when available)");
    }
    
    private static VehicleType selectVehicleType() {
        System.out.println("\nWhat's your ride?");
        System.out.println("1. 🏍️  Bike  2. 🚗 Car  3. 🚕 Auto  4. 🚌 Bus");
        
        int choice = getChoice();
        VehicleType[] types = VehicleType.values();
        
        if (choice >= 1 && choice <= types.length) {
            return types[choice - 1];
        }
        
        System.out.println("Invalid choice, assuming Car");
        return VehicleType.CAR;
    }
    
    private static FuelType selectFuelType() {
        System.out.println("\nFuel type?");
        System.out.println("1. ⛽ Petrol  2. ⚡ Electric  3. 🔋 Hybrid");
        
        int choice = getChoice();
        FuelType[] types = FuelType.values();
        
        if (choice >= 1 && choice <= types.length) {
            return types[choice - 1];
        }
        
        System.out.println("Invalid choice, assuming Petrol");
        return FuelType.PETROL;
    }
    
    private static Payment selectPaymentMethod() {
        System.out.println("\nHow would you like to pay?");
        System.out.println("1. 💵 Cash  2. 💳 Card  3. 📱 UPI");
        
        int choice = getChoice();
        Payment[] methods = Payment.values();
        
        if (choice >= 1 && choice <= methods.length) {
            return methods[choice - 1];
        }
        
        System.out.println("Invalid choice, using Card");
        return Payment.CARD;
    }
    
    private static String selectGate(String type) {
        System.out.println("\nSelect " + type + " gate:");
        System.out.println("1. Gate A  2. Gate B");
        
        int choice = getChoice();
        String prefix = type.equals("entry") ? "ENTRY" : "EXIT";
        
        return choice == 2 ? prefix + "_02" : prefix + "_01";
    }
}