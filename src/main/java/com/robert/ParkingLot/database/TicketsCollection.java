package com.robert.ParkingLot.database;

import com.robert.ParkingLot.parking.ParkingSpot;
import com.robert.ParkingLot.structures.Ticket;
import com.robert.ParkingLot.structures.User;
import com.robert.ParkingLot.vehicles.Vehicle;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import org.springframework.stereotype.Component;

import java.util.List;

import static dev.morphia.query.experimental.filters.Filters.eq;

@Component
public class TicketsCollection {
    private Datastore ticketsDatastore;

    public TicketsCollection(MorphiaDatabase morphiaDatabase) {
        ticketsDatastore = morphiaDatabase.getDatastore();
    }

    public void addTicket(Ticket ticket) {
        ticketsDatastore.save(ticket);
    }

    public void removeTicked(ParkingSpot parkingSpot) {
        ticketsDatastore
                .find(Ticket.class)
                .filter(eq("parkingSpot.id", parkingSpot.getId()))
                .delete(new DeleteOptions().multi(false));
    }

    public List<Ticket> getTickets() {
        List<Ticket> tickets = ticketsDatastore.find(Ticket.class).iterator().toList();
        return tickets;
    }

    public void resetTicketsCollection() {
        ticketsDatastore.find(Ticket.class)
                .delete(new DeleteOptions().multi(true));
    }


}
