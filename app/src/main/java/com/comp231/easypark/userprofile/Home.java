package com.comp231.easypark.userprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.comp231.easypark.AutoCancellationActivity;
import com.comp231.easypark.Login;
import com.comp231.easypark.MapActivity;
import com.comp231.easypark.OptionsMenuActivity;
import com.comp231.easypark.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

public class Home extends OptionsMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void goToProfile(View view) {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

    }

    public void goToMapPage(View v){
        startActivity(new Intent(getApplicationContext(), MapActivity.class));
    }

    public void goToAutoCancelPage(View v){
        startActivity(new Intent(getApplicationContext(), AutoCancellationActivity.class));
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}