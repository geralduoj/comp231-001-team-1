package com.comp231.easypark.psm;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comp231.easypark.R;
import com.comp231.easypark.RegisterPage;
import com.comp231.easypark.reservation.ParkingLot;
import com.comp231.easypark.reservation.ParkingSpot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateParkingLotActivity extends AppCompatActivity {

    private EditText txtName, txtDescription, txtLatitude, txtLongitude, txtNumSpots;
    private Button btnCreate, btnCancel, btnUseMap;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_parking_lot);

        db = FirebaseFirestore.getInstance();

        txtName = (EditText)findViewById(R.id.createParkingLot_Name);
        txtDescription = (EditText)findViewById(R.id.createParkingLot_Description);
        txtLatitude = (EditText)findViewById(R.id.createParkingLot_Latitude);
        txtLongitude = (EditText)findViewById(R.id.createParkingLot_Longitude);
        txtNumSpots = (EditText)findViewById(R.id.createParkingLot_SpotNum);

        btnCreate = (Button)findViewById(R.id.createParkingLot_CreateBtn);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new ParkingLot
                ParkingLot newParkingLot = new ParkingLot();
                newParkingLot.setName(txtName.getText().toString());
                newParkingLot.setDescription(txtDescription.getText().toString());
                double latitude = Double.parseDouble(txtLatitude.getText().toString());
                double longitude = Double.parseDouble(txtLongitude.getText().toString());
                newParkingLot.setLocation(new GeoPoint(latitude, longitude));
                // Price not handled properly yet, need to implement
                Map<String, Long> price = new HashMap<String, Long>();
                price.put("12hour", (long)22);
                price.put("1hour", (long)123);
                price.put("24hour", (long)30);
                price.put("6hour", (long)11);
                newParkingLot.setPrice(price);
                int numSpots = Integer.parseInt(txtNumSpots.getText().toString());
                List<ParkingSpot> spots = new ArrayList<>();
                for (int i = 0; i < numSpots; i++) {
                    spots.add(new ParkingSpot(i + 1, "free"));// Assuming that all spots should be free at the beginning
                }
                newParkingLot.setSpots(spots);
                newParkingLot.setDocId(db.collection("ParkingLotDemo").document().getId());// get id of new entry
                // Send to database
                DocumentReference docRef = db.collection("ParkingLotDemo").document(newParkingLot.getDocId());
                Task insert = docRef.set(newParkingLot);
                insert.addOnSuccessListener(o -> {
                    Log.d("TAG", "New parking lot added!");
                    // Also update PSM data
                    PSM newData = PSMManager.getPSM();
                    newData.addParkingLot(newParkingLot.getDocId());
                    PSMManager.setPSM(newData);
                    DocumentReference sfDocRef = db.collection("PSM").document(PSMManager.getPSM().getEmail());
                    db.runTransaction(new Transaction.Function<Void>() {
                        @Override
                        public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                            DocumentSnapshot snapshot = transaction.get(sfDocRef);
                            transaction.update(sfDocRef, "parkingLots", newData.getParkingLots());
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(CreateParkingLotActivity.this, "New parking lot registered", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                });
                insert.addOnFailureListener(o->{
                    Log.e("TAG", "Failed to add new parking lot.");
                    Toast.makeText(CreateParkingLotActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                });
            }
        });

        btnCancel = (Button)findViewById(R.id.createParkingLot_CancelBtn);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUseMap = (Button)findViewById(R.id.createParkingLot_MapBtn);
        btnUseMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapSelectionActivity.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Put in values set for the location
        GeoPoint selectedPoint = PSMManager.getLocationRef();
        if (selectedPoint != null) {
            txtLatitude.setText(Double.toString(selectedPoint.getLatitude()));
            txtLongitude.setText(Double.toString(selectedPoint.getLongitude()));
        }
    }
}