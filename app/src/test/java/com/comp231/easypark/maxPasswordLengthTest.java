package com.comp231.easypark;
import org.junit.Test;
import static org.junit.Assert.*;

public class maxPasswordLengthTest {
    int maxLength = 12;
    String password = "password1212";
    @Test
    public void minPasswordLength() {
        assertEquals(maxLength, password.length());
    }
}
