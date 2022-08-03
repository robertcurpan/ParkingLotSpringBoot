package com.robert.ParkingLot.parking;

import com.robert.ParkingLot.database.VehiclesCollection;
import com.robert.ParkingLot.database.ParkingSpotsCollection;
import com.robert.ParkingLot.exceptions.*;
import org.junit.jupiter.api.Test;
import com.robert.ParkingLot.strategy.TicketGenerator;
import com.robert.ParkingLot.structures.Ticket;
import com.robert.ParkingLot.vehicles.Car;
import com.robert.ParkingLot.vehicles.Motorcycle;
import com.robert.ParkingLot.vehicles.Vehicle;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ParkingLotUnitTest {
    @Test
    public void getCorrectParkingSpotIdAndVerifyMethodCalls() throws ParkingLotGeneralException {
        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        VehiclesCollection vehiclesCollection = mock(VehiclesCollection.class);
        TicketGeneratorCreator ticketGeneratorCreator = mock(TicketGeneratorCreator.class);
        TicketGenerator ticketGenerator = mock(TicketGenerator.class);
        Ticket ticketMock = mock(Ticket.class);
        ParkingSpot parkingSpotMock = mock(ParkingSpot.class);

        ParkingLotService parkingLotService = new ParkingLotService(ticketGeneratorCreator, parkingSpotsCollection, vehiclesCollection);
        Driver driver = new Driver("Robert", false);
        Vehicle vehicle = new Car(driver, "red", 2000, false);

        when(ticketGeneratorCreator.getTicketGenerator(vehicle)).thenReturn(ticketGenerator);
        when(ticketGenerator.getTicket(parkingSpotsCollection, vehicle)).thenReturn(ticketMock);
        when(ticketMock.getParkingSpot()).thenReturn(parkingSpotMock);

        // When
        Ticket ticket = parkingLotService.getParkingTicket(vehicle);

        // Then
        verify(ticketGeneratorCreator).getTicketGenerator(vehicle); // verific ca atunci cand se apeleaza getParkingTicket apeleaza metoda getTicketGenerator
        verify(ticketGenerator).getTicket(parkingSpotsCollection, vehicle);
        verify(parkingSpotsCollection).updateParkingSpotWhenDriverParks(parkingSpotMock);
        verify(vehiclesCollection).addVehicle(vehicle);
    }

    @Test
    public void throwExceptionWhenLeavingAFreeParkingSpot() throws ParkingSpotNotOccupiedException, ParkingSpotNotFoundException {
        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        VehiclesCollection vehiclesCollection = mock(VehiclesCollection.class);
        TicketGeneratorCreator ticketGeneratorCreator = mock(TicketGeneratorCreator.class);
        ParkingSpot parkingSpot = mock(ParkingSpot.class);
        ParkingLotService parkingLotService = new ParkingLotService(ticketGeneratorCreator, parkingSpotsCollection, vehiclesCollection);

        when(parkingSpotsCollection.getParkingSpotById(0)).thenReturn(parkingSpot);
        when(parkingSpotsCollection.isParkingSpotFree(parkingSpot)).thenReturn(true);

        assertThrowsExactly(ParkingSpotNotOccupiedException.class, () -> parkingLotService.leaveParkingLot(0));
    }

}