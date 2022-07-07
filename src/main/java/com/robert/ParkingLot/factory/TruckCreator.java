package com.robert.ParkingLot.factory;

import com.robert.ParkingLot.parking.Driver;
import com.robert.ParkingLot.vehicles.Truck;
import com.robert.ParkingLot.vehicles.Vehicle;

public class TruckCreator extends VehicleCreator {
    @Override
    public Vehicle getVehicle(Driver driver, String color, int price, boolean electric) {
        return new Truck(driver, color, price, electric);
    }
}
