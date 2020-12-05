package com.comp231.easypark.userprofile;

import java.io.Serializable;
import java.util.ArrayList;

public class Driver implements Serializable {

    String firstName;
    String lastName;
    String email;
    String password;
    ArrayList<PaymentMethod> paymentMethods;

    public Driver(String firstName, String lastName, String email, String password, ArrayList<PaymentMethod> paymentMethods) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.paymentMethods = paymentMethods;
    }
}
