package com.example.aaron.groupprojectseg;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidationHelperUnitTesting {


    @Test
    public void test_validateUsername(){
        ValidationHelper validHelp = new ValidationHelper();
        assertNull("Validating a good username",validHelp.validateUsername("ValidUserName"));
        assertNotNull("testing when username is too short",validHelp.validateUsername("s"));
        assertNotNull("testing username with special characters",validHelp.validateUsername(".*"));
    }

    @Test
    public void test_validateFirstName(){
        ValidationHelper validHelp = new ValidationHelper();
        assertNull("Testing Valid first name",validHelp.validateFirstName("Jon"));
    }

    @Test
    public void test_validateLastName(){
        ValidationHelper validHelp = new ValidationHelper();
        assertNull("Testing Valid first name",validHelp.validateLastName("Doe"));
    }

    @Test
    public void test_validatePassword(){
        ValidationHelper validHelp = new ValidationHelper();
        assertNull("Testing valid password", validHelp.validatePassword("!Q@W#E$R"));
        assertNotNull("Testing Invalid password", validHelp.validatePassword("12"));
    }
    
}
