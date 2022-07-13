package com.robert.ParkingLot.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robert.ParkingLot.exceptions.*;
import com.robert.ParkingLot.parking.Driver;
import com.robert.ParkingLot.parking.ParkingLotService;
import com.robert.ParkingLot.parking.ParkingSpot;
import com.robert.ParkingLot.parking.ParkingSpotType;
import com.robert.ParkingLot.structures.Ticket;
import com.robert.ParkingLot.vehicles.Car;
import com.robert.ParkingLot.vehicles.CreateVehicleFromJsonUtil;
import com.robert.ParkingLot.vehicles.Vehicle;
import com.robert.ParkingLot.vehicles.VehicleJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ParkingLotController {
    @Autowired
    public ParkingLotService parkingLotService;

    @PostMapping(value = "/generateParkingTicket")
    public ResponseEntity<Ticket> generateParkingTicket(@RequestBody VehicleJson vehicleJson) throws ParkingLotGeneralException {
        Ticket dummyTicket = new Ticket(0, new Car(new Driver("Robert", true), "red", 1000, true));
        Vehicle vehicle = CreateVehicleFromJsonUtil.createVehicle(vehicleJson);
        Ticket ticket = parkingLotService.getParkingTicket(vehicle);
        return new ResponseEntity<Ticket>(ticket, HttpStatus.OK);
    }

    @PostMapping(value = "/leaveParkingLot")
    public ResponseEntity<Ticket> leaveParkingLot(@RequestBody ParkingSpot parkingSpot) throws ParkingLotGeneralException {
        Ticket ticket = parkingLotService.leaveParkingLot(parkingSpot.getId());
        return new ResponseEntity<Ticket>(ticket, HttpStatus.OK);
    }

    @GetMapping(value = "/getParkingSpots")
    public List<ParkingSpot> getParkingSpots() {
        return parkingLotService.getAllParkingSpots();
    }

    @GetMapping(value = "/getTickets")
    public List<Ticket> getTickets() throws VehicleNotFoundException {
        return parkingLotService.getTickets();
    }

}
