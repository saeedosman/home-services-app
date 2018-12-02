package com.example.aaron.groupprojectseg;

public class Booking {
    private ServiceProvider serviceProvider;
    private String service;
    private AvailableTime time;
    private String username;

    public Booking() {

    }

    public Booking(ServiceProvider serviceProvider, String service, AvailableTime time, String username) {
        this.serviceProvider = serviceProvider;
        this.service = service;
        this.time = time;
        this.username = username;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public AvailableTime getTime() {
        return time;
    }

    public void setTime(AvailableTime time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
