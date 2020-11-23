package com.comp231.easypark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.comp231.easypark.map.ParkingLot;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Button btnNearestLot;
    private Button btnLow1HrFare;
    private Button btnLow6HrFare;
    private Button btnLow12HrFare;
    private Button btnLow24HrFare;
    private Button btnToggleTraffic;
    private SupportMapFragment mapFragment;

    private static final String TAG = "MapActivity";
    private static final float INIT_ZOOM = 14f;

    private GoogleMap gMap;
    private FusedLocationProviderClient locationClient;
    private Location myLoc;
    private MarkerOptions filterMarkerOptions;

    private FirebaseFirestore db;
    private List<ParkingLot> parkingLotList;
    private ParkingLot selectedParkingLot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        locationClient = LocationServices.getFusedLocationProviderClient(this);
        db = FirebaseFirestore.getInstance();
        parkingLotList = new ArrayList<>();
        getAllParkingLotsFromDb();
        initMapComponent();
        initControlComponent();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        trackMyLocation(gMap);
        showAllParkingLotsOnMap();
        selectParkingLotHandler();
    }

    private void initMapComponent(){
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initControlComponent(){

        btnNearestLot = findViewById(R.id.btnNearestLot);
        btnNearestLot.setOnClickListener(v->{
            myLoc = gMap.getMyLocation();
            getAllParkingLotsFromDb();
            Map<ParkingLot, Double> distances = new HashMap<>();
            for(ParkingLot p : parkingLotList){
                GeoPoint geoPoint = p.getLocation();
                double distance = Math.sqrt(
                        Math.pow(geoPoint.getLatitude() - myLoc.getLatitude(), 2) + Math.pow(geoPoint.getLongitude() - myLoc.getLongitude(), 2)
                );
                distances.put(p, distance);
            }
            ParkingLot nearest = Collections.min(distances.entrySet(), Map.Entry.comparingByValue()).getKey();
            Log.d(TAG,"get min: " + nearest.getName());
            setFilterMarkerOptions(nearest, String.format("Available/Total: %s/%s", nearest.getAvailableNumOfSpots(), nearest.getTotalNumOfSpots()));
        });

        btnLow1HrFare = findViewById(R.id.btnLow1HrFare);
        btnLow1HrFare.setOnClickListener(v->{
            setPriceFilterOptions("1hour", "Hour");
        });

        btnLow6HrFare = findViewById(R.id.btnLow6HrFare);
        btnLow6HrFare.setOnClickListener(v->{
            setPriceFilterOptions("6hour", "6 Hours");
        });

        btnLow12HrFare = findViewById(R.id.btnLow12HrFare);
        btnLow12HrFare.setOnClickListener(v->{
            setPriceFilterOptions("12hour", "12 Hours");
        });

        btnLow24HrFare = findViewById(R.id.btnLow24HrFare);
        btnLow24HrFare.setOnClickListener(v->{
            setPriceFilterOptions("24hour", "24 Hours");
        });

        btnToggleTraffic = findViewById(R.id.btnToggleTraffic);
        btnToggleTraffic.setOnClickListener(v->{
            if(gMap.isTrafficEnabled()){
                gMap.setTrafficEnabled(false);
            }else{
                gMap.setTrafficEnabled(true);
            }
        });
    }

    private void trackMyLocation(GoogleMap gMap){
        try{
            final Task getLocation = locationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null);
            getLocation.addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    myLoc = (Location) task.getResult();
                    Log.d(TAG,"Track myLoc: " + myLoc.toString());
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLoc.getLatitude(), myLoc.getLongitude()), INIT_ZOOM));
                    gMap.setMyLocationEnabled(true);
                }else{
                    Log.d(TAG, "Tracking My Location Task Failed.");
                }
            });
        }catch (SecurityException e){
            Log.d(TAG, "Security Exception: " + e.getMessage());
        }
    }

    private void getAllParkingLotsFromDb(){
        db.collection("ParkingLotDemo").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                parkingLotList.clear();
                for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                    parkingLotList.add(documentSnapshot.toObject(ParkingLot.class));
                }
                gMap.clear();
                onMapReady(gMap);
            }else{
                Log.e(TAG, "Get All Parking Lots Error: " + task.getException().toString());
            }
        });
    }

    private void showAllParkingLotsOnMap(){
        for(ParkingLot p : parkingLotList){
            LatLng lotLocation = new LatLng(p.getLocation().getLatitude(), p.getLocation().getLongitude());
            gMap.addMarker(new MarkerOptions()
                    .position(lotLocation).title(p.getName()).snippet(String.format("Available/Total: %s/%s", p.getAvailableNumOfSpots(), p.getTotalNumOfSpots()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            );
        }
        if(filterMarkerOptions!=null){
            Marker marker = gMap.addMarker(filterMarkerOptions);
            marker.showInfoWindow();
            filterMarkerOptions=null;
        }
    }

    private void selectParkingLotHandler(){
        gMap.setOnInfoWindowClickListener(marker -> {
            getAllParkingLotsFromDb();
            for(ParkingLot p : parkingLotList){
                if(p.getLocation().compareTo(new GeoPoint(marker.getPosition().latitude, marker.getPosition().longitude))==0){
                    selectedParkingLot = p;
                    break;
                }
            }
            Log.d(TAG, "Marker: " + marker.getPosition().toString());
            Log.d(TAG, "Location: " + selectedParkingLot.getLocation().toString());
            Bundle bundle = new Bundle();
            bundle.putString("docId", selectedParkingLot.getDocId());
            bundle.putString("lotName", selectedParkingLot.getName());
            Intent intent = new Intent(getApplicationContext(), MapPassActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void setFilterMarkerOptions(ParkingLot p, String snippet){
        LatLng lotLocation = new LatLng(p.getLocation().getLatitude(), p.getLocation().getLongitude());
        filterMarkerOptions = new MarkerOptions()
                .position(lotLocation).title(p.getName()).snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                );
    }

    private void setPriceFilterOptions(String key, String duration){
        getAllParkingLotsFromDb();
        Map<ParkingLot, Long> prices = new HashMap<>();
        for(ParkingLot p : parkingLotList){
            prices.put(p, p.getPrice().get(key));
        }
        ParkingLot lowest = Collections.min(prices.entrySet(), Map.Entry.comparingByValue()).getKey();
        Log.d(TAG,"get lowest: " + lowest.getPrice());
        NumberFormat currencyFmt = NumberFormat.getCurrencyInstance(Locale.CANADA);
        setFilterMarkerOptions(lowest, String.format("Rate: %s/%s", currencyFmt.format(lowest.getPrice().get(key)), duration));
    }
}