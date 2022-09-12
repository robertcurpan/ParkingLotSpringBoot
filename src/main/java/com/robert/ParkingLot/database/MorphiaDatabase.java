package com.robert.ParkingLot.database;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.bson.UuidRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MorphiaDatabase {
    private Datastore datastore;

    public MorphiaDatabase(@Value("${parkingLotDatabaseName}") String databaseName) {
        MongoClientSettings clientSettings = MongoClientSettings.builder().uuidRepresentation(UuidRepresentation.STANDARD).build();
        datastore = Morphia.createDatastore(MongoClients.create(clientSettings), databaseName);
        datastore.getMapper().mapPackage("com.robert.ParkingLot.parking");
        datastore.getMapper().mapPackage("com.robert.ParkingLot.vehicles");
        datastore.getMapper().mapPackage("com.robert.ParkingLot.structures");
        datastore.ensureIndexes();
    }

    public Datastore getDatastore() { return datastore; }
}
