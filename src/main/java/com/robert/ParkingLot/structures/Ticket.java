package com.robert.ParkingLot.structures;

import com.robert.ParkingLot.parking.ParkingSpot;
import com.robert.ParkingLot.vehicles.Vehicle;

public class Ticket {
    private ParkingSpot parkingSpot;
    private Vehicle vehicle;

    public Ticket(ParkingSpot parkingSpot, Vehicle vehicle) { this.parkingSpot = parkingSpot; this.vehicle = vehicle; }

    public ParkingSpot getParkingSpot() { return parkingSpot; }
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    @Override
    public boolean equals(Object object) {
        if(object instanceof Ticket) {
            Ticket ticket = (Ticket) object;
            return this.parkingSpot.equals(ticket.parkingSpot) && this.vehicle.equals(ticket.getVehicle());
        }
        return false;
    }
}
