# Smart Parking Lot System

A simple yet powerful parking management system built in Java. Features interactive console interface where users can park vehicles and pay bills just like in real life!

## What It Does

### 🚗 Easy Vehicle Parking
- Drive up, enter your vehicle details
- System finds the closest available spot
- Get a ticket with your parking details
- Supports bikes, cars, autos, and buses

### 💰 Smart Billing
- Exit anytime by entering your vehicle number
- See exactly how long you parked
- Dynamic pricing based on vehicle type and time
- Multiple payment options (Cash, Card, UPI)

### ⚡ EV Friendly
- Electric and hybrid vehicle support
- Automatic charging spot allocation when available
- Special charging fees included in bill

### 🏢 Multi-Level Design
- Ground floor + 3 parking levels
- Different slot sizes (Small, Medium, Large)
- Smart allocation - smaller vehicles can use larger slots
- Real-time capacity monitoring

## How to Run

```bash
# Compile everything
javac -d . src/**/*.java src/*.java

# Start the system
java Main
```

## What You'll See

```
🚗 Welcome to Smart Parking System 🚗
=====================================

===================================
         PARKING MENU
===================================
1. 🚗 Park My Vehicle
2. 🚪 Exit & Pay
3. 📊 Check Available Spots
4. 💰 View Parking Rates
5. 🚪 Exit System
===================================
Your choice: 1

🚗 PARKING YOUR VEHICLE
------------------------
Vehicle number: ABC123
What's your ride?
1. 🏍️  Bike  2. 🚗 Car  3. 🚕 Auto  4. 🚌 Bus
Your choice: 2

✅ Successfully parked!
🅿️  Your spot: F1M5
🏢 Floor: 1
⚡ Bonus: EV charging available!
```

## Project Structure

```
src/
├── Main.java                    # Main program - all the user interaction
├── enums/                       # Simple enums for types
│   ├── VehicleType.java        # BIKE, CAR, AUTO, BUS
│   ├── FuelType.java           # PETROL, ELECTRIC, HYBRID
│   ├── SlotType.java           # SMALL, MEDIUM, LARGE
│   ├── SlotStatus.java         # FILLED, EMPTY, MAINTENANCE
│   └── Payment.java            # CASH, CARD, UPI
├── interfaces/                  # Just the essential interfaces
│   ├── PricingStrategy.java    # For different pricing models
│   └── SlotAllocationStrategy.java # For slot finding algorithms
├── models/                      # The main business objects
│   ├── Vehicle.java            # Vehicle info
│   ├── ParkingSlot.java        # Individual parking spot
│   ├── Ticket.java             # Entry ticket
│   ├── Bill.java               # Exit bill
│   ├── EntryGate.java          # Entry point
│   ├── ExitGate.java           # Exit point
│   ├── ParkingFloor.java       # One floor of parking
│   └── ParkingLot.java         # The whole parking lot
└── strategies/                  # Different algorithms
    ├── NearestSlotStrategy.java # Find closest spot
    ├── DynamicPricing.java     # Smart pricing
    └── PerHourPricingSt.java   # Simple hourly rates
```

## Key Features

### Smart Slot Allocation
- Always finds the closest available spot to your entry gate
- Considers vehicle size - bikes can park in car spots if needed
- Prioritizes EV charging spots for electric/hybrid vehicles

### Dynamic Pricing
- Base rates: Bike $2/hr, Car $4/hr, Auto $3.5/hr, Bus $8/hr
- Peak hours (9 AM - 6 PM): +50% surcharge
- Long parking: Up to 20% discount for staying longer
- EV charging: Extra $3/hour when using charging spots

### User-Friendly Design
- Simple menu navigation
- Clear prompts and feedback
- Real-time parking status
- Error handling for invalid inputs

## Design Principles

The code follows good object-oriented practices:
- **Single Responsibility**: Each class does one thing well
- **Strategy Pattern**: Easy to add new pricing or allocation methods
- **Clean Interfaces**: Simple contracts between components
- **Extensible**: Easy to add new vehicle types or features

## Real-World Usage

This system mimics how actual parking lots work:
1. **Entry**: Drive up, get a ticket with your assigned spot
2. **Parking**: Walk to your designated floor and slot
3. **Exit**: Return to pay machine, enter vehicle number, pay bill
4. **Leave**: Show receipt at exit gate

Perfect for:
- Shopping malls
- Office buildings  
- Airports
- Hospitals
- Any multi-level parking facility

## Want to Extend It?

- Add new vehicle types in `VehicleType.java`
- Create custom pricing in new strategy classes
- Add more floors by modifying the initialization
- Build a web interface by keeping the same backend logic

The code is designed to be readable and extensible - just like how a human developer would naturally write it!