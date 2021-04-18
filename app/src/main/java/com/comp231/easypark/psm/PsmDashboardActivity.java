package com.comp231.easypark.psm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.comp231.easypark.R;

public class PsmDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psm_dashboard);
    }

    public void goToProfile(View view) {
        startActivity(new Intent(getApplicationContext(), PSMProfileActivity.class));
    }

    public void goToViewParkingLots(View view) {
        startActivity(new Intent(getApplicationContext(), ViewParkingLotsActivity.class));
    }

    public void goToViewFeedback(View view) {
        startActivity(new Intent(getApplicationContext(), ViewPsmFeedbackActivity.class));
    }

    public void logout(View view) {
        startActivity(new Intent(getApplicationContext(), PsmLoginActivity.class));
        PSMManager.setPSM(null);
        finish();
    }
}