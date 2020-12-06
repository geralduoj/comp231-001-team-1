package com.comp231.easypark.reservation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.comp231.easypark.MainActivity;
import com.comp231.easypark.OptionsMenuActivity;
import com.comp231.easypark.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookingActivity extends OptionsMenuActivity implements AdapterView.OnItemSelectedListener {

    //public String retrievedParkingLotID = "HBsJKENagjA6FHwSJUJh";
    private String seletectItem;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private DocumentReference resDocRef = db.collection("ParkingLotDemo").document(retrievedParkingLotID);
    private DocumentReference resDocRef;
    public static ConfirmReservation newReservation;
    public String [] parkingCost = {"12hour - $20","1hour - $4", "24hour - $30", "6hour - $12"};
    String retrievedParkingLotID;
    String retrievedParkingLotName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        retrievedParkingLotID = bundle.getString("docId");
        retrievedParkingLotName = bundle.getString("lotName");
        resDocRef = db.collection("ParkingLotDemo").document(retrievedParkingLotID);

        TextView parkingLotField = (TextView) findViewById(R.id.parkingLotField);
        TextView spotNumberField = (TextView) findViewById(R.id.spotNumberField);
        Spinner spinnerDuration = (Spinner) findViewById(R.id.reservationTimeListSP);
        newReservation = new ConfirmReservation();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, parkingCost);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDuration.setAdapter(adapter);
        spinnerDuration.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        parkingLotField.setText(retrievedParkingLotName);

        resDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    ParkingLot parkingLot = documentSnapshot.toObject(ParkingLot.class);
                    ParkingSpot sp = parkingLot.getAvailableSpot();
                    spotNumberField.setText(String.valueOf(sp.getId()));
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
        Bundle bundle = new Bundle();
        bundle.putString("retrievedParkingLotID", retrievedParkingLotID);
        bundle.putString("retrievedParkingLotName", retrievedParkingLotName);
        Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void cancelBooking(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}