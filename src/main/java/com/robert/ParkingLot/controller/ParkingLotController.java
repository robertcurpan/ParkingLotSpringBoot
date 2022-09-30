package com.robert.ParkingLot.controller;

import com.robert.ParkingLot.authentication.AuthenticationService;
import com.robert.ParkingLot.exceptions.*;
import com.robert.ParkingLot.parking.ParkingLotService;
import com.robert.ParkingLot.parking.ParkingSpot;
import com.robert.ParkingLot.authentication.AuthorizationResponse;
import com.robert.ParkingLot.structures.StandardMessage;
import com.robert.ParkingLot.structures.Ticket;
import com.robert.ParkingLot.authentication.UserDetails;
import com.robert.ParkingLot.structures.User;
import com.robert.ParkingLot.vehicles.CreateVehicleFromJsonUtil;
import com.robert.ParkingLot.vehicles.Vehicle;
import com.robert.ParkingLot.vehicles.VehicleJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ParkingLotController {
    @Autowired
    public ParkingLotService parkingLotService;
    @Autowired
    public AuthenticationService authenticationService;

    @PostMapping(value = "/generateParkingTicket")
    @CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:4200"})
    public ResponseEntity<Ticket> generateParkingTicket(@RequestBody VehicleJson vehicleJson) throws ParkingLotGeneralException {
        Vehicle vehicle = CreateVehicleFromJsonUtil.createVehicle(vehicleJson);
        Ticket ticket = parkingLotService.generateParkingTicket(vehicle);
        return new ResponseEntity<Ticket>(ticket, HttpStatus.OK);
    }

    @PostMapping(value = "/leaveParkingLot")
    @CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:4200"})
    public ResponseEntity<Ticket> leaveParkingLot(@RequestBody ParkingSpot parkingSpot) throws ParkingLotGeneralException {
        Ticket ticket = parkingLotService.leaveParkingLot(parkingSpot.getId());
        return new ResponseEntity<Ticket>(ticket, HttpStatus.OK);
    }

    @GetMapping(value = "/getParkingSpots")
    @CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:4200"})
    public ResponseEntity<List<ParkingSpot>> getParkingSpots() {
        List<ParkingSpot> parkingSpots = parkingLotService.getAllParkingSpots();
        return new ResponseEntity<List<ParkingSpot>>(parkingSpots, HttpStatus.OK);
    }

    @GetMapping(value = "/getTickets")
    @CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:4200"})
    public ResponseEntity<List<Ticket>> getTickets() {
        List<Ticket> tickets = parkingLotService.getTickets();
        return new ResponseEntity<List<Ticket>>(tickets, HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    @CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:4200"})
    public ResponseEntity<AuthorizationResponse> login(@RequestBody UserDetails userDetails) throws ParkingLotGeneralException {
        String jwt = authenticationService.verifyLoginAndGenerateJwt(userDetails);
        User user = authenticationService.getUserByUsername(userDetails.getUsername());
        AuthorizationResponse authorizationResponse = new AuthorizationResponse(jwt, user.getAccountType());
        return new ResponseEntity<AuthorizationResponse>(authorizationResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    @CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:4200"})
    public ResponseEntity<StandardMessage> register(@RequestBody UserDetails userDetails) throws ParkingLotGeneralException {
        String hashedPassword = BCrypt.hashpw(userDetails.getPassword(), BCrypt.gensalt(10));
        authenticationService.registerUser(new User(userDetails.getUsername(), hashedPassword, "user"));
        StandardMessage response = new StandardMessage("Successfully registered!");
        return new ResponseEntity<StandardMessage>(response, HttpStatus.OK);
    }
}