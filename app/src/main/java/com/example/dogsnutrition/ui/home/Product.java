package com.example.dogsnutrition.ui.home;

public class Product {
    private String name;
    private int members;
    private int imageResId;

    public Product(String name, int members, int imageResId) {
        this.name = name;
        this.members = members;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getMembers() {
        return members;
    }

    public int getImageResId() {
        return imageResId;
    }
}
