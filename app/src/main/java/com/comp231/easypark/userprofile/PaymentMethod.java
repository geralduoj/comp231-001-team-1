package com.comp231.easypark.userprofile;

import java.io.Serializable;

public class PaymentMethod implements Serializable {

    String paymentId;
    String type;
    String cardNumber;
    String holderName;
    String expirationDate;
    boolean isDefault;


    public PaymentMethod(String paymentId, String type, String cardNumber, String holderName, String expirationDate, boolean isDefault) {
        this.paymentId = paymentId;
        this.type = type;
        this.cardNumber = cardNumber;
        this.holderName = holderName;
        this.expirationDate = expirationDate;
        this.isDefault = isDefault;
    }
}
