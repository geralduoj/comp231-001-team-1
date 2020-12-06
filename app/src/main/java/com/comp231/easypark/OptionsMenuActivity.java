package com.comp231.easypark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.comp231.easypark.userprofile.DriverProfile;
import com.comp231.easypark.userprofile.PaymentInfo;
import com.comp231.easypark.userprofile.PaymentMethod;
import com.comp231.easypark.userprofile.ProfileActivity;
import com.comp231.easypark.userprofile.ReservationList;


public class OptionsMenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                this.startActivity(intent);
                break;
            case R.id.map:
                Intent intentMap = new Intent(this, MapActivity.class);
                this.startActivity(intentMap);
                break;
            case R.id.parkinghistory:
                Intent intentParkingHistory = new Intent(this, ReservationList.class);
                this.startActivity(intentParkingHistory);
                break;
            case R.id.paymentinfo:
                Intent intentPaymentInfo = new Intent(this, PaymentInfo.class);
                this.startActivity(intentPaymentInfo);
                break;
            case R.id.feedback:
//                Intent intentFeedback = new Intent(this, FeedbackActivity.class);
//                this.startActivity(intentFeedback);
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

}
