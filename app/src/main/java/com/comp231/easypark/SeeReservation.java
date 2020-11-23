package com.comp231.easypark;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.stream.Collectors;

public class SeeReservation extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public String retrievedParkingLotID = "HBsJKENagjA6FHwSJUJh";
    private String seletectItem;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference resDocRef = db.collection("ParkingLot").document(retrievedParkingLotID);

    public static ConfirmReservation newReservation;
    public String [] parkingCost = {"12hour - $20","1hour - $4", "24hour - $30", "6hour - $12"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_reservation);
        TextView parkingLotIDTXT = (TextView) findViewById(R.id.parkingLotIDTXT);
        TextView availableParkingTXT = (TextView) findViewById(R.id.availableParkingTXT);
        Spinner spinnerDuration = (Spinner) findViewById(R.id.reservationTimeListSP);
        newReservation = new ConfirmReservation();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, parkingCost);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDuration.setAdapter(adapter);
        spinnerDuration.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        parkingLotIDTXT.setText(retrievedParkingLotID);

        resDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    ParkingLot parkingLot = documentSnapshot.toObject(ParkingLot.class);
                    ParkingSpot sp = parkingLot.getAvailableSpot();
                    availableParkingTXT.setText(String.valueOf(sp.getId()));


                    newReservation.setParkingLotID(retrievedParkingLotID);
                    newReservation.setAvailableParkingSpot(sp.getId());





                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
        //seletectItem = parent.getItemAtPosition(position).toString();
        seletectItem = parkingCost[position];
        newReservation.setParkingCost(seletectItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void confirmBooking(View view)
    {
        Intent intent = new Intent(this, CompleteReservation.class);
        startActivity(intent);
    }
    public void cancelBooking(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}