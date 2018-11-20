package com.example.aaron.groupprojectseg;

import java.util.ArrayList;

public class ServiceProvider {
    private String address;
    private String phoneNumber;
    private String name;
    private String description;
    private boolean licensed;
    private String username;
    private ArrayList<String> services;

    public ServiceProvider() {
    }

    public ServiceProvider(String address, String phoneNumber, String name, String description, boolean licensed, String username, ArrayList<String> services) {
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.description = description;
        this.licensed = licensed;
        this.username = username;
        this.services = services;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isLicensed() {
        return licensed;
    }

    public void setLicensed(boolean licensed) {
        this.licensed = licensed;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getServices() {
        return services;
    }

    public void setServices(ArrayList<String> services) {
        this.services = services;
    }
}
