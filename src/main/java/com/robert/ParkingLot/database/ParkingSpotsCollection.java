package com.robert.ParkingLot.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import com.robert.ParkingLot.exceptions.ParkingSpotNotAvailableException;
import com.robert.ParkingLot.exceptions.ParkingSpotNotFoundException;
import com.robert.ParkingLot.exceptions.SimultaneousOperationInDatabaseCollectionException;
import com.robert.ParkingLot.parking.ParkingSpot;
import com.robert.ParkingLot.parking.ParkingSpotType;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/*
!!! Functiile din baza de date trebuie sa stie doar de operatii CRUD. Noile valori in cazul unui update (sau valorile in cazul unui insert) sunt preluate din obiectele date ca parametru
 */
@Component
public class ParkingSpotsCollection {
    private MongoCollection<Document> parkingSpotsCollection;

    public ParkingSpotsCollection(Database database) {
        parkingSpotsCollection = database.getDatabase().getCollection("parkingSpotInputs");
    }

    // Preiau starea obiectului parkingSpot (care reprezinta starea finala) si actualizez in baza de date folosind aceasta stare
    public void updateParkingSpotWhenDriverLeaves(ParkingSpot parkingSpot) throws SimultaneousOperationInDatabaseCollectionException {
        // Trebuie sa facem locul "free" (eliberam locul de parcare)
        int version = parkingSpot.getVersion();
        Bson filterParkingSpotId = Filters.eq("parkingSpotId", parkingSpot.getId());
        Bson filterVersion = Filters.eq("version", version);

        UpdateResult updateResult = parkingSpotsCollection.updateOne(Filters.and(filterParkingSpotId, filterVersion), Updates.combine(Updates.unset("vehicleId"), Updates.set("version", version + 1)));
        if(updateResult.getMatchedCount() == 0)
            throw new SimultaneousOperationInDatabaseCollectionException("optimisticLocking");
    }

    public void updateParkingSpotWhenDriverParks(ParkingSpot parkingSpot) throws SimultaneousOperationInDatabaseCollectionException {
        int version = parkingSpot.getVersion();
        Bson filterParkingSpotId = Filters.eq("parkingSpotId", parkingSpot.getId());
        Bson filterVersion = Filters.eq("version", version);

        UpdateResult updateResult = parkingSpotsCollection.updateOne(Filters.and(filterParkingSpotId, filterVersion), Updates.combine(Updates.set("vehicleId", parkingSpot.getVehicleId()), Updates.set("version", version + 1)));
        if(updateResult.getMatchedCount() == 0)
            throw new SimultaneousOperationInDatabaseCollectionException("optimisticLocking");
    }

    public int getIdForAvailableParkingSpot(ParkingSpotType parkingSpotType, boolean electric) throws ParkingSpotNotAvailableException {
        Bson vehicleTypeFilter = Filters.eq("parkingSpotType", parkingSpotType.toString());
        Bson electricFilter = Filters.eq("electricType", electric ? "electric" : "nonelectric");
        Bson freeFilter = Filters.not(Filters.exists("vehicleId"));

        Document parkingSpotDocument = parkingSpotsCollection.find(Filters.and(vehicleTypeFilter, electricFilter, freeFilter)).first();
        if(parkingSpotDocument == null) {
            throw new ParkingSpotNotAvailableException("notAvailable");
        }

        // Am gasit un parking spot cu cerintele cautate
        return (int) parkingSpotDocument.get("parkingSpotId");
    }

    public List<ParkingSpot> getParkingSpots() {
        List<ParkingSpot> parkingSpots = new ArrayList<>();

        MongoCursor<Document> cursor = this.parkingSpotsCollection.find().iterator();
        while(cursor.hasNext()) {
            Document parkingSpotDocument = cursor.next();
            int spotId = (int) parkingSpotDocument.get("parkingSpotId");
            UUID vehicleId = (UUID) parkingSpotDocument.get("vehicleId");
            String electricType = (String) parkingSpotDocument.get("electricType");
            boolean electric = electricType.equals("electric");
            int version = (int) parkingSpotDocument.get("version");
            String parkingSpotTypeString = (String) parkingSpotDocument.get("parkingSpotType");
            ParkingSpotType parkingSpotType = ParkingSpotType.valueOf(parkingSpotTypeString);

            parkingSpots.add(new ParkingSpot(spotId, vehicleId, parkingSpotType, electric, version));
        }

        return parkingSpots;
    }

