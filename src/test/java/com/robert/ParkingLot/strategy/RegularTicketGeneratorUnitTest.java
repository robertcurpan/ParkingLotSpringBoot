package com.robert.ParkingLot.strategy;

import com.robert.ParkingLot.database.VehiclesCollection;
import com.robert.ParkingLot.database.ParkingSpotsCollection;
import com.robert.ParkingLot.exceptions.ParkingSpotNotFoundException;
import com.robert.ParkingLot.exceptions.SimultaneousOperationInDatabaseCollectionException;
import org.junit.jupiter.api.Test;
import com.robert.ParkingLot.parking.Driver;
import com.robert.ParkingLot.parking.ParkingSpotType;
import com.robert.ParkingLot.structures.Ticket;
import com.robert.ParkingLot.vehicles.Car;
import com.robert.ParkingLot.vehicles.Motorcycle;
import com.robert.ParkingLot.vehicles.Vehicle;
import com.robert.ParkingLot.vehicles.VehicleType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegularTicketGeneratorUnitTest {
    @Test
    public void throwExceptionWhenNoSpotIsAvailable() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        Vehicle vehicle = new Motorcycle(new Driver("Andrei", false), "red", 2000, false);

        TicketGenerator ticketGenerator = new RegularTicketGenerator();
        Ticket ticket;

        // When
        when(parkingSpotsCollection.getIdForAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), vehicle.getElectric())).thenReturn(1);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        assertEquals(1, ticket.getSpotId());

        when(parkingSpotsCollection.getIdForAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), vehicle.getElectric())).thenReturn(3);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        assertEquals(3, ticket.getSpotId());

        when(parkingSpotsCollection.getIdForAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), vehicle.getElectric())).thenThrow(new ParkingSpotNotFoundException());
        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> ticketGenerator.getTicket(parkingSpotsCollection, vehicle));
    }
}
