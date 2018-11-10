package com.example.aaron.groupprojectseg;

public class Service {
    private String service;
    private double hourlyRate;

    public Service(String service, double rate){
    this.service = service;
    this.hourlyRate = rate;
    }

    public String getService(){
        return service;
    }

    public void setService(String s){
        this.service = s;
    }

    public double getHourlyRate(){
        return hourlyRate;
    }

    public void setHourlyRate(double r){
        this.hourlyRate = r;
    }
}
