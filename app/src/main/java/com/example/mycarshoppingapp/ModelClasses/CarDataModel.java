package com.example.mycarshoppingapp.ModelClasses;


import java.io.Serializable;

public class CarDataModel implements Serializable {
    private String model;
    private double engineCapacity;
    private String bodyType;
    private String registeredIn;
    private String color;
    private String imageUrl;
    private String price;

    private String phoneno;

    private String Id_of_user;

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public CarDataModel() {
        // Default constructor required for Firebase database operations
    }


    public String getId_of_user() {
        return Id_of_user;
    }

    public void setId_of_user(String id_of_user) {
        Id_of_user = id_of_user;
    }

    public CarDataModel(String model, double engineCapacity, String bodyType, String registeredIn, String color, String imageUrl, String price, String phoneno, String Id_of_user) {
        this.model = model;
        this.engineCapacity = engineCapacity;
        this.bodyType = bodyType;
        this.registeredIn = registeredIn;
        this.color = color;
        this.imageUrl = imageUrl;
        this.price = price;
        this.phoneno = phoneno;
        this.Id_of_user = Id_of_user;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(double engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getRegisteredIn() {
        return registeredIn;
    }

    public void setRegisteredIn(String registeredIn) {
        this.registeredIn = registeredIn;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String toString() {
        return "Model: " + model + "\n" +
                "Engine Capacity: " + engineCapacity + "\n" +
                "Body Type: " + bodyType + "\n" +
                "Registered In: " + registeredIn + "\n" +
                "Color: " + color + "\n" +
                "Price: " + price + "\n" +
                "Phone Number: " + phoneno + "\n" ;
    }
}
