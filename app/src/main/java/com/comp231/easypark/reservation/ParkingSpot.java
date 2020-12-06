package com.comp231.easypark.reservation;

import java.io.Serializable;

public class ParkingSpot implements Serializable {
    private long id;
    private String status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
