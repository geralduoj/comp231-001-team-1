package com.comp231.easypark.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.comp231.easypark.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void enterReservation(View view)
    {
        Intent intent = new Intent(this, BookingActivity.class);
        startActivity(intent);
    }
}