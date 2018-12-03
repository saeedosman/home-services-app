package com.example.aaron.groupprojectseg;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BookingUnitTesting {

    //Booking(ServiceProvider serviceProvider, String service, AvailableTime time, String username) {

    @Test
    public void test_getServiceProvider(){
        AvailableTime at = new AvailableTime("monday", "11:30", "4:00");
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<AvailableTime> a2 = new ArrayList<>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al, a2);
        Booking book = new Booking(sp,"test", at, "test");

        assertSame(sp,book.getServiceProvider());
    }

    @Test
    public void test_setServiceProvider(){
        AvailableTime at = new AvailableTime("monday", "11:30", "4:00");
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<AvailableTime> a2 = new ArrayList<>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al, a2);
        Booking book = new Booking(sp,"test", at, "test");

        ServiceProvider newSp = new ServiceProvider("test", "test", "test", "test", true, "test", al, a2);

        book.setServiceProvider(newSp);

        assertSame(newSp,book.getServiceProvider());
        assertNotSame(sp,book.getServiceProvider());
    }

    @Test
    public void test_getService(){
        AvailableTime at = new AvailableTime("monday", "11:30", "4:00");
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<AvailableTime> a2 = new ArrayList<>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al, a2);
        Booking book = new Booking(sp,"test", at, "test");

        assertEquals("test",book.getService());
    }

    @Test
    public void test_setService(){
        AvailableTime at = new AvailableTime("monday", "11:30", "4:00");
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<AvailableTime> a2 = new ArrayList<>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al, a2);
        Booking book = new Booking(sp,"test", at, "test");

        book.setService("newService");

        assertEquals("newService", book.getService());
        assertNotEquals("test",book.getService());
    }

    @Test
    public void test_getTime(){
        AvailableTime at = new AvailableTime("monday", "11:30", "4:00");
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<AvailableTime> a2 = new ArrayList<>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al, a2);
        Booking book = new Booking(sp,"test", at, "test");
    }

    @Test
    public void test_setTime(){
        AvailableTime at = new AvailableTime("monday", "11:30", "4:00");
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<AvailableTime> a2 = new ArrayList<>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al, a2);
        Booking book = new Booking(sp,"test", at, "test");
    }

    @Test
    public void test_getUsername(){
        AvailableTime at = new AvailableTime("monday", "11:30", "4:00");
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<AvailableTime> a2 = new ArrayList<>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al, a2);
        Booking book = new Booking(sp,"test", at, "test");
    }

    @Test
    public void test_setUsername(){
        AvailableTime at = new AvailableTime("monday", "11:30", "4:00");
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<AvailableTime> a2 = new ArrayList<>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al, a2);
        Booking book = new Booking(sp,"test", at, "test");
    }
}
