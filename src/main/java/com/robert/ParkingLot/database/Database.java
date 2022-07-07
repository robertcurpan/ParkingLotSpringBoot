package com.robert.ParkingLot.database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
public class Database {
    private MongoDatabase mongoDatabase;

    public Database(@Value("${parkingLotDatabaseName}") String databaseName) {
        MongoClient mongoClient = new MongoClient();
        mongoDatabase = mongoClient.getDatabase(databaseName);
    }

    public MongoDatabase getDatabase() {
        return mongoDatabase;
    }
}
