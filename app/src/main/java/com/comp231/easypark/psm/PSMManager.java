package com.comp231.easypark.psm;

import com.comp231.easypark.reservation.ParkingLot;

/*
    Static class to keep the details of the logged-in PSM in memory
 */
public class PSMManager {

    private static PSM psm;
    private static ParkingLot currentParkingLot; // the ParkingLot currently being edited

    public static PSM getPSM() {
        return psm;
    }
    public static ParkingLot getCurrentParkingLot() { return currentParkingLot; }

    public static void setPSM(PSM newPsm) {
        psm = newPsm;
    }
    public static void setCurrentParkingLot(ParkingLot newPL) { currentParkingLot = newPL; }
}
