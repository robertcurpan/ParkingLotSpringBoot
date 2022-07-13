package com.robert.ParkingLot.parking;

import com.robert.ParkingLot.database.ParkingSpotsCollection;
import com.robert.ParkingLot.database.VehiclesCollection;
import com.robert.ParkingLot.exceptions.*;
import com.robert.ParkingLot.strategy.TicketGenerator;
import com.robert.ParkingLot.structures.Ticket;
import com.robert.ParkingLot.vehicles.Vehicle;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class ParkingLotService {

    private TicketGeneratorCreator ticketGeneratorCreator;
    private ParkingSpotsCollection parkingSpotsCollection;
    private VehiclesCollection vehiclesCollection;

    public ParkingLotService(TicketGeneratorCreator ticketGeneratorCreator, ParkingSpotsCollection parkingSpotsCollection, VehiclesCollection vehiclesCollection) {
        this.ticketGeneratorCreator = ticketGeneratorCreator;
        this.parkingSpotsCollection = parkingSpotsCollection;
        this.vehiclesCollection = vehiclesCollection;
    }

    public Ticket getParkingTicket(Vehicle vehicle) throws ParkingLotGeneralException {
        doBusinessValidations(vehicle);
        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(vehicle);
        Ticket ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        ParkingSpot parkingSpot = parkingSpotsCollection.getParkingSpotById(ticket.getSpotId());
        parkingSpot.setVehicleId(vehicle.getVehicleId()); // nu putem seta id-ul din interfata, asa ca il setam aici (mapare 1:1 dintre parkingSpotId si vehicleId)
        parkingSpotsCollection.updateParkingSpotWhenDriverParks(parkingSpot);
        vehiclesCollection.addVehicle(vehicle);
        return ticket;
    }

    public Ticket leaveParkingLot(int idParkingSpot) throws ParkingLotGeneralException {
        ParkingSpot parkingSpot = parkingSpotsCollection.getParkingSpotById(idParkingSpot);
        if (!parkingSpotsCollection.isParkingSpotFree(parkingSpot)) {
            Vehicle vehicle = vehiclesCollection.getVehicleById(parkingSpot.getVehicleId());
            parkingSpotsCollection.updateParkingSpotWhenDriverLeaves(parkingSpot);  // o functie din colectia bazei de date trebuie sa stie doar operatii CRUD (valorile atributelor ce trebuie actualizate le ia din obiectul dat ca parametru - parkingSpot.isFree())
            vehiclesCollection.removeVehicle(vehicle);
            return new Ticket(idParkingSpot, vehicle);
        }

        throw new ParkingSpotNotOccupiedException("notOccupied");
    }

    public List<Ticket> getTickets() throws VehicleNotFoundException {
        List<Ticket> tickets = new ArrayList<>();
        List<Vehicle> vehicles = vehiclesCollection.getAllVehicles();
        List<ParkingSpot> parkingSpots = parkingSpotsCollection.getParkingSpots();

        for(Vehicle vehicle : vehicles) {
            for(ParkingSpot parkingSpot : parkingSpots) {
                if(vehicle.getVehicleId().equals(parkingSpot.getVehicleId())) {
                    tickets.add(new Ticket(parkingSpot.getId(), vehicle));
                }
            }
        }

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
