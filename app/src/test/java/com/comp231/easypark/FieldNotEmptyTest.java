package com.comp231.easypark;

import org.junit.Test;
import static org.junit.Assert.*;

public class FieldNotEmptyTest {
    String field = "field";
    @Test
    public void emptyFieldsTest(){
        assertNotEquals("", field);
    }
}
