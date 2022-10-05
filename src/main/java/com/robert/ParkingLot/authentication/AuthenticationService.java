package com.robert.ParkingLot.authentication;

import com.robert.ParkingLot.database.UsersCollection;
import com.robert.ParkingLot.exceptions.ParkingLotGeneralException;
import com.robert.ParkingLot.structures.User;
import com.robert.ParkingLot.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AuthenticationService {
    @Autowired
    private JwtUtil jwtUtil;
    private UsersCollection usersCollection;

    public AuthenticationService(UsersCollection usersCollection) {
        this.usersCollection = usersCollection;
    }

    public String verifyLoginAndGenerateJwt(UserDetails userDetails) throws ParkingLotGeneralException {
        if(!isValidUser(userDetails))
            throw new ParkingLotGeneralException("authenticationFailed");

        return jwtUtil.generateToken(userDetails);
    }

    public boolean isValidUser(UserDetails userDetails) {
        List<User> users = usersCollection.getUsers();
        for(User user : users) {
            if(Objects.equals(user.getUsername(), userDetails.getUsername()) && BCrypt.checkpw(userDetails.getPassword(), user.getHashedPassword())) {
                return true;
            }
        }
        return false;
    }

    public User getUserByUsername(String username) {
        List<User> users = usersCollection.getUsers();
        for(User user : users) {
            if(Objects.equals(user.getUsername(), username)) {
                return user;
            }
        }

        throw new RuntimeException();
    }

    public void registerUser(User user) {
        usersCollection.addUser(user);
    }

}
