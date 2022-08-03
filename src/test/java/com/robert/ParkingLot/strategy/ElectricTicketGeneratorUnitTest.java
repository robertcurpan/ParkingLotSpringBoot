package com.robert.ParkingLot.strategy;

import com.robert.ParkingLot.database.ParkingSpotsCollection;
import com.robert.ParkingLot.exceptions.ParkingLotGeneralException;
import com.robert.ParkingLot.exceptions.ParkingSpotNotAvailableException;
import org.junit.jupiter.api.Test;
import com.robert.ParkingLot.parking.*;
import com.robert.ParkingLot.structures.Ticket;
import com.robert.ParkingLot.vehicles.Motorcycle;
import com.robert.ParkingLot.vehicles.Vehicle;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ElectricTicketGeneratorUnitTest {
    @Test
    public void throwExceptionWhenNoSpotIsAvailable() throws ParkingLotGeneralException {
        // MOCKUIM ParkingSpotsColleciton deoarece aici testam ELECTRIC TICKET GENERATOR, nu ParkingSpotsCollection

        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        Vehicle vehicle = new Motorcycle(new Driver("Andrei", false), "red", 2000, true);

        // Creat obiectul manual
        TicketGenerator ticketGenerator = new ElectricTicketGenerator();
        Ticket ticket;

        // When
        when(parkingSpotsCollection.getAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), vehicle.getElectric())).thenReturn(new ParkingSpot(9, null, ParkingSpotType.SMALL, true, 0));
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        assertEquals(9, ticket.getParkingSpot().getId());

        // getParkingSpotId() arunca RuntimeException (aceasta e prinsa in ElectricTicketGenerator si in catch se arunca ParkingSpotNotFoundException)
        when(parkingSpotsCollection.getAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), vehicle.getElectric())).thenThrow(new ParkingSpotNotAvailableException());
        assertThrowsExactly(ParkingSpotNotAvailableException.class, () -> ticketGenerator.getTicket(parkingSpotsCollection, vehicle)); // nu mai exista locuri libere pt un sofer non-vip cu masina electrica
    }

}
