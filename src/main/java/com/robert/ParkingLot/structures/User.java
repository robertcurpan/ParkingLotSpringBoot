package com.robert.ParkingLot.structures;


import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.UUID;

@Entity("users")
public class User {
    @Id
    private UUID id;
    private String username;
    private String hashedPassword;
    private String accountType;

    public User(String username, String hashedPassword, String accountType) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.accountType = accountType;
        this.id = UUID.randomUUID();
    }

    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getHashedPassword() { return hashedPassword; }
    public String getAccountType() { return accountType; }

    @Override
    public boolean equals(Object object) {
        if(object instanceof User) {
            User user = (User) object;
            return this.username.equals(user.getUsername()) && this.hashedPassword.equals(user.getHashedPassword()) && this.accountType.equals(user.getAccountType());
        }
        return false;
    }
}
