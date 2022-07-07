package com.robert.ParkingLot.vehicles;

import com.robert.ParkingLot.parking.Driver;

public class Car extends Vehicle
{
    public Car(Driver driver, String color, int price, boolean electric)
    {
        super(VehicleType.CAR, driver, color, price, electric);
    }

    @Override
    public String getDescription()
    {
        return "The car is " + color + " and it costs " + price + " euro!" + " [Driver: " + driver.toString() + "]";
    }
}
