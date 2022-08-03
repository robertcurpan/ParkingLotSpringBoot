package com.robert.ParkingLot.strategy;

import com.robert.ParkingLot.database.VehiclesCollection;
import com.robert.ParkingLot.database.ParkingSpotsCollection;
import com.robert.ParkingLot.exceptions.ParkingSpotNotAvailableException;
import com.robert.ParkingLot.exceptions.ParkingSpotNotFoundException;
import com.robert.ParkingLot.exceptions.SimultaneousOperationInDatabaseCollectionException;
import com.robert.ParkingLot.parking.ParkingSpot;
import org.junit.jupiter.api.Test;
import com.robert.ParkingLot.parking.Driver;
import com.robert.ParkingLot.parking.ParkingSpotType;
import com.robert.ParkingLot.structures.Ticket;
import com.robert.ParkingLot.vehicles.Car;
import com.robert.ParkingLot.vehicles.Motorcycle;
import com.robert.ParkingLot.vehicles.Vehicle;
import com.robert.ParkingLot.vehicles.VehicleType;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RegularTicketGeneratorUnitTest {
    @Test
    public void throwExceptionWhenNoSpotIsAvailable() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException, ParkingSpotNotAvailableException {
        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        Vehicle vehicle = new Motorcycle(new Driver("Andrei", false), "red", 2000, false);

        TicketGenerator ticketGenerator = new RegularTicketGenerator();
        Ticket ticket;

        // When
        when(parkingSpotsCollection.getAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), vehicle.getElectric())).thenReturn(new ParkingSpot(1, null, ParkingSpotType.SMALL, true, 0));
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        assertEquals(1, ticket.getParkingSpot().getId());

        when(parkingSpotsCollection.getAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), vehicle.getElectric())).thenReturn(new ParkingSpot(3, null, ParkingSpotType.SMALL, true, 0));
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        assertEquals(3, ticket.getParkingSpot().getId());

        when(parkingSpotsCollection.getAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), vehicle.getElectric())).thenThrow(new ParkingSpotNotAvailableException());
        assertThrowsExactly(ParkingSpotNotAvailableException.class, () -> ticketGenerator.getTicket(parkingSpotsCollection, vehicle));
    }
}
