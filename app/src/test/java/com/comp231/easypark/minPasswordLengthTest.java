package com.comp231.easypark;

import org.junit.Test;
import static org.junit.Assert.*;

public class minPasswordLengthTest {
    int minLength = 4;
    String password = "pass";
    @Test
    public void minPasswordLength() {
        assertEquals(minLength, password.length());
    }
}
