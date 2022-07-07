package com.robert.ParkingLot.vehicles;

import com.robert.ParkingLot.factory.VehicleCreatorGenerator;
import com.robert.ParkingLot.parking.Driver;

public class CreateVehicleFromJsonUtil {

    public static Vehicle createVehicle(VehicleJson vehicleJson) {
        VehicleType vehicleType = VehicleType.valueOf(vehicleJson.getType());
        Driver driver = new Driver(vehicleJson.getDriver().getName(), vehicleJson.getDriver().getVipStatus());
        String color = vehicleJson.getColor();
        int price = vehicleJson.getPrice();
        boolean electric = vehicleJson.getElectric();

        return VehicleCreatorGenerator.getVehicleCreator(vehicleType).getVehicle(driver, color, price, electric);
    }

}
