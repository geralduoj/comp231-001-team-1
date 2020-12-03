package com.comp231.easypark.userprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.comp231.easypark.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.comp231.easypark.userprofile.DriverProfile.userDocRef;

public class PaymentMethodInfo extends AppCompatActivity {

    RadioGroup typeRadioGroup;
    RadioButton creditButton;
    RadioButton debitButton;
    EditText holderName;
    EditText cardNumber;
    EditText expirationDate;
    CheckBox defaultMethod;

    PaymentMethod method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method_info);

        typeRadioGroup = findViewById(R.id.typeRadioGroup);
        creditButton = findViewById(R.id.creditRadioButton);
        debitButton = findViewById(R.id.debitRadioButton);
        holderName = findViewById(R.id.holderNameText);
        cardNumber = findViewById(R.id.cardNumberText);
        expirationDate = findViewById(R.id.expirationDate);
        defaultMethod = findViewById(R.id.defaultMethod);

        method = (PaymentMethod) getIntent().getSerializableExtra("method");
        if (method != null)
        {
            if (method.type.equals("Debit"))
            {
                debitButton.setChecked(true);
            }
            else
            {
                creditButton.setChecked(true);
            }

            holderName.setText(method.holderName);
            cardNumber.setText(method.cardNumber);
            expirationDate.setText(method.expirationDate);
            defaultMethod.setChecked(method.isDefault);
            defaultMethod.setEnabled(!method.isDefault);
        } else
        {
            CollectionReference paymentMethods = userDocRef.collection("PaymentMethod");
            paymentMethods.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful())
                    {
                        QuerySnapshot document = task.getResult();
                        if (document != null)
                        {
                            List<DocumentSnapshot> documentSnapshot = document.getDocuments();
                            if (documentSnapshot.size() == 0)
                            {
                                defaultMethod.setChecked(true);
                                defaultMethod.setEnabled(false);
                            }
                        }

                    }
                    Log.d("LOGGER", "received reservation List");
                }
            });


        }
    }

    public void addMethod(View view)
    {
        if (typeRadioGroup.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(this, "Please, select payment type", Toast.LENGTH_LONG).show();
            return;
        }

        if (cardNumber.getText().toString().length() != 16 || !validateCreditCardNumber(cardNumber.getText().toString()))
        {
            cardNumber.setError("Please, enter a valid card number");
            return;
        }

        if (holderName.getText().toString().length() < 1)
        {
            holderName.setError("Please, enter the holder name");
            return;
        }

        if (expirationDate.getText().toString().length() != 5 || !validateExpirationDate(expirationDate.getText().toString()))
        {
            expirationDate.setError("Please, enter a valid expiration date");
            return;
        }

            String type;
            if (debitButton.isChecked())
            {
                type = "Debit";
            }
            else
            {
                type = "Credit";
            }


            Map<String, Object> currentMethod = new HashMap<>();
            currentMethod.put("type", type);
            currentMethod.put("cardNumber", cardNumber.getText().toString());
            currentMethod.put("holderName", holderName.getText().toString());
            currentMethod.put("expirationDate", expirationDate.getText().toString());
            currentMethod.put("default", defaultMethod.isChecked());

            if (defaultMethod.isChecked())
            {

                CollectionReference paymentMethods = userDocRef.collection("PaymentMethod");
                paymentMethods.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            QuerySnapshot document = task.getResult();
                            if (document != null)
                            {
                                List<DocumentSnapshot> documentSnapshot = document.getDocuments();
                                for (DocumentSnapshot doc : documentSnapshot)
                                {
                                    if (method == null || doc.getId() != method.paymentId){
                                        doc.getReference().update("default", false);
                                    }
                                }
                            }
                        }
                        Log.d("LOGGER", "received reservation List");
                        if (method != null)
                        {
                            DocumentReference docRef = userDocRef.collection("PaymentMethod").document(method.paymentId);
                            docRef.set(currentMethod);
                        } else
                        {
                            userDocRef.collection("PaymentMethod").add(currentMethod);
                        }

                        finish();
                    }
                });





            } else
            {
                if (method != null)
                {
                    DocumentReference docRef = userDocRef.collection("PaymentMethod").document(method.paymentId);
                    docRef.set(currentMethod);
                } else
                {
                    userDocRef.collection("PaymentMethod").add(currentMethod);
                }
                finish();

            }


    }

    private void changeDefaultMethod()
    {

    }


    private boolean validateExpirationDate(String s)
    {
        String date = s.substring(0,2) + s.substring(3,5);

        java.text.DateFormat sdf = new java.text.SimpleDateFormat("MMyy");
        java.util.Calendar now = java.util.Calendar.getInstance();
        now.set(now.get(java.util.Calendar.YEAR), now.get(java.util.Calendar.MONTH), 0, 23, 59, 59);
        Date exp = null;
        try {
            exp = sdf.parse(date);
            if (exp != null && exp.before(now.getTime())) return false;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return s.matches("(?:0[1-9]|1[0-2])/[0-9]{2}");
    }

    private boolean validateCreditCardNumber(String str) {

        int[] ints = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            ints[i] = Integer.parseInt(str.substring(i, i + 1));
        }
        for (int i = ints.length - 2; i >= 0; i = i - 2) {
            int j = ints[i];
            j = j * 2;
            if (j > 9) {
                j = j % 10 + 1;
            }
            ints[i] = j;
        }
        int sum = 0;
        for (int i = 0; i < ints.length; i++) {
            sum += ints[i];
        }
        if (sum % 10 == 0) {
            System.out.println(str + " is a valid credit card number");
            return true;
        } else {
            System.out.println(str + " is an invalid credit card number");
            return false;
        }
    }

}