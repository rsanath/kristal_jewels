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

    public User() {
    }
}
