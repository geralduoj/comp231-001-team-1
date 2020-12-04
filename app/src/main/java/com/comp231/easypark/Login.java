package com.example.loginlogoutauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.comp231.easypark.RegisterPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    public static final String TAG = "TAG";
    private EditText loginPageEmail, loginPagePassword;
    private Button loginPage_registerBtn,loginPage_loginBtn;
    private TextView forgotTextLink;
    ProgressBar progressBar;

    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginPageEmail=findViewById(R.id.loginPage_emailR);
        loginPagePassword=findViewById(R.id.loginPage_passwordR);
        progressBar = findViewById(R.id.progressBar);

        loginPage_loginBtn=findViewById(R.id.loginPage_loginBtn);

        loginPage_registerBtn = findViewById(R.id.loginPage_registerBtn);
        forgotTextLink = findViewById(R.id.forgotPassword);
        fAuth = FirebaseAuth.getInstance();
        attachJavaObjectToXML();

        loginPage_loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = loginPageEmail.getText().toString().trim();
                String password = loginPagePassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    loginPageEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    loginPagePassword.setError("Password is Required.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // authenticate the user
                try {
                    fAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = fAuth.getCurrentUser();

                                        String uid = user.getUid();
                                        System.out.println(uid);

                                        Log.d("User Iddsdddd: ",uid);

//                                updateUI(user)
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    }else {
                                        Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println(email);
                    System.out.println(password);
                }


            }
        });

        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });

    }

    private void moveToRegisterPage(){
        try{
            startActivity(new Intent( getApplicationContext(), RegisterPage.class));
        } catch (Exception e){

        }
    }

    private void attachJavaObjectToXML() {
        try {

//            objectRelativeLayout = findViewById(R.id.lo)



            loginPage_registerBtn.setOnClickListener (new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    moveToRegisterPage();
                }
            });
        }
        catch (Exception e) {
            Toast.makeText(this,"LoginPage: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}