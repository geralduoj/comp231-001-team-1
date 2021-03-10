package com.comp231.easypark.psm;

/*
    Static class to keep the details of the logged-in PSM in memory
 */
public class PSMManager {

    private static PSM psm;

    public static PSM getPSM() {
        return psm;
    }

    public static void setPSM(PSM newPsm) {
        psm = newPsm;
    }
}
