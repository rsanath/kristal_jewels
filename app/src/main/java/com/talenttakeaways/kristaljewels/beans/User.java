package com.talenttakeaways.kristaljewels.beans;

/**
 * Created by sanath on 09/06/17.
 */

public class User {
    String name, email, password, number;

    public User(String name, String email, String password, String number) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.number = number;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "Name : "+name+
                "Email : "+email+
                "Password : "+password+
                "Number : "+number;
    }
}
