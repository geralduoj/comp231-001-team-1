package com.comp231.easypark.psm;

import com.comp231.easypark.reservation.ParkingLot;

import java.util.List;

public class PSM {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<String> parkingLots;// each String contains the id of a ParkingLot

    public PSM() {}

    public boolean PasswordConfirmation(String input) {
        if (input.equals(this.password)) {
            return true;
        } else {
            return false;
        }
    }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setParkingLots(List<String> parkingLots) { this.parkingLots = parkingLots; }

    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public String getEmail() { return this.email; }
    public String getPassword() { return this.password; }
    public List<String> getParkingLots() { return this.parkingLots; }
    public void addParkingLot(String newPL) { parkingLots.add(newPL); }
}
