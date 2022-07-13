package com.robert.ParkingLot.strategy;


import com.robert.ParkingLot.database.ParkingSpotsCollection;
import com.robert.ParkingLot.exceptions.ParkingSpotNotAvailableException;
import com.robert.ParkingLot.exceptions.ParkingSpotNotFoundException;
import com.robert.ParkingLot.exceptions.SimultaneousOperationInDatabaseCollectionException;
import com.robert.ParkingLot.structures.Ticket;
import com.robert.ParkingLot.vehicles.Vehicle;

// Part of Strategy design patter
public interface TicketGenerator
{
    Ticket getTicket(ParkingSpotsCollection parkingSpotsCollection, Vehicle vehicle) throws SimultaneousOperationInDatabaseCollectionException, ParkingSpotNotAvailableException;
}
