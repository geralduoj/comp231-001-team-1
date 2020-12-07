package com.comp231.easypark.payment;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.comp231.easypark.ConfirmationActivity;
import com.comp231.easypark.R;
import com.comp231.easypark.reservation.ParkingLot;
import com.comp231.easypark.userprofile.Driver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import static com.comp231.easypark.Login.userDocRef;


public class PaymentActivity extends AppCompatActivity {

    public static Driver currentDriver;
    TextView parkingSpotName, spotNumberText, priceText,txt4, parkingDuration, parkingTime;
    Button payNowButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public String reservationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //get data
        parkingSpotName =findViewById(R.id.parkingLotField);
        spotNumberText = findViewById(R.id.spotNumberField);
        priceText = findViewById(R.id.price);
        //  btn = findViewById(R.id.btnCPay);
        parkingDuration = findViewById(R.id.parkingDuration);
        parkingTime = findViewById(R.id.parkingTimeValue);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String RetrievedReservationIdForPayment = bundle.getString("ReservationIdForPayment");
        reservationId = RetrievedReservationIdForPayment;


        payNowButton = findViewById(R.id.payNowButton);
        payNowButton.setOnClickListener(v->{
            Bundle bundle1 = new Bundle();
            bundle1.putString("reservationId", reservationId);
            Intent intent1= new Intent(getApplicationContext(), ConfirmationActivity.class);
            intent1.putExtras(bundle1);
            startActivity(intent1);
        });



        DocumentReference reservationDocRef = userDocRef.collection("Reservation").document(RetrievedReservationIdForPayment);
        reservationDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Reservation p = document.toObject(Reservation.class);
                        Timestamp reserveTime = (Timestamp)(document.get("reserveTime"));

                        String parkingLot = document.getString("parkingLotId");
                        //String reservationTime = p.getReservationTime().toString();
                        parkingDuration.setText(reserveTime.toString());
                        long spotNumber = (long)document.get("parkingSpotId");

                        spotNumberText.setText("" + spotNumber);
                        long price = (long) document.get("cost");
                        priceText.setText("$" + price);

                        String duration = "";

                        switch ((int) price)
                        {
                            case 4:
                                duration = "1h";
                                break;
                            case 12:
                                duration = "6h";
                                break;
                            case 20:
                                duration = "12h";
                                break;
                            case 30:
                                duration = "24h";
                                break;
                        }
                        parkingDuration.setText("" + duration);

                        Log.d("TAG", "ReservationTime: " + reserveTime);
                        Log.d("TAG", "ParkingLotId: " + parkingLot);

                        /*Parking lot Start*/
                        DocumentReference docRef = db.collection("ParkingLotDemo").document(parkingLot );
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        ParkingLot p = document.toObject(ParkingLot.class);
                                        parkingSpotName.setText(p.getName());

                                        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                                        String currentTime = sdf.format(new Date());
                                        parkingTime.setText(currentTime);

                                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                                    } else {
                                        Log.d("TAG", "No such document");
                                    }
                                } else {
                                    Log.d("TAG", "get failed with ", task.getException());
                                }
                            }
                        }); //  ParkingLotCollectionEnd
                        /*Parking lot End*/



                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
 /*       btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                String parkingLotId;

//  ReservationCollectionEnd
                }   //  btnOnClickEnd
            }); //  btnEventEnd*/
    }   //  onCreateEnd
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_menu, menu );
        return true;
    }

//    public void sendToConfirmation(View view) {
//        //   Intent intent = new Intent(PaymentActivity.this, ConfirmationActivity.class);
//        // startActivity(intent);
//
//    }


}