    public ParkingSpot getParkingSpotById(int parkingSpotId) throws ParkingSpotNotFoundException {
        Bson filterId = Filters.eq("parkingSpotId", parkingSpotId);
        Document parkingSpotDocument = parkingSpotsCollection.find(filterId).first();
        if(parkingSpotDocument == null) {
            throw new ParkingSpotNotFoundException("notFound");
        }

        UUID vehicleId = (UUID) parkingSpotDocument.get("vehicleId");
        String parkingSpotTypeString = (String) parkingSpotDocument.get("parkingSpotType");
        String electricString = (String) parkingSpotDocument.get("electricType");
        ParkingSpotType parkingSpotType = ParkingSpotType.valueOf(parkingSpotTypeString);
        boolean electric = electricString.equals("electric");
        int version = (int) parkingSpotDocument.get("version");

        return new ParkingSpot(parkingSpotId, vehicleId, parkingSpotType, electric, version);
    }

    public boolean isParkingSpotFree(ParkingSpot parkingSpot) {
        Document parkingSpotDocument = parkingSpotsCollection.find(Filters.and(Filters.exists("vehicleId"), Filters.eq("parkingSpotId", parkingSpot.getId()))).first();
        return parkingSpotDocument == null; // daca nu am gasit un document ce contine field-ul vehicleId, inseamna ca acel loc de parcare nu e ocupat -> returnez true la metoda isFree
    }

    public void initializeParkingSpotsCollection() {
        Document document1 = new Document();
        document1.append("parkingSpotId", 7);
        document1.append("electricType", "electric");
        document1.append("parkingSpotType", "SMALL");
        document1.append("version", 1);

        Document document2 = new Document();
        document2.append("parkingSpotId", 2);
        document2.append("electricType", "nonelectric");
        document2.append("parkingSpotType", "SMALL");
        document2.append("version", 1);

        Document document3 = new Document();
        document3.append("parkingSpotId", 1);
        document3.append("electricType", "nonelectric");
        document3.append("parkingSpotType", "MEDIUM");
        document3.append("version", 1);

        Document document4 = new Document();
        document4.append("parkingSpotId", 3);
        document4.append("electricType", "nonelectric");
        document4.append("parkingSpotType", "MEDIUM");
        document4.append("version", 1);

        Document document5 = new Document();
        document5.append("parkingSpotId", 9);
        document5.append("electricType", "electric");
        document5.append("parkingSpotType", "MEDIUM");
        document5.append("version", 1);

        Document document6 = new Document();
        document6.append("parkingSpotId", 6);
        document6.append("electricType", "electric");
        document6.append("parkingSpotType", "LARGE");
        document6.append("version", 1);

        Document document7 = new Document();
        document7.append("parkingSpotId", 5);
        document7.append("electricType", "electric");
        document7.append("parkingSpotType", "LARGE");
        document7.append("version", 1);

        Document document8 = new Document();
        document8.append("parkingSpotId", 8);
        document8.append("electricType", "nonelectric");
        document8.append("parkingSpotType", "LARGE");
        document8.append("version", 1);

        Document document9 = new Document();
        document9.append("parkingSpotId", 4);
        document9.append("electricType", "electric");
        document9.append("parkingSpotType", "LARGE");
        document9.append("version", 1);

        parkingSpotsCollection.insertOne(document1);
        parkingSpotsCollection.insertOne(document2);
        parkingSpotsCollection.insertOne(document3);
        parkingSpotsCollection.insertOne(document4);
        parkingSpotsCollection.insertOne(document5);
        parkingSpotsCollection.insertOne(document6);
        parkingSpotsCollection.insertOne(document7);
        parkingSpotsCollection.insertOne(document8);
        parkingSpotsCollection.insertOne(document9);
    }
    public void resetParkingSpotsCollection() {
        // Delete all documents from "parkingSpotInputs" collection
        parkingSpotsCollection.deleteMany(new Document());
    }


}
