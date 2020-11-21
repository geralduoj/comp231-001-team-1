package com.comp231.easypark.userprofile;

import java.util.ArrayList;

public class Driver {

    String userId;
    String fullName;
    String email;
    String password;
    ArrayList<PaymentMethod> paymentMethods;

    public Driver(String userId, String fullName, String email, String password, ArrayList<PaymentMethod> paymentMethods) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.paymentMethods = paymentMethods;
    }
}
