package com.example.aaron.groupprojectseg;

import org.junit.Test;
import static org.junit.Assert.*;

public class AvailableTimeUnitTesting {
    //public AvailableTime(String day, String startTime, String endTime) {

    @Test
    public void test_getDay(){
        AvailableTime at = new AvailableTime("monday","11:30","5:00");

        assertEquals("monday",at.getDay());
    }

    @Test
    public void test_setDay() {
        AvailableTime at = new AvailableTime("monday","11:30","5:00");

        at.setDay("Wednesday");
        assertEquals("Wednesday",at.getDay());
        assertNotEquals("monday",at.getDay());
    }

    @Test
    public void test_getStartTime() {
        AvailableTime at = new AvailableTime("monday","11:30","5:00");

        assertEquals("11:30",at.getStartTime());
    }

    @Test
    public void test_setStartTime() {
        AvailableTime at = new AvailableTime("monday","11:30","5:00");

        at.setStartTime("10:00");

        assertEquals("10:00", at.getStartTime());
        assertNotEquals("11:30", at.getStartTime());
    }

    @Test
    public void test_getEndTime() {
        AvailableTime at = new AvailableTime("monday","11:30","5:00");

        assertEquals("5:00",at.getEndTime());
    }

    @Test
    public void test_setEndTime() {
        AvailableTime at = new AvailableTime("monday","11:30","5:00");

        at.setEndTime("4:00");

        assertEquals("4:00",at.getEndTime());
        assertNotEquals("5:00", at.getEndTime());
    }

}
