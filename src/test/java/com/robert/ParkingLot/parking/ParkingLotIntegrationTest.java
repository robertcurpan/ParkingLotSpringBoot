package com.robert.ParkingLot.parking;

import com.robert.ParkingLot.database.ParkingSpotsCollection;
import com.robert.ParkingLot.database.VehiclesCollection;
import com.robert.ParkingLot.exceptions.*;
import com.robert.ParkingLot.structures.Ticket;
import com.robert.ParkingLot.vehicles.Car;
import com.robert.ParkingLot.vehicles.Vehicle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class ParkingLotIntegrationTest
{
    @Autowired
    private ParkingSpotsCollection parkingSpotsCollection;
    @Autowired
    private VehiclesCollection vehiclesCollection;
    @Autowired
    private ParkingLotService parkingLotService;

    public static final String COLOR = "red";
    public static final int PRICE = 1000;
    public static final boolean NON_ELECTRIC = false;
    public static final String NAME = "Robert";
    public static final boolean VIP = true;

    @BeforeEach
    void initializeDatabaseCollections() {
        parkingSpotsCollection.initializeParkingSpotsCollection();
    }

    @AfterEach
    void resetDatabaseCollections() {
        parkingSpotsCollection.resetParkingSpotsCollection();
        vehiclesCollection.resetVehiclesCollection();
    }

    @Test
    public void getParkingTicket() throws ParkingLotGeneralException {
        // Fiecare test are cumva prestabilite (alegem dinainte) ce tip de vehicul, cate locuri de parcare etc. si verificam ca am primit ce am asteptat

        // 1) Given (preconditiile testului - trebuie sa le avem ca sa putem executa testul)
        Driver driver = new Driver("Robert", false);
        Vehicle vehicle = new Car(driver, "red", 2000, false);

        // 2) When
        Ticket parkingTicket = parkingLotService.getParkingTicket(vehicle);

        // 3) Then (asserturi)
        assertEquals(1, parkingTicket.getSpotId());
    }

    @Test
    public void leaveParkingLot() throws ParkingLotGeneralException {
        // 1) Given
        Driver driver = new Driver("Robert", false);
        Vehicle vehicle = new Car(driver, "red", 2000, false);

        // 2) When
        Ticket ticket = parkingLotService.getParkingTicket(vehicle);
        Ticket ticketLeft = parkingLotService.leaveParkingLot(ticket.getSpotId());

        // 3) Then
        assertEquals(ticket, ticketLeft);
    }

}
