package com.robert.ParkingLot.vehicles;

import com.robert.ParkingLot.parking.Driver;

public class Truck extends Vehicle
{
    public Truck(Driver driver, String color, int price, boolean electric)
    {
        super(VehicleType.TRUCK, driver, color, price, electric);
    }

    @Override
    public String getDescription()
    {
        return "The truck is " + color + " and it costs " + price + " euro!" + " [Driver: " + driver.toString() + "]";
    }
}
