package com.robert.ParkingLot.strategy;


import com.robert.ParkingLot.database.ParkingSpotsCollection;
import com.robert.ParkingLot.exceptions.ParkingSpotNotFoundException;
import com.robert.ParkingLot.exceptions.SimultaneousOperationInDatabaseCollectionException;
import com.robert.ParkingLot.structures.Ticket;
import com.robert.ParkingLot.vehicles.Vehicle;

public class RegularTicketGenerator implements TicketGenerator
{
    @Override
    public Ticket getTicket(ParkingSpotsCollection parkingSpotsCollection, Vehicle vehicle) throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        // Caut un loc de parcare liber, cu charger electric si specific vehiculului soferului
        int idParkingSpot = parkingSpotsCollection.getIdForAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), false);
        return new Ticket(idParkingSpot, vehicle);
    }

}
