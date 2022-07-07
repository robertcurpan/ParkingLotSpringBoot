package com.robert.ParkingLot.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robert.ParkingLot.exceptions.ParkingSpotNotFoundException;
import com.robert.ParkingLot.exceptions.ParkingSpotNotOccupiedException;
import com.robert.ParkingLot.exceptions.SimultaneousOperationInDatabaseCollectionException;
import com.robert.ParkingLot.exceptions.VehicleNotFoundException;
import com.robert.ParkingLot.parking.ParkingLotService;
import com.robert.ParkingLot.parking.ParkingSpot;
import com.robert.ParkingLot.structures.Ticket;
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

    @PostMapping(value = "/getParkingTicket")
    public Ticket getParkingTicket(@RequestBody VehicleJson vehicleJson) throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        Vehicle vehicle = CreateVehicleFromJsonUtil.createVehicle(vehicleJson);
        return parkingLotService.getParkingTicket(vehicle);
    }

    @PostMapping(value = "/leaveParkingLot/{idParkingSpot}")
    public ResponseEntity<String> leaveParkingLot(@PathVariable String idParkingSpot) throws VehicleNotFoundException, ParkingSpotNotOccupiedException, ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException, JsonProcessingException {
        try {
            Vehicle vehicle = parkingLotService.leaveParkingLot(Integer.parseInt(idParkingSpot));
            return new ResponseEntity<String>(new ObjectMapper().writeValueAsString(vehicle), HttpStatus.OK);
        } catch(ParkingSpotNotOccupiedException | ParkingSpotNotFoundException parkingSpotNotFoundException) {
            return new ResponseEntity<String>("The vehicle with id: " + idParkingSpot + " does not exist!", HttpStatus.OK);
        }
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
