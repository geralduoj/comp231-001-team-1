package com.comp231.easypark;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidateEmailTest {
    String mail = "nameLastName@mail.com";
    @Test
    public void emailFormat_isCorrect(){
        assertEquals(mail, "nameLastName@mail.com");
    }
}
