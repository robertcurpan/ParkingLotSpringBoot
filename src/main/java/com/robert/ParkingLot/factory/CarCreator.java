package com.robert.ParkingLot.factory;


import com.robert.ParkingLot.parking.Driver;
import com.robert.ParkingLot.vehicles.Car;
import com.robert.ParkingLot.vehicles.Vehicle;

public class CarCreator extends VehicleCreator {
    @Override
    public Vehicle getVehicle(Driver driver, String color, int price, boolean electric) {
        return new Car(driver, color, price, electric);
    }
}
