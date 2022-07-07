package com.robert.ParkingLot.strategy;

import com.robert.ParkingLot.database.VehiclesCollection;
import com.robert.ParkingLot.database.ParkingSpotsCollection;
import com.robert.ParkingLot.exceptions.ParkingSpotNotFoundException;
import com.robert.ParkingLot.exceptions.SimultaneousOperationInDatabaseCollectionException;
import org.junit.jupiter.api.Test;
import com.robert.ParkingLot.parking.Driver;
import com.robert.ParkingLot.parking.ParkingSpotType;
import com.robert.ParkingLot.structures.Ticket;
import com.robert.ParkingLot.vehicles.Motorcycle;
import com.robert.ParkingLot.vehicles.Truck;
import com.robert.ParkingLot.vehicles.Vehicle;
import com.robert.ParkingLot.vehicles.VehicleType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VIPRegularTicketGeneratorUnitTest {
    @Test
    public void getSpotFromTheNextParkingSpotCategories() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        Vehicle vehicle = new Motorcycle(new Driver("Andrei", true), "red", 2000, false);

        TicketGenerator ticketGenerator = new VIPRegularTicketGenerator();
        Ticket ticket;

        // When
        when(parkingSpotsCollection.getIdForAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), false)).thenReturn(2);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        assertEquals(2, ticket.getSpotId());

        when(parkingSpotsCollection.getIdForAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), false)).thenReturn(1);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        assertEquals(1, ticket.getSpotId());

        when(parkingSpotsCollection.getIdForAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), false)).thenReturn(3);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        assertEquals(3, ticket.getSpotId());

        when(parkingSpotsCollection.getIdForAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), false)).thenReturn(8);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        assertEquals(8, ticket.getSpotId());
    }

    @Test
    public void throwExceptionWhenThereIsNoSpotAvailable() throws ParkingSpotNotFoundException {
        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        Vehicle vehicle = new Motorcycle(new Driver("Andrei", true), "red", 2000, false);

        TicketGenerator ticketGenerator = new VIPRegularTicketGenerator();

        when(parkingSpotsCollection.getIdForAvailableParkingSpot(any(ParkingSpotType.class), eq(vehicle.getElectric()))).thenThrow(new ParkingSpotNotFoundException());
        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> ticketGenerator.getTicket(parkingSpotsCollection, vehicle));
    }
}
