package com.comp231.easypark.psm;

import androidx.appcompat.app.AppCompatActivity;
import com.comp231.easypark.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PSMProfileActivity extends AppCompatActivity {

    TextView firstName;
    TextView lastName;
    TextView email;

    Button btnBack;

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

        btnBack = (Button)findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void goToUpdateProfile(View view) {
        startActivity(new Intent(getApplicationContext(), EditPSMProfileActivity.class));
    }
}