package com.comp231.easypark.userprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.comp231.easypark.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.comp231.easypark.userprofile.DriverProfile.userDocRef;

public class PaymentInfo extends AppCompatActivity  implements PaymentMethodAdapter.OnCardListener {

    private PaymentMethodAdapter paymentMethodAdapter;

    private ArrayList<PaymentMethod> methodsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_info);
        initList();

        RecyclerView recyclerView = findViewById(R.id.methodRecyclerView);
        recyclerView.setHasFixedSize(true);
        paymentMethodAdapter = new PaymentMethodAdapter(this, methodsList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(paymentMethodAdapter);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        methodsList.clear();
        initList();
    }

    private void initList()
    {

        CollectionReference reservations = userDocRef.collection("PaymentMethod");
        reservations.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    QuerySnapshot document = task.getResult();
                    if (document != null)
                    {
                        List<DocumentSnapshot> documentSnapshots = document.getDocuments();
                        for (DocumentSnapshot doc : documentSnapshots)
                        {
                            PaymentMethod method = new PaymentMethod( doc.getId(), doc.getString("type"),
                                    doc.getString("cardNumber"), doc.getString("holderName"), doc.getString("expirationDate"));
                            methodsList.add(method);
                        }
                    }

                }
                Log.d("LOGGER", "received reservation List");
                paymentMethodAdapter.notifyDataSetChanged();

            }
        });
    }

    public void addPaymentMethod(View view)
    {
        startActivity(new Intent(getApplicationContext(), PaymentMethodInfo.class));

    }

    @Override
    public void onCardClick(int position) {
        Log.d("LOGGER", "clicked at position: " + position);
        PaymentMethod selectedMethod = methodsList.get(position);
        startActivity(new Intent(getApplicationContext(), PaymentMethodInfo.class).putExtra("method", selectedMethod));

    }
}