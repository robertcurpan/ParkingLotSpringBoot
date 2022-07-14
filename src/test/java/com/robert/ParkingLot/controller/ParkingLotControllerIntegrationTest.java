package com.robert.ParkingLot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robert.ParkingLot.database.ParkingSpotsCollection;
import com.robert.ParkingLot.database.VehiclesCollection;
import com.robert.ParkingLot.parking.Driver;
import com.robert.ParkingLot.parking.ParkingLotService;
import com.robert.ParkingLot.parking.ParkingSpot;
import com.robert.ParkingLot.structures.Ticket;
import com.robert.ParkingLot.vehicles.CreateVehicleFromJsonUtil;
import com.robert.ParkingLot.vehicles.Vehicle;
import com.robert.ParkingLot.vehicles.VehicleJson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ParkingLotControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ParkingSpotsCollection parkingSpotsCollection;

    @Autowired
    private VehiclesCollection vehiclesCollection;

    @BeforeEach
    public void initializeParkingLot() {
        parkingSpotsCollection.initializeParkingSpotsCollection();
        vehiclesCollection.resetVehiclesCollection();
    }

    @AfterEach
    public void resetParkingLot() {
        parkingSpotsCollection.resetParkingSpotsCollection();
    }

    @Test
    public void generateParkingTicketTest() throws Exception {
        String url = "http://localhost:8080/generateParkingTicket";
        VehicleJson vehicleJson = new VehicleJson("MOTORCYCLE", new Driver("Robert", true), "red", 2000, true);
        String requestBodyJson = new ObjectMapper().writeValueAsString(vehicleJson);

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.spotId", is(7)));

    }


    @Test
    public void leaveParkingLotTest() throws Exception {
        String urlGenerateParkingTicket = "http://localhost:8080/generateParkingTicket";
        String urlLeaveParkingLot = "http://localhost:8080/leaveParkingLot";

        VehicleJson vehicleJson = new VehicleJson("MOTORCYCLE", new Driver("Robert", true), "red", 2000, true);
        String requestBodyJson = new ObjectMapper().writeValueAsString(vehicleJson);

        mockMvc.perform(post(urlGenerateParkingTicket)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isOk());

        ParkingSpot parkingSpot = parkingSpotsCollection.getParkingSpotById(7);
        requestBodyJson = new ObjectMapper().writeValueAsString(parkingSpot);

        mockMvc.perform(post(urlLeaveParkingLot)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.spotId", is(7)));
    }

    @Test
    public void getParkingSpotsTest() throws Exception {
        String url = "http://localhost:8080/getParkingSpots";

        mockMvc.perform(get(url))
                .andExpect(jsonPath("$.size()", is(9)));
    }

    @Test
    public void getTicketsTest() throws Exception {
        String url = "http://localhost:8080/getTickets";
        String urlGenerateParkingTicket = "http://localhost:8080/generateParkingTicket";

        VehicleJson vehicleJson = new VehicleJson("MOTORCYCLE", new Driver("Robert", true), "red", 2000, true);
        String requestBodyJson = new ObjectMapper().writeValueAsString(vehicleJson);

        mockMvc.perform(post(urlGenerateParkingTicket)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isOk());

        mockMvc.perform(get(url))
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].spotId", is(7)));
    }

}
