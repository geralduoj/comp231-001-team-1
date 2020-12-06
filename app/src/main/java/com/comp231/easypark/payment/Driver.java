package com.comp231.easypark.payment;

import java.io.Serializable;
import java.util.ArrayList;

public class Driver implements Serializable {

    String firstName;
    String lastName;
    String email;
    String password;
    ArrayList<PaymentMethod> paymentMethods;

    public Driver() {
    }

    public String getDefaultPaymentMethod() {
        String defaultCardNumber = "";

        for (PaymentMethod p : paymentMethods) {
            if (p.isDefault()) {
                defaultCardNumber = p.getCardNumber();
                break;
            }
        }
        return defaultCardNumber;
    }
}
