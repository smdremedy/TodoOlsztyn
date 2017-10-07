package com.soldiersofmobile.todoexpert.api;

public class LoginResponse {

    public String sessionToken;
    public String objectId;

    @Override
    public String toString() {
        return "LoginResponse{" +
                "sessionToken='" + sessionToken + '\'' +
                ", objectId='" + objectId + '\'' +
                '}';
    }
}
