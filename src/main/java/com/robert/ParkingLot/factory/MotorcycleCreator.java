package com.robert.ParkingLot.factory;


import com.robert.ParkingLot.parking.Driver;
import com.robert.ParkingLot.vehicles.Motorcycle;
import com.robert.ParkingLot.vehicles.Vehicle;

public class MotorcycleCreator extends VehicleCreator {
    @Override
    public Vehicle getVehicle(Driver driver, String color, int price, boolean electric) {
        return new Motorcycle(driver, color, price, electric);
    }
}
