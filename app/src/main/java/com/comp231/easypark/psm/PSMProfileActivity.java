package com.comp231.easypark.psm;

import androidx.appcompat.app.AppCompatActivity;
import com.comp231.easypark.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PSMProfileActivity extends AppCompatActivity {

    TextView firstName;
    TextView lastName;
    TextView email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psm_profile);

        firstName = findViewById(R.id.profileFirstNameResult);
        lastName = findViewById(R.id.profileLastNameResult);
        email = findViewById(R.id.profileEmailResult);

        firstName.setText(PSMManager.getPSM().getFirstName());
        lastName.setText(PSMManager.getPSM().getLastName());
        email.setText(PSMManager.getPSM().getEmail());
    }

    public void goToUpdateProfile(View view) {
        // TODO: Profile activity and link
        startActivity(new Intent(getApplicationContext(), EditPSMProfileActivity.class));
    }
}