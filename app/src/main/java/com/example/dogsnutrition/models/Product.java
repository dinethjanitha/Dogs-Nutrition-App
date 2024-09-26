package com.example.dogsnutrition.models;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String name;
    private String description;
    private String price;
    private String brand;
    private String type;
    private String age;
    private String imageUri;

    // Constructor, getters, and setters

    public Product(int id, String name, String description, String price, String brand, String type, String age, String imageUri) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.brand = brand;
        this.type = type;
        this.age = age;
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getBrand() {
        return brand;
    }

    public String getType() {
        return type;
    }

    public String getAge() {
        return age;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }



    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
