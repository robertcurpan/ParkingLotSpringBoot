package com.robert.ParkingLot.strategy;


import com.robert.ParkingLot.database.ParkingSpotsCollection;
import com.robert.ParkingLot.exceptions.ParkingSpotNotAvailableException;
import com.robert.ParkingLot.exceptions.ParkingSpotNotFoundException;
import com.robert.ParkingLot.exceptions.SimultaneousOperationInDatabaseCollectionException;
import com.robert.ParkingLot.parking.ParkingSpotType;
import com.robert.ParkingLot.structures.Ticket;
import com.robert.ParkingLot.vehicles.Vehicle;

public class ElectricTicketGenerator implements TicketGenerator
{
    @Override
    public Ticket getTicket(ParkingSpotsCollection parkingSpotsCollection, Vehicle vehicle) throws SimultaneousOperationInDatabaseCollectionException, ParkingSpotNotAvailableException {
        // Caut un loc de parcare liber, cu charger electric si specific vehiculului soferului
        int idParkingSpot = parkingSpotsCollection.getIdForAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), true);
        return new Ticket(idParkingSpot, vehicle);
    }

}
