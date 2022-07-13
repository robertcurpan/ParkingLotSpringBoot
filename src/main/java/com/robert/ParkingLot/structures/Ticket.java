package com.robert.ParkingLot.structures;

import com.robert.ParkingLot.vehicles.Vehicle;

public class Ticket {
    private int spotId;
    private Vehicle vehicle;

    public Ticket(int spotId, Vehicle vehicle) { this.spotId = spotId; this.vehicle = vehicle; }

    public int getSpotId() { return spotId; }
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    @Override
    public boolean equals(Object object) {
        if(object instanceof Ticket) {
            Ticket ticket = (Ticket) object;
            return this.spotId == ticket.spotId && this.vehicle.equals(ticket.getVehicle());
        }
        return false;
    }
}
