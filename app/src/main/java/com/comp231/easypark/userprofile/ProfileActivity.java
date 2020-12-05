package com.comp231.easypark.userprofile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.comp231.easypark.MapActivity;
import com.comp231.easypark.OptionsMenuActivity;
import com.comp231.easypark.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.comp231.easypark.Login.user;
import static com.comp231.easypark.Login.userDocRef;

public class ProfileActivity extends OptionsMenuActivity {

    TextView firstName;
    TextView lastName;
    TextView email;


    ConstraintLayout visualInformation;
    ConstraintLayout editableInformation;


    String uid;
    String userEmail;
    String currentPassword;

    Driver currentDriver;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firstName = findViewById(R.id.profileFirstNameResult);
        lastName = findViewById(R.id.profileLastNameResult);
        email = findViewById(R.id.profileEmailResult);

        db = FirebaseFirestore.getInstance();


        getUsersInfo();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getUsersInfo();
    }

    void getUsersInfo()
    {

        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            userEmail = user.getEmail();


            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            uid = user.getUid();
        }

        userDocRef = db.collection("Users").document(uid);


        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document != null)
                    {
                        currentPassword = document.getString("password");
                        currentDriver = new Driver(
                                document.getString("fName"),
                                document.getString("lName"),
                                document.getString("email"),
                                document.getString("password"),
                                null);

                        currentPassword = currentDriver.password;
                        firstName.setText(currentDriver.firstName);

                        lastName.setText(currentDriver.lastName);

                        email.setText(currentDriver.email);

                    }
                    else
                    {
                        Log.d("LOGGER", "No such document");
                    }
                }
                else
                {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });
    }


    public void sendToEditProfile(View view) {

        startActivity( new Intent(ProfileActivity.this, EditProfileActivity.class).putExtra("driver", currentDriver));
    }

    public void sendToParkingHistory(View view) {
        Intent intent = new Intent(ProfileActivity.this, ReservationList.class);
        startActivity(intent);
    }

    public void sendToPaymentInfo(View view) {
        Intent intent = new Intent(ProfileActivity.this, PaymentInfo.class);
        startActivity(intent);
    }

}