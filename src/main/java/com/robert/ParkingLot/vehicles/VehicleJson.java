package com.robert.ParkingLot.vehicles;

import com.robert.ParkingLot.parking.Driver;

public class VehicleJson {
    private String vehicleType;
    private Driver driver;
    private String color;
    private int price;
    private boolean electric;

    public VehicleJson() {

    }

    public VehicleJson(String vehicleType, Driver driver, String color, int price, boolean electric) {
        this.vehicleType = vehicleType;
        this.driver = driver;
        this.color = color;
        this.price = price;
        this.electric = electric;
    }

    public String getVehicleType() { return vehicleType; }
    public Driver getDriver() { return driver; }
    public String getColor() { return color; }
    public int getPrice() { return price; }
    public boolean getElectric() { return electric; }

    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public void setDriver(Driver driver) { this.driver = driver; }
    public void setColor(String color) { this.color = color; }
    public void setPrice(int price) { this.price = price; }
    public void setElectric(boolean electric) { this.electric = electric; }

    @Override
    public String toString() {
        return vehicleType + " / " + driver.toString() + " / " + color + " / " + price + " / " + electric + "\n";
    }
}
