package com.robert.ParkingLot.database;

import com.robert.ParkingLot.exceptions.ParkingSpotNotAvailableException;
import com.robert.ParkingLot.exceptions.ParkingSpotNotFoundException;
import com.robert.ParkingLot.exceptions.ParkingSpotNotOccupiedException;
import com.robert.ParkingLot.exceptions.SimultaneousOperationInDatabaseCollectionException;
import com.robert.ParkingLot.parking.Driver;
import com.robert.ParkingLot.parking.ParkingSpot;
import com.robert.ParkingLot.strategy.TicketGeneratorUtil;
import com.robert.ParkingLot.vehicles.Car;
import com.robert.ParkingLot.vehicles.Vehicle;
import com.robert.ParkingLot.vehicles.VehicleType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ParkingSpotsCollectionIntegrationTest {
    public static final String NAME = "Robert";
    public static final boolean VIP_STATUS = false;
    public static final String COLOR = "red";
    public static final int PRICE = 2000;
    public static final boolean ELECTRIC = false;
    public static final int ID = 1;

    @Autowired
    private ParkingSpotsCollection parkingSpotsCollection;

    @BeforeEach
    void initializeDatabaseCollections() {
        parkingSpotsCollection.initializeParkingSpotsCollection();
    }

    @AfterEach
    void resetDatabaseCollections() {
        parkingSpotsCollection.resetParkingSpotsCollection();
    }

    @Test
    public void getVehicleIdFromParkingSpot() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException, ParkingSpotNotOccupiedException {
        Driver driver = new Driver(NAME, VIP_STATUS);
        Vehicle vehicle = new Car(driver, COLOR, PRICE, ELECTRIC);
        ParkingSpot parkingSpot = new ParkingSpot(ID, vehicle.getVehicleId(), TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), false, 1);

        parkingSpotsCollection.updateParkingSpotWhenDriverParks(parkingSpot);
        assertEquals(vehicle.getVehicleId(), parkingSpotsCollection.getParkingSpotById(parkingSpot.getId()).getVehicleId());
    }

    @Test
    public void getParkingSpotId() throws ParkingSpotNotFoundException, ParkingSpotNotAvailableException {
        assertEquals(7, parkingSpotsCollection.getIdForAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(VehicleType.MOTORCYCLE), true));
    }

    @Test
    public void updateParkingSpotWhenDriverParks() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        Driver driver = new Driver("Robert", false);
        Vehicle vehicle = new Car(driver, "red", 2000, false);
        ParkingSpot parkingSpot = new ParkingSpot(1, vehicle.getVehicleId(), TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), false, 1);

        parkingSpotsCollection.updateParkingSpotWhenDriverParks(parkingSpot);

        assertEquals(false, parkingSpotsCollection.isParkingSpotFree(parkingSpot));
    }

}
