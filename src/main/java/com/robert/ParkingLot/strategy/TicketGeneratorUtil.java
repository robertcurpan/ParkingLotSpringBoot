package com.robert.ParkingLot.strategy;


import com.robert.ParkingLot.parking.ParkingSpotType;
import com.robert.ParkingLot.vehicles.VehicleType;

public class TicketGeneratorUtil {

    public static ParkingSpotType getSmallestFittingParkingSpotTypeFromVehicleType(VehicleType vehicleType) {
        ParkingSpotType parkingSpotType = null;
        switch(vehicleType) {
            case MOTORCYCLE: parkingSpotType = ParkingSpotType.SMALL; break;
            case CAR: parkingSpotType = ParkingSpotType.MEDIUM; break;
            case TRUCK: parkingSpotType = ParkingSpotType.LARGE; break;
        }

        return parkingSpotType;
    }
}
