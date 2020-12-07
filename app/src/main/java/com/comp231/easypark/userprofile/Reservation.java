package com.comp231.easypark.userprofile;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Reservation implements Serializable {

    Timestamp reserveTime;
    String parkingLotId;
    String userId;
    long parkingSpotId;
    long cost;

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

    public long getParkingSpotId() {
        return parkingSpotId;
    }

    public void setParkingSpotId(long parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public Reservation(Timestamp reserveTime, String parkingLotId, String userId, long parkingSpotId, long cost) {
        this.reserveTime = reserveTime;
        this.parkingLotId = parkingLotId;
        this.userId = userId;
        this.parkingSpotId = parkingSpotId;
        this.cost = cost;
    }

}
