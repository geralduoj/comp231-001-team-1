package com.comp231.easypark.psm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comp231.easypark.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

public class ApplyPsmLicenseActivity extends AppCompatActivity {

    private EditText txtName, txtAddress, txtEmail;
    private TextView txtThankYou;
    private Button btnApply, btnBack;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_psm_license);

        db = FirebaseFirestore.getInstance();

        txtName = (EditText)findViewById(R.id.applyLicense_Name);
        txtAddress = (EditText)findViewById(R.id.applyLicense_Address);
        txtEmail = (EditText)findViewById(R.id.applyLicense_Email);
        txtThankYou = (TextView)findViewById(R.id.applyLicense_ThankYou);
        txtThankYou.setVisibility(View.INVISIBLE);
        btnBack = (Button)findViewById(R.id.applyLicense_BackBtn);
        btnBack.setVisibility(View.INVISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnApply = (Button)findViewById(R.id.applyLicense_ApplyBtn);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new PSMApplication object
                PSMApplication newApp = new PSMApplication();
                newApp.setFullName(txtName.getText().toString());
                newApp.setAddress(txtAddress.getText().toString());
                newApp.setEmail(txtEmail.getText().toString());
                newApp.setDocId(db.collection("PSMApplication").document().getId());// get id of new entry
                // Record in the database
                DocumentReference docRef = db.collection("PSMApplication").document(newApp.getDocId());
                Task insert = docRef.set(newApp);
                insert.addOnSuccessListener(o -> {
                    Log.d("TAG", "New PSM application added!");
                    // Show thank you message
                    txtThankYou.setVisibility(View.VISIBLE);
                    btnBack.setVisibility(View.VISIBLE);
                    btnApply.setEnabled(false);
                    txtName.setEnabled(false);
                    txtAddress.setEnabled(false);
                    txtEmail.setEnabled(false);
                });
                insert.addOnFailureListener(o->{
                    Log.e("TAG", "Failed to add new PSM application.");
                    Toast.makeText(ApplyPsmLicenseActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                    finish();
                });

            }
        });
    }
}