package com.comp231.easypark.userprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.comp231.easypark.R;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

import static com.comp231.easypark.userprofile.DriverProfile.userDocRef;

public class PaymentMethodInfo extends AppCompatActivity {

    RadioGroup typeRadioGroup;
    RadioButton creditButton;
    RadioButton debitButton;
    EditText holderName;
    EditText cardNumber;
    EditText expirationDate;

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
        }
    }

    public void addMethod(View view)
    {
        if (typeRadioGroup.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(this, "Please, select payment type", Toast.LENGTH_LONG).show();
            return;
        }

        if (cardNumber.getText().toString().length() != 16)
        {
            Toast.makeText(this, "Please, enter a valid card number", Toast.LENGTH_LONG).show();
            return;
        }

        if (holderName.getText().toString().length() < 1)
        {
            Toast.makeText(this, "Please, enter the holder name", Toast.LENGTH_LONG).show();
            return;
        }

        if (expirationDate.getText().toString().length() != 4)
        {
            Toast.makeText(this, "Please, enter a valid expiration date", Toast.LENGTH_LONG).show();
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