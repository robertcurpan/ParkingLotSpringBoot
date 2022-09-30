package com.robert.ParkingLot.structures;

import com.robert.ParkingLot.parking.ParkingSpot;
import com.robert.ParkingLot.utils.CurrentDateUtil;
import com.robert.ParkingLot.vehicles.Vehicle;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity("tickets")
public class Ticket {
    @Id
    private UUID ticketId;
    private ParkingSpot parkingSpot;
    private Vehicle vehicle;
    private LocalDateTime date;

    public Ticket(ParkingSpot parkingSpot, Vehicle vehicle) {
        this.parkingSpot = parkingSpot;
        this.vehicle = vehicle;
        ticketId = UUID.randomUUID();
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute(), LocalTime.now().getSecond());
        this.date = LocalDateTime.of(ld, lt);
    }

    public ParkingSpot getParkingSpot() { return parkingSpot; }
    public Vehicle getVehicle() { return vehicle; }
    public UUID getTicketId() { return ticketId; }
    public LocalDateTime getDate() { return date; }
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
