package com.robert.ParkingLot.parking;


import com.robert.ParkingLot.strategy.ElectricTicketGenerator;
import com.robert.ParkingLot.strategy.RegularTicketGenerator;
import com.robert.ParkingLot.strategy.TicketGenerator;
import com.robert.ParkingLot.strategy.VIPElectricTicketGenerator;
import com.robert.ParkingLot.strategy.VIPRegularTicketGenerator;
import com.robert.ParkingLot.vehicles.Vehicle;
import org.springframework.stereotype.Service;

@Service
public class TicketGeneratorCreator {

    public TicketGenerator getTicketGenerator(Vehicle vehicle) {
        // In functie de statutul soferului si de tipul masinii, stabilim strategia potrivita
        boolean isVip = vehicle.getDriver().getVipStatus();
        boolean isElectric = vehicle.getElectric();

        if (isVip && isElectric) {
            return new VIPElectricTicketGenerator();
        }
        if (isVip && !isElectric) {
            return new VIPRegularTicketGenerator();
        }
        if (!isVip && isElectric) {
            return new ElectricTicketGenerator();
        }
        if (!isVip && !isElectric) {
            return new RegularTicketGenerator();
        }
        throw new IllegalStateException();
    }
}
