package com.robert.ParkingLot.strategy;

import com.robert.ParkingLot.database.ParkingSpotsCollection;
import com.robert.ParkingLot.exceptions.ParkingSpotNotAvailableException;
import com.robert.ParkingLot.exceptions.ParkingSpotNotFoundException;
import com.robert.ParkingLot.exceptions.SimultaneousOperationInDatabaseCollectionException;
import com.robert.ParkingLot.parking.ParkingSpot;
import org.junit.jupiter.api.Test;
import com.robert.ParkingLot.parking.Driver;
import com.robert.ParkingLot.parking.ParkingSpotType;
import com.robert.ParkingLot.structures.Ticket;
import com.robert.ParkingLot.vehicles.Motorcycle;
import com.robert.ParkingLot.vehicles.Vehicle;
import com.robert.ParkingLot.vehicles.VehicleType;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VIPElectricTicketGeneratorUnitTest {
    @Test
    public void getSpotFromTheNextParkingSpotCategories() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException, ParkingSpotNotAvailableException {
        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        Vehicle vehicle = mock(Vehicle.class);
        Driver driver = mock(Driver.class);
        when(vehicle.getDriver()).thenReturn(driver);
        when(vehicle.getElectric()).thenReturn(true);
        when(vehicle.getDriver().getVipStatus()).thenReturn(true);
        when(vehicle.getVehicleType()).thenReturn(VehicleType.MOTORCYCLE);

        TicketGenerator ticketGenerator = new VIPElectricTicketGenerator();
        Ticket ticket;

        // When, Then
        when(parkingSpotsCollection.getAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), true)).thenReturn(new ParkingSpot(7, null, ParkingSpotType.SMALL, true, 0));
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        assertEquals(7, ticket.getParkingSpot().getId());

        when(parkingSpotsCollection.getAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), true)).thenReturn(new ParkingSpot(9, null, ParkingSpotType.SMALL, true, 0));
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        assertEquals(9, ticket.getParkingSpot().getId());
    }

    @Test
    public void throwExceptionWhenThereIsNoSpotAvailable() throws ParkingSpotNotFoundException, ParkingSpotNotAvailableException {
        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        Vehicle vehicle = new Motorcycle(new Driver("Andrei", true), "red", 2000, true);

        TicketGenerator ticketGenerator = new VIPElectricTicketGenerator();

        //TODO se poate si cu mai multe when-uri
        when(parkingSpotsCollection.getAvailableParkingSpot(any(ParkingSpotType.class), eq(vehicle.getElectric()))).thenThrow(new ParkingSpotNotAvailableException());
        assertThrowsExactly(ParkingSpotNotAvailableException.class, () -> ticketGenerator.getTicket(parkingSpotsCollection, vehicle));
    }
}
