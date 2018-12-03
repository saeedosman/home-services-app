package com.example.aaron.groupprojectseg;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RatingUnitTesting {

    //Rating(ServiceProvider serviceProvider, String username, float rating, String comment)

    @Test
    public void test_getServiceProvider() {
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<AvailableTime> at = new ArrayList<>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al, at);
        Rating rating = new Rating(sp,"test",1,"test");

        assertEquals(sp,rating.getServiceProvider());
    }

    @Test
    public void test_setServiceProvider() {
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<AvailableTime> at = new ArrayList<>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al, at);
        Rating rating = new Rating(sp,"test",1,"test");

        ServiceProvider sp2 = new ServiceProvider("test", "test", "test", "test", true, "test", al, at);
        rating.setServiceProvider(sp2);

        assertEquals(sp2,rating.getServiceProvider());
    }

    @Test
    public void test_getUsername() {
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<AvailableTime> at = new ArrayList<>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al, at);
        Rating rating = new Rating(sp,"test",1,"test");

        assertEquals("test",rating.getUsername());
    }

    @Test
    public void test_setUsername() {
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<AvailableTime> at = new ArrayList<>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al, at);
        Rating rating = new Rating(sp,"test",1,"test");

        rating.setUsername("Newtest");

        assertEquals("Newtest",rating.getUsername());
    }

    @Test
    public void test_getRating() {
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<AvailableTime> at = new ArrayList<>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al, at);
        Rating rating = new Rating(sp,"test",1,"test");

        assertTrue(1==rating.getRating());
    }

    @Test
    public void test_setRating() {
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<AvailableTime> at = new ArrayList<>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al, at);
        Rating rating = new Rating(sp,"test",1,"test");

        rating.setRating(2);

        assertTrue(2==rating.getRating());
    }

    @Test
    public void test_getComment() {
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<AvailableTime> at = new ArrayList<>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al, at);
        Rating rating = new Rating(sp,"test",1,"test");

        assertEquals("test",rating.getComment());
    }

    @Test
    public void test_setComment() {
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<AvailableTime> at = new ArrayList<>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al, at);
        Rating rating = new Rating(sp,"test",1,"test");

        rating.setComment("NewTest");

        assertEquals("NewTest", rating.getComment());
    }
}
