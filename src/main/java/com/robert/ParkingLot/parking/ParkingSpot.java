package com.robert.ParkingLot.parking;
import com.robert.ParkingLot.vehicles.Vehicle;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.UUID;

@Entity("parkingSpotInputs")
public class ParkingSpot
{
    @Id
    private int id;
    private UUID vehicleId;
    private ParkingSpotType spotType;
    private boolean electric;
    private int version;

    public ParkingSpot(int id, UUID vehicleId, ParkingSpotType spotType, boolean electric, int version)
    {
        this.id = id;
        this.vehicleId = vehicleId;
        this.spotType = spotType;
        this.electric = electric;
        this.version = version;
    }

    public int getId() { return this.id; }
    public UUID getVehicleId() { return this.vehicleId; }
    public boolean getElectric() { return this.electric; }
    public ParkingSpotType getSpotType() { return this.spotType; }
    public int getVersion() { return this.version; }


    public void setId(int id) { this.id = id; }
    public void setVehicleId(UUID vehicleId) { this.vehicleId = vehicleId; }
    public void setSpotType(ParkingSpotType spotType) { this.spotType = spotType; }
    public void setElectric(boolean electric) { this.electric = electric; }
    public void setVersion(int version) { this.version = version; }

    public void updateWhenOccupiedByVehicle(Vehicle vehicle) {
        vehicleId = vehicle.getVehicleId();
        ++version;
    }

    public void updateWhenLeftByVehicle() {
        vehicleId = null;
        ++version;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof ParkingSpot) {
            ParkingSpot parkingSpot = (ParkingSpot) object;
            return this.id == parkingSpot.id && this.vehicleId == parkingSpot.vehicleId && this.spotType == parkingSpot.spotType && this.electric == parkingSpot.electric && this.version == parkingSpot.version;
        }
        return false;
    }
    @Override
    public String toString()
    {
        return "Spot with id: " + id + "[" + spotType.toString() + " (vehicleId = " + vehicleId + ") ], electric: " + electric;
    }

}
