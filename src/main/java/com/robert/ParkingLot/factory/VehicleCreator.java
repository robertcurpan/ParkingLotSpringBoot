package com.robert.ParkingLot.factory;

import com.robert.ParkingLot.parking.Driver;
import com.robert.ParkingLot.vehicles.Vehicle;

public abstract class VehicleCreator {
    public abstract Vehicle getVehicle(Driver driver, String color, int price, boolean electric);
}
