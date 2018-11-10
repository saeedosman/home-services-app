package com.example.aaron.groupprojectseg;

public class Service {
    private String name;
    private double rate;
    
    public Service() {

    }

    public Service(String name, double rate){
    this.name = name;
    this.rate = rate;
    }

    public String getName(){
        return name;
    }

    public void setName(String s){
        this.name = s;
    }

    public double getRate(){
        return rate;
    }

    public void setRate(double r){
        this.rate = r;
    }
}
