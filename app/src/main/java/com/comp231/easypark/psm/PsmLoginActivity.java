package com.comp231.easypark.psm;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.comp231.easypark.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PsmLoginActivity extends AppCompatActivity {

    private EditText loginPageEmail, loginPagePassword;
    private Button loginPage_loginBtn;
    ProgressBar progressBar;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psm_login);

        db = FirebaseFirestore.getInstance();

        loginPageEmail=findViewById(R.id.loginPage_emailR);
        loginPagePassword=findViewById(R.id.loginPage_passwordR);
        progressBar = findViewById(R.id.progressBar);
        loginPage_loginBtn=findViewById(R.id.loginPage_loginBtn);
        // TODO: Sign up button (replace it with PSM license application button?)

        loginPage_loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Find a PSM with the same email and get its password
                DocumentReference docRef = db.collection("PSM").document(loginPageEmail.getText().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                PSM psm = document.toObject(PSM.class);
                                // Does the password match?
                                if (psm.PasswordConfirmation(loginPagePassword.getText().toString())) {
                                    Log.d("TAG", "Password correct!");
                                    startActivity(new Intent(getApplicationContext(), PsmDashboardActivity.class));
                                } else {
                                    Log.d("TAG", "Password wrong!");
                                    Toast.makeText(PsmLoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Log.d("TAG", "ERROR: Couldn't find that PSM email");
                            Toast.makeText(PsmLoginActivity.this, "Email not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Create dummy PSM code (caution: handle with care so as not to create multiple PSMs that are the same)
        /*Map<String, Object> newPSM = new HashMap<>();
        newPSM.put("firstName", "Elon");
        newPSM.put("lastName", "Musk");
        newPSM.put("email", "elon@Tesla.com");
        newPSM.put("password", "password");
        newPSM.put("parkingLots", Arrays.asList("38Z9K7oAFIFyfiIX7ED0","QdhM4vuEH6RfqWcbWrSR"));
        newPSM.put("firstName", "Jeff");
        newPSM.put("lastName", "Bezos");
        newPSM.put("email", "mr.j@amazon.com");
        newPSM.put("password", "password");
        newPSM.put("parkingLots", Arrays.asList("U4CB4ACFoNAglPeUNDbX","t6nJzSqWOYFvqqkqQ5zN"));
        DocumentReference docRef = db.collection("PSM").document("mr.j@amazon.com");
        Task insert = docRef.set(newPSM);
        insert.addOnSuccessListener(o -> {
            Log.d("TAG", "New PSM added!");
        });*/
    }
}