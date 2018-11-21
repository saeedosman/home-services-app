package com.example.aaron.groupprojectseg;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ServiceProviderUnitTesting {

    @Test
    public void test_getFirstName() {
        Account a = new Account("FirstN","LastN","EMail","UserN","!Q@W#E$R","User");
        assertEquals("Testing getFirstName",a.getFirstName(),"FirstN");
    }
    @Test
    public void test_getAddress() {
        ArrayList<String> al = new ArrayList<String>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al);
        assertEquals("Testing getAddress",sp.getAddress(),"Fake Street");
    }
    @Test
    public void test_setAddress() {
        ArrayList<String> al = new ArrayList<String>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al);
        sp.setAddress("New Address");
        assertNotEquals("Testing setAddress, is not the old one",sp.getAddress(),"Fake Street");
        assertEquals("Testing setAddress, is the new one",sp.getAddress(),"New Address");
    }
    @Test
    public void test_getPhoneNumber() {
        ArrayList<String> al = new ArrayList<String>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al);
        assertEquals("Testing getPhoneNumber",sp.getPhoneNumber(),"8197727283");
    }
    @Test
    public void test_setPhoneNumber() {
        ArrayList<String> al = new ArrayList<String>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al);
        sp.setPhoneNumber("1234567890");
        assertNotEquals("Testing setPhoneNumber, is not the old name",sp.getPhoneNumber(),"8197727283");
        assertEquals("Testing setPhoneNumber, is the new number",sp.getPhoneNumber(),"1234567890");
    }
    @Test
    public void test_getName() {
        ArrayList<String> al = new ArrayList<String>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al);
        assertEquals("Testing getName",sp.getName(),"Jon Doe");
    }
    @Test
    public void test_setName() {
        ArrayList<String> al = new ArrayList<String>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al);
        sp.setName("New Name");
        assertNotEquals("Testing setName, is not the old name",sp.getName(),"Jon Doe");
        assertEquals("Testing setName, is the new name",sp.getName(),"New Name");
    }
    @Test
    public void test_getDescription() {
        ArrayList<String> al = new ArrayList<String>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al);
        assertEquals("Testing getDescription",sp.getDescription(),"N/A");
    }
    @Test
    public void test_setDescription() {
        ArrayList<String> al = new ArrayList<String>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al);
        sp.setDescription("I changed my description");
        assertNotEquals("Testing setDescription, is not the old description",sp.getDescription(),"N/A");
        assertEquals("Testing setDescription, is the new description",sp.getDescription(),"I changed my description");
    }
    @Test
    public void test_isLicensed() {
        ArrayList<String> al = new ArrayList<String>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al);
        assertEquals("Testing isLicensed",sp.isLicensed(),true);
    }
    @Test
    public void test_setLicensed() {
        ArrayList<String> al = new ArrayList<String>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al);
        sp.setLicensed(false);
        assertNotEquals("Testing setLicensed, is not the old licensed",sp.isLicensed(),true);
        assertEquals("Testing setLicensed, is the new licensed",sp.isLicensed(),false);
    }
    @Test
    public void test_getUsername() {
        ArrayList<String> al = new ArrayList<String>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al);
        assertEquals("Testing getUsername",sp.getUsername(),"JD");
    }
    @Test
    public void test_setUsername() {
        ArrayList<String> al = new ArrayList<String>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al);
        sp.setUsername("New Username");
        assertNotEquals("Testing setUsername, is not the old username",sp.getUsername(),"JD");
        assertEquals("Testing setLicensed, is the new username",sp.getUsername(),"New Username");
    }
    @Test
    public void test_getServices() {
        ArrayList<String> al = new ArrayList<String>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al);
        assertEquals("Testing getServices",sp.getServices(),al);
    }
    @Test
    public void test_setServices() {
        ArrayList<String> al = new ArrayList<String>();
        al.add("test");
        ArrayList<String> al2 = new ArrayList<String>();
        ServiceProvider sp = new ServiceProvider("Fake Street", "8197727283", "Jon Doe", "N/A", true, "JD", al);
        sp.setServices(al2);
        assertNotEquals("Testing setServices, is not the old services",sp.getServices(),al);
        assertEquals("Testing setServices, is the new services",sp.getServices(),al2);
    }
}
