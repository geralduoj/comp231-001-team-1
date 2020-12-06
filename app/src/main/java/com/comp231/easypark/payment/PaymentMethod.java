package com.comp231.easypark.payment;

import java.io.Serializable;

public class PaymentMethod implements Serializable {

    private  String paymentId;
    private String type;
    private String cardNumber;
    private String holderName;
    private String expirationDate;
    private boolean isDefault;

//
//    public PaymentMethod(String paymentId, String type, String cardNumber, String holderName, String expirationDate, boolean isDefault) {
//        this.paymentId = paymentId;
//        this.type = type;
//        this.cardNumber = cardNumber;
//        this.holderName = holderName;
//        this.expirationDate = expirationDate;
//        this.isDefault = isDefault;
//    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
