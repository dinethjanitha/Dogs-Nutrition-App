package com.example.dogsnutrition.models;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String email;
    private String name;
    private String address;
    private boolean isAdmin;

    public User(int id, String email, String name, String address, boolean isAdmin) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.address = address;
        this.isAdmin = isAdmin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
