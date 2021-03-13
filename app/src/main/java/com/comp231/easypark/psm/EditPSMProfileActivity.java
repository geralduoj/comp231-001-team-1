package com.comp231.easypark.psm;

import androidx.appcompat.app.AppCompatActivity;

import com.comp231.easypark.Login;
import com.comp231.easypark.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditPSMProfileActivity extends AppCompatActivity {

    FirebaseFirestore db;

    EditText firstNameEdit;
    EditText lastNameEdit;
    TextView emailEdit;
    EditText password;
    EditText confirmPassword;

    private static final String FIRSTNAME_KEY = "firstName";
    private static final String LASTNAME_KEY = "lastName";
    private static final String EMAIL_KEY = "email";
    private static final String PASSWORD_KEY = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_psm_profile);

        db = FirebaseFirestore.getInstance();

        firstNameEdit = findViewById(R.id.firstNameField);
        lastNameEdit = findViewById(R.id.lastNameField);
        emailEdit = findViewById(R.id.registerEmailField);
        password = findViewById(R.id.registerPasswordField);
        confirmPassword = findViewById(R.id.registerConfirmPasswordField);

        firstNameEdit.setText(PSMManager.getPSM().getFirstName());
        lastNameEdit.setText(PSMManager.getPSM().getLastName());
        emailEdit.setText(PSMManager.getPSM().getEmail());
    }

    public void saveInformation(View view) {
        String fname = firstNameEdit.getText().toString();
        String lname = lastNameEdit.getText().toString();
        String pass;

        if(!password.getText().toString().equals(confirmPassword.getText().toString())){
            Toast.makeText(EditPSMProfileActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            pass = password.getText().toString();
        }

        DocumentReference contact = db.collection("PSM").document(emailEdit.getText().toString());
        contact.update(FIRSTNAME_KEY, fname);
        contact.update(LASTNAME_KEY, lname);
        contact.update(EMAIL_KEY, emailEdit.getText().toString());
        contact.update(PASSWORD_KEY, pass)
                .addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditPSMProfileActivity.this, "Updated Successfully",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        PSMManager.getPSM().setFirstName(fname);
        PSMManager.getPSM().setLastName(lname);
        PSMManager.getPSM().setPassword(pass);

        startActivity(new Intent(getApplicationContext(), PSMProfileActivity.class));


    }
}