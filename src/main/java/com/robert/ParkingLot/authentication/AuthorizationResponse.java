package com.robert.ParkingLot.authentication;

public class AuthorizationResponse {
    private String jwt;
    private String accountType;

    public AuthorizationResponse() {}

    public AuthorizationResponse(String jwt, String accountType) {
        this.jwt = jwt;
        this.accountType = accountType;
    }

    public String getJwt() { return jwt; }
    public void setJwt(String jwt) { this.jwt = jwt; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
}
