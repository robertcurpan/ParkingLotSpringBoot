package com.robert.ParkingLot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robert.ParkingLot.authentication.UserDetails;
import com.robert.ParkingLot.database.ParkingSpotsCollection;
import com.robert.ParkingLot.database.TicketsCollection;
import com.robert.ParkingLot.database.VehiclesCollection;
import com.robert.ParkingLot.parking.Driver;
import com.robert.ParkingLot.parking.ParkingSpot;
import com.robert.ParkingLot.utils.JwtUtil;
import com.robert.ParkingLot.vehicles.VehicleJson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private TicketsCollection ticketsCollection;

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    public void initializeParkingLot() {
        parkingSpotsCollection.initializeParkingSpotsCollection();
        vehiclesCollection.resetVehiclesCollection();
        ticketsCollection.resetTicketsCollection();
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
                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyb2JlcnQiLCJleHAiOjE2NjQ5OTA5MzcsImlhdCI6MTY2NDk3MjkzN30.AZhe18LRbL2FA9ykBysY4BaYaDvCj1598V9GvhrWQBzj5VgkaxsuRz7fLNSuh3jX25IaMqNVOLVjV_ou5M_0AA")
                        .content(requestBodyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parkingSpot.id", is(7)));
    }


    @Test
    public void leaveParkingLotTest() throws Exception {
        String urlGenerateParkingTicket = "http://localhost:8080/generateParkingTicket";
        String urlLeaveParkingLot = "http://localhost:8080/leaveParkingLot";

        VehicleJson vehicleJson = new VehicleJson("MOTORCYCLE", new Driver("Robert", true), "red", 2000, true);
        String requestBodyJson = new ObjectMapper().writeValueAsString(vehicleJson);

        mockMvc.perform(post(urlGenerateParkingTicket)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyb2JlcnQiLCJleHAiOjE2NjQ5OTA5MzcsImlhdCI6MTY2NDk3MjkzN30.AZhe18LRbL2FA9ykBysY4BaYaDvCj1598V9GvhrWQBzj5VgkaxsuRz7fLNSuh3jX25IaMqNVOLVjV_ou5M_0AA")
                        .content(requestBodyJson))
                .andExpect(status().isOk());

        ParkingSpot parkingSpot = parkingSpotsCollection.getParkingSpotById(7);
        requestBodyJson = new ObjectMapper().writeValueAsString(parkingSpot);

        mockMvc.perform(post(urlLeaveParkingLot)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyb2JlcnQiLCJleHAiOjE2NjQ5OTA5MzcsImlhdCI6MTY2NDk3MjkzN30.AZhe18LRbL2FA9ykBysY4BaYaDvCj1598V9GvhrWQBzj5VgkaxsuRz7fLNSuh3jX25IaMqNVOLVjV_ou5M_0AA")
                        .content(requestBodyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parkingSpot.id", is(7)));
    }

    @Test
    public void getParkingSpotsTest() throws Exception {
        String url = "http://localhost:8080/getParkingSpots";

        mockMvc.perform(get(url)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyb2JlcnQiLCJleHAiOjE2NjQ5OTA5MzcsImlhdCI6MTY2NDk3MjkzN30.AZhe18LRbL2FA9ykBysY4BaYaDvCj1598V9GvhrWQBzj5VgkaxsuRz7fLNSuh3jX25IaMqNVOLVjV_ou5M_0AA"))
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
                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyb2JlcnQiLCJleHAiOjE2NjQ5OTA5MzcsImlhdCI6MTY2NDk3MjkzN30.AZhe18LRbL2FA9ykBysY4BaYaDvCj1598V9GvhrWQBzj5VgkaxsuRz7fLNSuh3jX25IaMqNVOLVjV_ou5M_0AA")
                        .content(requestBodyJson))
                .andExpect(status().isOk());

        mockMvc.perform(get(url)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyb2JlcnQiLCJleHAiOjE2NjQ5OTA5MzcsImlhdCI6MTY2NDk3MjkzN30.AZhe18LRbL2FA9ykBysY4BaYaDvCj1598V9GvhrWQBzj5VgkaxsuRz7fLNSuh3jX25IaMqNVOLVjV_ou5M_0AA"))
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].parkingSpot.id", is(7)));
    }

}
