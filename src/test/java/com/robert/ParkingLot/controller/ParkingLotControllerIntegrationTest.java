package com.robert.ParkingLot.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robert.ParkingLot.parking.Driver;
import com.robert.ParkingLot.parking.ParkingLotService;
import com.robert.ParkingLot.vehicles.VehicleJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(ParkingLotController.class)
public class ParkingLotControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void generateParkingTicketTest() throws Exception {
        String url = "http://localhost:8080/generateParkingTicket";
        VehicleJson vehicleJson = new VehicleJson("MOTORCYCLE", new Driver("Robert", true), "red", 2000, true);
        String requestBodyJson = new ObjectMapper().writeValueAsString(vehicleJson);

        mockMvc.perform(post(url)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestBodyJson))
                .andExpect(status().isOk());
    }

}
