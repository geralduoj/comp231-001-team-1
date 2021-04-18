package com.comp231.easypark;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import static org.junit.Assert.*;

public class LocationCoordinatesTest {

    double latitudeMaxValue = 90;
    double latitudeMinValue = -90;
    double longitudeMaxValue = 180;
    double longitudeMinValue = -180;

    @Test
    public void areLocationCoordinatesCorrect(GeoPoint point) {
        assertTrue("Latitude is lower than the min value", point.getLatitude() > latitudeMinValue);
        assertTrue("Latitude is higher than the max value", point.getLatitude() < latitudeMaxValue);
        assertTrue("Longitude is lower than the min value", point.getLongitude() > longitudeMinValue);
        assertTrue("Longitude is higher than the max value", point.getLongitude() < longitudeMaxValue);
    }
}
