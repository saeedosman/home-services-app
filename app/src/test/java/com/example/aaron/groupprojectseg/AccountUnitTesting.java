package com.example.aaron.groupprojectseg;

import org.junit.Test;
import static org.junit.Assert.*;

public class AccountUnitTesting {
    //public Account( String firstName, String lastName, String email, String username, String password, String type)

    @Test
    public void test_getFirstName() {
        Account a = new Account("FirstN","LastN","EMail","UserN","!Q@W#E$R","User");
        assertEquals("Testing getFirstName",a.getFirstName(),"FirstN");
    }

    @Test
    public void test_setFirstName() {
        Account a = new Account("FirstN","LastN","EMail","UserN","!Q@W#E$R","User");
        a.setFirstName("NewFirstName");
        assertNotEquals("Testing name is changed after setFirstName",a.getFirstName(),"FirstN");
        assertEquals("Testing name is changed after setFirstName",a.getFirstName(),"NewFirstName");

    }

    @Test
    public void test_getLastName() {
        Account a = new Account("FirstN","LastN","EMail","UserN","!Q@W#E$R","User");
        assertEquals("Testing getLastName",a.getLastName(),"LastN");
    }

    @Test
    public void test_setLastName() {
        Account a = new Account("FirstN","LastN","EMail","UserN","!Q@W#E$R","User");
        a.setLastName("NewLastName");
        assertNotEquals("Testing name is changed after setLastName",a.getLastName(),"LastN");
        assertEquals("Testing name is changed after setLastName",a.getLastName(),"NewLastName");
    }

    @Test
    public void test_getEmailName() {
        Account a = new Account("FirstN","LastN","EMail","UserN","!Q@W#E$R","User");
        assertEquals("Testing getEmail",a.getEmail(),"EMail");
    }

    @Test
    public void test_setEmailName() {
        Account a = new Account("FirstN","LastN","EMail","UserN","!Q@W#E$R","User");
        a.setEmail("NewEmail");
        assertNotEquals("Testing email is changed after setEmail",a.getEmail(),"EMail");
        assertEquals("Testing email is changed after setEmail",a.getEmail(),"NewEmail");
    }

    @Test
    public void test_getUserName() {
        Account a = new Account("FirstN","LastN","EMail","UserN","!Q@W#E$R","User");
        assertEquals("Testing getUserName",a.getUsername(),"UserN");
    }

    @Test
    public void test_setUserName() {
        Account a = new Account("FirstN","LastN","EMail","UserN","!Q@W#E$R","User");
        a.setUsername("NewUserN");
        assertNotEquals("Testing username is changed after setEmail",a.getUsername(),"UserN");
        assertEquals("Testing username is correctly changed",a.getUsername(),"NewUserN");
    }

    @Test
    public void test_getPassword() {
        Account a = new Account("FirstN","LastN","EMail","UserN","!Q@W#E$R","User");
        assertEquals("Testing getPassword",a.getPassword(),"!Q@W#E$R");
    }

    @Test
    public void test_setPassword() {
        Account a = new Account("FirstN","LastN","EMail","UserN","!Q@W#E$R","User");
        a.setPassword("1q2w3e4r");
        assertNotEquals("Testing password is changed after setPassword",a.getPassword(),"!Q@W#E$R");
        assertEquals("Testing password is correctly changed",a.getPassword(),"1q2w3e4r");
    }

    @Test
    public void test_getType() {
        Account a = new Account("FirstN","LastN","EMail","UserN","!Q@W#E$R","User");
        assertEquals("Testing getType",a.getType(),"User");
    }

    @Test
    public void test_setType() {
        Account a = new Account("FirstN","LastN","EMail","UserN","!Q@W#E$R","User");
        a.setType("ServiceProvider");
        assertNotEquals("Testing type is changed after setType",a.getType(),"User");
        assertEquals("Testing type is correctly changed",a.getType(),"ServiceProvider");
    }
}