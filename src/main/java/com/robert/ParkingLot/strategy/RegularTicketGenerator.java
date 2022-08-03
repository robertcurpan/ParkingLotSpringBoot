package com.robert.ParkingLot.strategy;


import com.robert.ParkingLot.database.ParkingSpotsCollection;
import com.robert.ParkingLot.exceptions.ParkingSpotNotAvailableException;
import com.robert.ParkingLot.exceptions.ParkingSpotNotFoundException;
import com.robert.ParkingLot.exceptions.SimultaneousOperationInDatabaseCollectionException;
import com.robert.ParkingLot.parking.ParkingSpot;
import com.robert.ParkingLot.structures.Ticket;
import com.robert.ParkingLot.vehicles.Vehicle;

public class RegularTicketGenerator implements TicketGenerator
{
    @Override
    public Ticket getTicket(ParkingSpotsCollection parkingSpotsCollection, Vehicle vehicle) throws  SimultaneousOperationInDatabaseCollectionException, ParkingSpotNotAvailableException {
        // Caut un loc de parcare liber, cu charger electric si specific vehiculului soferului
        ParkingSpot parkingSpot = parkingSpotsCollection.getAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), false);
        return new Ticket(parkingSpot, vehicle);
    }

}
