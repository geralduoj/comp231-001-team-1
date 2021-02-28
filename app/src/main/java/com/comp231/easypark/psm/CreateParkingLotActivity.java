package com.comp231.easypark.psm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.comp231.easypark.R;

public class CreateParkingLotActivity extends AppCompatActivity {

    private EditText txtName, txtDescription, txtLatitude, txtLongitude, txtNumSpots;
    private Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_parking_lot);

        txtName = (EditText)findViewById(R.id.createParkingLot_Name);
        txtDescription = (EditText)findViewById(R.id.createParkingLot_Description);
        txtLatitude = (EditText)findViewById(R.id.createParkingLot_Latitude);
        txtLongitude = (EditText)findViewById(R.id.createParkingLot_Longitude);
        txtNumSpots = (EditText)findViewById(R.id.createParkingLot_SpotNum);

        btnCreate = (Button)findViewById(R.id.createParkingLot_CreateBtn);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}