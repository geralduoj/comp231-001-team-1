package com.comp231.easypark.userprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comp231.easypark.OptionsMenuActivity;
import com.comp231.easypark.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firestore.v1.WriteResult;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.comp231.easypark.Login.user;

//import static com.comp231.easypark.Login.myPreference;

public class DriverProfile extends OptionsMenuActivity {

    EditText firstNameEdit;
    EditText lastNameEdit;
    EditText emailEdit;
    EditText passwordEdit;
    EditText confirmPasswordEdit;

    TextView firstName;
    TextView lastName;
    TextView email;
    TextView password;


    ConstraintLayout visualInformation;
    ConstraintLayout editableInformation;


    String uid;
    String userEmail;
    String currentPassword;

    public static DocumentReference userDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        firstNameEdit = findViewById(R.id.firstNameEditText);
        lastNameEdit = findViewById(R.id.lastNameEditText);
        emailEdit = findViewById(R.id.emailEditText);
        passwordEdit = findViewById(R.id.passwordEditText);
        confirmPasswordEdit = findViewById(R.id.confirmPasswordEditText);

        firstName = findViewById(R.id.firstNameTextView);
        lastName = findViewById(R.id.lastNameTextView);
        email = findViewById(R.id.emailTextView);
        password = findViewById(R.id.passwordTextView);


        visualInformation = findViewById(R.id.visualInformation);
        editableInformation = findViewById(R.id.editableInformation);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            userEmail = user.getEmail();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

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
                        firstName.setText(document.getString("firstName"));
                        firstNameEdit.setText(firstName.getText());

                        lastName.setText(document.getString("lastName"));
                        lastNameEdit.setText(lastName.getText());

                        email.setText(userEmail);
                        emailEdit.setText(userEmail);

                        currentPassword = document.getString("password");
                        password.setText(currentPassword);
                        passwordEdit.setText(currentPassword);
                        confirmPasswordEdit.setText(currentPassword);
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

    public void goToActivityList(View view)
    {
        startActivity(new Intent(getApplicationContext(), ReservationList.class));
    }

    public void goToPaymentInfo(View view)
    {
        startActivity(new Intent(getApplicationContext(), PaymentInfo.class));
    }

    public void editInformation(View view)
    {
        firstNameEdit.setText(firstName.getText());
        lastNameEdit.setText(lastName.getText());
        emailEdit.setText(email.getText());
        passwordEdit.setText(password.getText());


        visualInformation.setVisibility(View.GONE);
        editableInformation.setVisibility(View.VISIBLE);
    }

    public void cancelEdition(View view)
    {
        visualInformation.setVisibility(View.VISIBLE);
        editableInformation.setVisibility(View.GONE);

    }

    public void saveInformation(View view)
    {
        if(user != null)
        {
            if (passwordEdit.getText().toString().equals(confirmPasswordEdit.getText().toString()))
            {
                if (firstNameEdit.getText().toString().isEmpty())
                {
                    firstNameEdit.setError("Please enter your first name");
                    return;
                }

                if (lastNameEdit.getText().toString().isEmpty())
                {
                    lastNameEdit.setError("Please enter your last name");
                    return;
                }

                if (emailEdit.getText().toString().isEmpty())
                {
                    emailEdit.setError("Please enter your email");
                    return;
                }

                String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$";
                Pattern pat = Pattern.compile(emailRegex);
                if (!pat.matcher(emailEdit.getText().toString()).matches())
                {
                    emailEdit.setError("Please enter a valid email");
                    return;
                }

                String fName = firstNameEdit.getText().toString();
                String lName = lastNameEdit.getText().toString();
                String _email = emailEdit.getText().toString();
                String newPassword = passwordEdit.getText().toString();

                firstName.setText(fName);
                lastName.setText(lName);
                email.setText(_email);
                password.setText(newPassword);

                Map<String, Object> userInfos = new HashMap<>();
                userInfos.put("firstName", fName);
                userInfos.put("lastName", lName);
                userInfos.put("email", _email);
                userInfos.put("password", newPassword);


                if (!userEmail.equals(emailEdit.getText().toString()))
                {
                    user.updateEmail(emailEdit.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Log.d("LOGGER", "User email address updated");
                            }
                        }
                    });
                }

                if (!currentPassword.equals(passwordEdit.getText().toString()))
                {
                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Log.d("LOGGER", "User password updated");
                            }
                        }
                    });
                }

                userDocRef.set(userInfos, SetOptions.merge());

                visualInformation.setVisibility(View.VISIBLE);
                editableInformation.setVisibility(View.GONE);
            }
            else
            {
                passwordEdit.setError("Password not matching");
                confirmPasswordEdit.setError("Password not matching");
            }

        }

    }

}