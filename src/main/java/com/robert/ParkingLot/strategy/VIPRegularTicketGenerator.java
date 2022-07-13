package com.robert.ParkingLot.strategy;


import com.robert.ParkingLot.database.ParkingSpotsCollection;
import com.robert.ParkingLot.exceptions.ParkingSpotNotAvailableException;
import com.robert.ParkingLot.exceptions.ParkingSpotNotFoundException;
import com.robert.ParkingLot.exceptions.SimultaneousOperationInDatabaseCollectionException;
import com.robert.ParkingLot.parking.ParkingSpotType;
import com.robert.ParkingLot.strategy.TicketGenerator;
import com.robert.ParkingLot.strategy.TicketGeneratorUtil;
import com.robert.ParkingLot.structures.Ticket;
import com.robert.ParkingLot.vehicles.Vehicle;

public class VIPRegularTicketGenerator implements TicketGenerator {
    @Override
    public Ticket getTicket(ParkingSpotsCollection parkingSpotsCollection, Vehicle vehicle) throws SimultaneousOperationInDatabaseCollectionException, ParkingSpotNotAvailableException {
        int parkingSpotTypeId = TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()).ordinal();
        while (parkingSpotTypeId < ParkingSpotType.values().length) {
            try {
                int idParkingSpot = findEmptyNonElectricSpotOnCurrentCategory(parkingSpotsCollection, vehicle, parkingSpotTypeId);
                return new Ticket(idParkingSpot, vehicle);
            } catch (ParkingSpotNotAvailableException exception) {
                ++parkingSpotTypeId;
            }
        }

        // Daca nu am iesit cu "return" din while, inseamna ca nu am gasit un loc de parcare.
        throw new ParkingSpotNotAvailableException("notAvailable");
    }

    public int findEmptyNonElectricSpotOnCurrentCategory(ParkingSpotsCollection parkingSpotsCollection, Vehicle vehicle, int parkingSpotTypeId) throws ParkingSpotNotAvailableException {
        int idParkingSpot = parkingSpotsCollection.getIdForAvailableParkingSpot(ParkingSpotType.values()[parkingSpotTypeId], vehicle.getElectric());
        return idParkingSpot;
    }

}
