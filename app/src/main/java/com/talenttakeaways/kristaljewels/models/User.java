package com.talenttakeaways.kristaljewels.models;

import java.util.ArrayList;

/**
 * Created by sanath on 09/06/17.
 */

public class User {
    private String name, email, number, userId;
    private boolean admin;
    private ArrayList<ShippingDetail> shippingDetails;

    public User(String userId, String name, String email, String number, boolean admin) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.number = number;
        this.admin = admin;
    }

    public ArrayList<ShippingDetail> getShippingDetails() {
        return shippingDetails;
    }

    public void setShippingDetails(ArrayList<ShippingDetail> shippingDetails) {
        this.shippingDetails = shippingDetails;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", number='" + number + '\'' +
                ", userId='" + userId + '\'' +
                ", admin=" + admin +
                ", shippingDetails=" + shippingDetails +
                '}';
    }
}
