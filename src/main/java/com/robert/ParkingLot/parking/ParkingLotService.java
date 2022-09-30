package com.robert.ParkingLot.parking;

import com.robert.ParkingLot.database.ParkingSpotsCollection;
import com.robert.ParkingLot.database.TicketsCollection;
import com.robert.ParkingLot.database.VehiclesCollection;
import com.robert.ParkingLot.exceptions.*;
import com.robert.ParkingLot.strategy.TicketGenerator;
import com.robert.ParkingLot.structures.Ticket;
import com.robert.ParkingLot.vehicles.Motorcycle;
import com.robert.ParkingLot.vehicles.Vehicle;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class ParkingLotService {

    private TicketGeneratorCreator ticketGeneratorCreator;
    private ParkingSpotsCollection parkingSpotsCollection;
    private VehiclesCollection vehiclesCollection;
    private TicketsCollection ticketsCollection;

    public ParkingLotService(TicketGeneratorCreator ticketGeneratorCreator, ParkingSpotsCollection parkingSpotsCollection, VehiclesCollection vehiclesCollection, TicketsCollection ticketsCollection) {
        this.ticketGeneratorCreator = ticketGeneratorCreator;
        this.parkingSpotsCollection = parkingSpotsCollection;
        this.vehiclesCollection = vehiclesCollection;
        this.ticketsCollection = ticketsCollection;
    }

    public Ticket generateParkingTicket(Vehicle vehicle) throws ParkingLotGeneralException {
        doBusinessValidations(vehicle);
        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(vehicle);
        Ticket ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        ticket.getParkingSpot().setVehicleId(vehicle.getVehicleId());
        parkingSpotsCollection.updateParkingSpotWhenDriverParks(ticket.getParkingSpot());
        vehiclesCollection.addVehicle(vehicle);
        ticket.getParkingSpot().updateWhenOccupiedByVehicle(vehicle);
        ticketsCollection.addTicket(ticket);
        return ticket;
    }

    public Ticket leaveParkingLot(int idParkingSpot) throws ParkingLotGeneralException {
        ParkingSpot parkingSpot = parkingSpotsCollection.getParkingSpotById(idParkingSpot);
        if (!parkingSpotsCollection.isParkingSpotFree(parkingSpot)) {
            Vehicle vehicle = vehiclesCollection.getVehicleById(parkingSpot.getVehicleId());
            parkingSpotsCollection.updateParkingSpotWhenDriverLeaves(parkingSpot);  // o functie din colectia bazei de date trebuie sa stie doar operatii CRUD (valorile atributelor ce trebuie actualizate le ia din obiectul dat ca parametru - parkingSpot.isFree())
            vehiclesCollection.removeVehicle(vehicle);
            ticketsCollection.removeTicked(parkingSpot);
            parkingSpot.updateWhenLeftByVehicle();
            return new Ticket(parkingSpot, vehicle);
        }

        throw new ParkingSpotNotOccupiedException("notOccupied");
    }

    public List<Ticket> getTickets() {
        List<Ticket> tickets = ticketsCollection.getTickets();
        return tickets;
    }

    public List<ParkingSpot> getAllParkingSpots() {
        return parkingSpotsCollection.getParkingSpots();
    }

    public void doBusinessValidations(Vehicle vehicle) throws ParkingLotGeneralException {
        if(vehicle.getPrice() > 10000) {
            throw new ParkingLotGeneralException("tooExpensive");
        }
    }
}