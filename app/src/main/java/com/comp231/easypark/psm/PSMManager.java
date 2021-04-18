package com.comp231.easypark.psm;

import android.location.Location;

import com.comp231.easypark.reservation.ParkingLot;
import com.google.firebase.firestore.GeoPoint;

/*
    Static class to keep the details of the logged-in PSM in memory
 */
public class PSMManager {

    private static PSM psm;
    private static ParkingLot currentParkingLot; // the ParkingLot currently being edited
    private static GeoPoint locationRef; // used for switching between MapSelectionActivity and other activities

    public static PSM getPSM() {
        return psm;
    }
    public static ParkingLot getCurrentParkingLot() { return currentParkingLot; }
    public static GeoPoint getLocationRef() { return locationRef; }

    public static void setPSM(PSM newPsm) {
        psm = newPsm;
    }
    public static void setCurrentParkingLot(ParkingLot newPL) { currentParkingLot = newPL; }
    public static void setLocationRef(double lat, double lon) {
        locationRef = new GeoPoint(lat, lon);
    }
}
