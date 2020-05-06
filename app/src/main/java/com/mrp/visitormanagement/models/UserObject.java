package com.mrp.visitormanagement.models;

public class UserObject {
    private String fullName;
    private String place;

    public UserObject(String fullName, String place) {
        this.fullName = fullName;
        this.place = place;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPlace() {
        return place;
    }
}
