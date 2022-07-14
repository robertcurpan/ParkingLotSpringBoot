package com.robert.ParkingLot.database;

import com.mongodb.client.result.UpdateResult;
import com.robert.ParkingLot.exceptions.ParkingSpotNotAvailableException;
import com.robert.ParkingLot.exceptions.ParkingSpotNotFoundException;
import com.robert.ParkingLot.exceptions.SimultaneousOperationInDatabaseCollectionException;
import com.robert.ParkingLot.parking.ParkingSpot;
import com.robert.ParkingLot.parking.ParkingSpotType;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.query.Query;
import dev.morphia.query.experimental.updates.UpdateOperators;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.morphia.query.experimental.filters.Filters.*;


/*
!!! Functiile din baza de date trebuie sa stie doar de operatii CRUD. Noile valori in cazul unui update (sau valorile in cazul unui insert) sunt preluate din obiectele date ca parametru
 */
@Component
public class ParkingSpotsCollection {
    private Datastore parkingSpotsDatastore;

    public ParkingSpotsCollection(MorphiaDatabase morphiaDatabase) {
        parkingSpotsDatastore = morphiaDatabase.getDatastore();
    }

    // Preiau starea obiectului parkingSpot (care reprezinta starea finala) si actualizez in baza de date folosind aceasta stare
    public void updateParkingSpotWhenDriverLeaves(ParkingSpot parkingSpot) throws SimultaneousOperationInDatabaseCollectionException {
        Query<ParkingSpot> parkingSpotQuery = parkingSpotsDatastore
                .find(ParkingSpot.class)
                .filter(eq("id", parkingSpot.getId()), eq("version", parkingSpot.getVersion()));

        UpdateResult updateResult = parkingSpotQuery
                .update(UpdateOperators.unset("vehicleId"), UpdateOperators.set("version", parkingSpot.getVersion() + 1))
                .execute();

        if (updateResult.getMatchedCount() == 0)
            throw new SimultaneousOperationInDatabaseCollectionException("optimisticLocking");
    }

    public void updateParkingSpotWhenDriverParks(ParkingSpot parkingSpot) throws SimultaneousOperationInDatabaseCollectionException {
        Query<ParkingSpot> parkingSpotQuery = parkingSpotsDatastore
                .find(ParkingSpot.class)
                .filter(eq("id", parkingSpot.getId()))
                .filter(eq("version", parkingSpot.getVersion()));

        UpdateResult updateResult = parkingSpotQuery
                .update(UpdateOperators.set("vehicleId", parkingSpot.getVehicleId()), UpdateOperators.set("version", parkingSpot.getVersion() + 1))
                .execute();

        if (updateResult.getMatchedCount() == 0)
            throw new SimultaneousOperationInDatabaseCollectionException("optimisticLocking");
    }

    public int getIdForAvailableParkingSpot(ParkingSpotType parkingSpotType, boolean electric) throws ParkingSpotNotAvailableException {
        Query<ParkingSpot> parkingSpotQuery = parkingSpotsDatastore
                .find(ParkingSpot.class)
                .filter(eq("spotType", parkingSpotType))
                .filter(eq("electric", electric))
                .filter(nor(exists("vehicleId")));
        ParkingSpot parkingSpot = parkingSpotQuery.first();

        if(parkingSpot == null) {
            throw new ParkingSpotNotAvailableException("notAvailable");
        }

        return parkingSpot.getId();
    }

    public List<ParkingSpot> getParkingSpots() {
        List<ParkingSpot> parkingSpots = parkingSpotsDatastore.find(ParkingSpot.class).iterator().toList();
        return parkingSpots;

    }

    public ParkingSpot getParkingSpotById(int parkingSpotId) throws ParkingSpotNotFoundException {
        Query<ParkingSpot> parkingSpotQuery = parkingSpotsDatastore
                .find(ParkingSpot.class)
                .filter(eq("id", parkingSpotId));
        ParkingSpot parkingSpot = parkingSpotQuery.first();

        if(parkingSpot == null) {
            throw new ParkingSpotNotFoundException("notFound");
        }

        return parkingSpot;
    }

    public boolean isParkingSpotFree(ParkingSpot parkingSpot) {
        Query<ParkingSpot> parkingSpotQuery = parkingSpotsDatastore
                .find(ParkingSpot.class)
                .filter(exists("vehicleId"))
                .filter(eq("id", parkingSpot.getId()));

        ParkingSpot parkingSpotFound = parkingSpotQuery.first();
        return parkingSpotFound == null;
    }

    public void initializeParkingSpotsCollection() {
        ParkingSpot parkingSpot1 = new ParkingSpot(7, null, ParkingSpotType.SMALL, true, 1);
        ParkingSpot parkingSpot2 = new ParkingSpot(2, null, ParkingSpotType.SMALL, false, 1);
        ParkingSpot parkingSpot3 = new ParkingSpot(1, null, ParkingSpotType.MEDIUM, false, 1);
        ParkingSpot parkingSpot4 = new ParkingSpot(3, null, ParkingSpotType.MEDIUM, false, 1);
        ParkingSpot parkingSpot5 = new ParkingSpot(9, null, ParkingSpotType.MEDIUM, true, 1);
        ParkingSpot parkingSpot6 = new ParkingSpot(6, null, ParkingSpotType.LARGE, true, 1);
        ParkingSpot parkingSpot7 = new ParkingSpot(5, null, ParkingSpotType.LARGE, true, 1);
        ParkingSpot parkingSpot8 = new ParkingSpot(8, null, ParkingSpotType.LARGE, false, 1);
        ParkingSpot parkingSpot9 = new ParkingSpot(4, null, ParkingSpotType.LARGE, true, 1);

        List<ParkingSpot> parkingSpots = new ArrayList<>(Arrays.asList(parkingSpot1, parkingSpot2, parkingSpot3, parkingSpot4, parkingSpot5, parkingSpot6, parkingSpot7, parkingSpot8, parkingSpot9));
        parkingSpotsDatastore.save(parkingSpots);
    }
    public void resetParkingSpotsCollection() {
        // Delete all documents from "parkingSpotInputs" collection
        parkingSpotsDatastore.find(ParkingSpot.class)
                .delete(new DeleteOptions().multi(true));
    }


}
