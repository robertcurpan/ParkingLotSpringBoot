package com.robert.ParkingLot.parking;

import org.junit.jupiter.api.Test;
import com.robert.ParkingLot.strategy.*;
import com.robert.ParkingLot.vehicles.Car;
import com.robert.ParkingLot.vehicles.Vehicle;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TicketGeneratorCreatorUnitTest {

    @Test
    public void getNonVIPElectricTicketGenerator() {
        Driver driver = new Driver("Robert", false);
        Vehicle vehicle = new Car(driver, "red", 2000, true);
        TicketGeneratorCreator ticketGeneratorCreator = new TicketGeneratorCreator();

        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(vehicle);

        assertEquals(ElectricTicketGenerator.class, ticketGenerator.getClass());
    }


    @Test
    public void getNonVIPRegularTicketGenerator() {
        Driver driver = new Driver("Robert", false);
        Vehicle vehicle = new Car(driver, "red", 2000, false);
        TicketGeneratorCreator ticketGeneratorCreator = new TicketGeneratorCreator();

        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(vehicle);

        assertEquals(RegularTicketGenerator.class, ticketGenerator.getClass());
    }


    @Test
    public void getVIPElectricTicketGenerator() {
        Driver driver = new Driver("Robert", true);
        Vehicle vehicle = new Car(driver, "red", 2000, true);
        TicketGeneratorCreator ticketGeneratorCreator = new TicketGeneratorCreator();

        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(vehicle);

        assertEquals(VIPElectricTicketGenerator.class, ticketGenerator.getClass());
    }


    @Test
    public void getVIPRegularTicketGenerator() {
        Driver driver = new Driver("Robert", true);
        Vehicle vehicle = new Car(driver, "red", 2000, false);
        TicketGeneratorCreator ticketGeneratorCreator = new TicketGeneratorCreator();

        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(vehicle);

        assertEquals(VIPRegularTicketGenerator.class, ticketGenerator.getClass());
    }
}
