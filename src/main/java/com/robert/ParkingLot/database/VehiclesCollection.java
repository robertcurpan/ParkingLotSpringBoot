package com.robert.ParkingLot.database;

import com.robert.ParkingLot.exceptions.VehicleNotFoundException;
import com.robert.ParkingLot.vehicles.Vehicle;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static dev.morphia.query.experimental.filters.Filters.eq;
import static dev.morphia.query.experimental.filters.Filters.exists;

@Component
public class VehiclesCollection {
    private Datastore vehiclesDatastore;

    public VehiclesCollection(MorphiaDatabase morphiaDatabase) {
        vehiclesDatastore = morphiaDatabase.getDatastore();
    }

    public Vehicle getVehicleById(UUID vehicleId) throws VehicleNotFoundException {
        Query<Vehicle> vehicleQuery = vehiclesDatastore
                .find(Vehicle.class)
                .filter(eq("vehicleId", vehicleId));
        Vehicle vehicle = vehicleQuery.first();

        if(vehicle == null) {
            throw new VehicleNotFoundException("notFound");
        }

        return vehicle;
    }

    public void removeVehicle(Vehicle vehicle) {
        vehiclesDatastore
                .find(Vehicle.class)
                .filter(eq("vehicleId", vehicle.getVehicleId()))
                .delete(new DeleteOptions().multi(false));
    }

    public void addVehicle(Vehicle vehicle) {
        vehiclesDatastore.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = vehiclesDatastore
                .find(Vehicle.class)
                .filter(exists("vehicleId"))
                .iterator()
                .toList();

        return vehicles;
    }

    public void resetVehiclesCollection() {
        vehiclesDatastore.find(Vehicle.class)
                .delete(new DeleteOptions().multi(true));
    }

}
