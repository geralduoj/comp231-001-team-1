package com.comp231.easypark.psm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comp231.easypark.R;
import com.comp231.easypark.reservation.ParkingLot;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Transaction;

public class EditParkingLotActivity extends AppCompatActivity {

    private EditText txtName, txtDescription, txtLatitude, txtLongitude;
    private Button btnUpdate, btnCancel;

    private FirebaseFirestore db;
    ParkingLot parkingLot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_parking_lot);

        db = FirebaseFirestore.getInstance();
        parkingLot = PSMManager.getCurrentParkingLot();

        txtName = (EditText)findViewById(R.id.editParkingLot_Name);
        txtDescription = (EditText)findViewById(R.id.editParkingLot_Description);
        txtLatitude = (EditText)findViewById(R.id.editParkingLot_Latitude);
        txtLongitude = (EditText)findViewById(R.id.editParkingLot_Longitude);

        // Fill with existing parking lot data
        txtName.setText(parkingLot.getName());
        txtDescription.setText(parkingLot.getDescription());
        txtLatitude.setText(Double.toString(parkingLot.getLocation().getLatitude()));
        txtLongitude.setText(Double.toString(parkingLot.getLocation().getLongitude()));

        btnUpdate = (Button)findViewById(R.id.editParkingLot_UpdateBtn);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the ParkingLot object
                ParkingLot updatedParkingLot = new ParkingLot();
                updatedParkingLot.setDocId(parkingLot.getDocId());
                updatedParkingLot.setName(txtName.getText().toString());
                updatedParkingLot.setDescription(txtDescription.getText().toString());
                double latitude = Double.parseDouble(txtLatitude.getText().toString());
                double longitude = Double.parseDouble(txtLongitude.getText().toString());
                updatedParkingLot.setLocation(new GeoPoint(latitude, longitude));
                updatedParkingLot.setPrice(parkingLot.getPrice());
                updatedParkingLot.setSpots(parkingLot.getSpots());
                // Update the database
                DocumentReference sfDocRef = db.collection("ParkingLotDemo").document(updatedParkingLot.getDocId());
                Task insert = sfDocRef.set(updatedParkingLot);
                insert.addOnSuccessListener(o -> {
                    Log.d("TAG", "Parking lot info updated!");
                    Toast.makeText(EditParkingLotActivity.this, "Updated Parking Lot", Toast.LENGTH_SHORT).show();
                    finish();
                });
                insert.addOnFailureListener(o->{
                    Log.e("TAG", "Failed to update parking lot info.");
                    Toast.makeText(EditParkingLotActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });

        btnCancel = (Button)findViewById(R.id.editParkingLot_CancelBtn);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}