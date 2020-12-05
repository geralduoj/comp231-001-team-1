package com.comp231.easypark.userprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.comp231.easypark.OptionsMenuActivity;
import com.comp231.easypark.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.comp231.easypark.Login.user;
import static com.comp231.easypark.Login.userDocRef;


public class EditProfileActivity extends OptionsMenuActivity {

    EditText firstNameEdit;
    EditText lastNameEdit;
    EditText emailEdit;
    EditText passwordEdit;
    EditText confirmPasswordEdit;

    Driver currentDriver;

    String uid;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        currentDriver = (Driver) getIntent().getSerializableExtra("driver");

        firstNameEdit = findViewById(R.id.firstNameField);
        lastNameEdit = findViewById(R.id.lastNameField);
        emailEdit = findViewById(R.id.registerEmailField);
        passwordEdit = findViewById(R.id.registerPasswordField);
        confirmPasswordEdit = findViewById(R.id.registerConfirmPasswordField);

        firstNameEdit.setText(currentDriver.firstName);
        lastNameEdit.setText(currentDriver.lastName);
        emailEdit.setText(currentDriver.email);
        passwordEdit.setText(currentDriver.password);
        confirmPasswordEdit.setText(currentDriver.password);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            userEmail = user.getEmail();


            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            uid = user.getUid();
        }

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

                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("fName", fName);
                userInfo.put("lName", lName);
                userInfo.put("email", _email);
                userInfo.put("password", newPassword);


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

                if (!currentDriver.password.equals(passwordEdit.getText().toString()))
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

                userDocRef.set(userInfo, SetOptions.merge());

            }
            else
            {
                passwordEdit.setError("Password not matching");
                confirmPasswordEdit.setError("Password not matching");
                return;
            }

        }

        finish();
    }




}