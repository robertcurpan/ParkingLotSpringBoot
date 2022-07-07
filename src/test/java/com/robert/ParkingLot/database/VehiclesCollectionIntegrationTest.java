package com.robert.ParkingLot.database;

import com.robert.ParkingLot.exceptions.VehicleNotFoundException;
import com.robert.ParkingLot.parking.Driver;
import com.robert.ParkingLot.vehicles.Car;
import com.robert.ParkingLot.vehicles.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class VehiclesCollectionIntegrationTest {
    @Autowired
    private VehiclesCollection vehiclesCollection;

    @BeforeEach
    void resetDriversCollection() {
        vehiclesCollection.resetVehiclesCollection();
    }

    @Test
    public void addDriver() throws VehicleNotFoundException {
        Driver driver = new Driver("Robert", false);
        Vehicle vehicle = new Car(driver, "red", 2000, false);

        vehiclesCollection.addVehicle(vehicle);

        assertEquals(vehicle, vehiclesCollection.getVehicleById(vehicle.getVehicleId()));
    }

    @Test
    public void removeDriver() throws VehicleNotFoundException {
        Driver driver = new Driver("Robert", false);
        Vehicle vehicle = new Car(driver, "red", 2000, false);

        vehiclesCollection.addVehicle(vehicle);
        assertEquals(vehicle, vehiclesCollection.getVehicleById(vehicle.getVehicleId()));
        vehiclesCollection.removeVehicle(vehicle);
        assertThrowsExactly(VehicleNotFoundException.class, () -> vehiclesCollection.getVehicleById(vehicle.getVehicleId()));
    }
}
