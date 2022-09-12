package com.robert.ParkingLot.database;

import com.robert.ParkingLot.authentication.UserDetails;
import com.robert.ParkingLot.parking.ParkingSpot;
import com.robert.ParkingLot.structures.Ticket;
import com.robert.ParkingLot.structures.User;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UsersCollection {
    private Datastore usersDatastore;

    public UsersCollection(MorphiaDatabase morphiaDatabase) {
        usersDatastore = morphiaDatabase.getDatastore();
    }

    public List<User> getUsers() {
        List<User> users = usersDatastore.find(User.class).iterator().toList();
        return users;
    }

    public void addUser(User user) {
        usersDatastore.save(user);
    }

    public void resetUsersCollection() {
        usersDatastore.find(User.class)
                .delete(new DeleteOptions().multi(true));
    }

}
