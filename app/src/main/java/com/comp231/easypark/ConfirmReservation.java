package com.comp231.easypark;

public class ConfirmReservation {




    private String parkingLotID;
    private long availableParkingSpot;
    private String parkingCost;

    public String getParkingLotID() {
        return parkingLotID;
    }

    public void setParkingLotID(String parkingLotID) {
        this.parkingLotID = parkingLotID;
    }

    public long getAvailableParkingSpot() {
        return availableParkingSpot;
    }

    public void setAvailableParkingSpot(long availableParkingSpot) {
        this.availableParkingSpot = availableParkingSpot;
    }

    public String getParkingCost() {
        return parkingCost;
    }

    public void setParkingCost(String parkingCost) {
        this.parkingCost = parkingCost;
    }
}
