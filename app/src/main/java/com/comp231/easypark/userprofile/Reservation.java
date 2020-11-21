package com.comp231.easypark.userprofile;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Reservation implements Serializable {

    Timestamp reserveTime;
    String parkingLotId;
    String userId;
    String parkingSpotId;
    double cost;

    public Timestamp getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(Timestamp reserveTime) {
        this.reserveTime = reserveTime;
    }

    public String getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(String parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParkingSpotId() {
        return parkingSpotId;
    }

    public void setParkingSpotId(String parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Reservation(Timestamp reserveTime, String parkingLotId, String userId, String parkingSpotId, double cost) {
        this.reserveTime = reserveTime;
        this.parkingLotId = parkingLotId;
        this.userId = userId;
        this.parkingSpotId = parkingSpotId;
        this.cost = cost;
    }
}
