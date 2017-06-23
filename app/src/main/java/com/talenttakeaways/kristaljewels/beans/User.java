package com.talenttakeaways.kristaljewels.beans;

/**
 * Created by sanath on 09/06/17.
 */

public class User {
    public String name, email, password, number, isAdmin;

    public User(String name, String email, String password, String number, String isAdmin) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.number = number;
        this.isAdmin = isAdmin;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public User() {
    }
}
