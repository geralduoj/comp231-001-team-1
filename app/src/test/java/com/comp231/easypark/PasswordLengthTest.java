package com.comp231.easypark;

import org.junit.Test;
import static org.junit.Assert.*;

public class PasswordLengthTest {
    String test = "password";
    @Test
    public void passwordLength_Test(){
        assertEquals(8, test.length());
        assert test.length() == 8;
    }
}
