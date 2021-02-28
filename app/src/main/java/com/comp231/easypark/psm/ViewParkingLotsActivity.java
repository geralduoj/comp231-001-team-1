package com.comp231.easypark.psm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.comp231.easypark.Login;
import com.comp231.easypark.R;

public class ViewParkingLotsActivity extends AppCompatActivity {

    ListView listParkingLots;
    ParkingLotListAdapter parkingLotListAdapter;
    Button btnCreateNew;
    Button btnDeleteSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_parking_lots);

        // Set up list of ParkingLots
        listParkingLots = (ListView)findViewById(R.id.listViewParkingLots);
        // TODO: Just using fake data for now
        String[] names = {"Sheppard Yonge", "King St"};
        String[] descriptions = {"Right next to Sheppard Yonge station.", "Close to Eaton Center."};
        parkingLotListAdapter = new ParkingLotListAdapter(ViewParkingLotsActivity.this, names, descriptions);
        listParkingLots.setAdapter(parkingLotListAdapter);

        // Set up buttons
        btnCreateNew = (Button)findViewById(R.id.viewParkingLots_btnCreate);
        btnCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateParkingLotActivity.class));
            }
        });
        btnDeleteSelected = (Button)findViewById(R.id.viewParkingLots_btnDelete);
        btnDeleteSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}