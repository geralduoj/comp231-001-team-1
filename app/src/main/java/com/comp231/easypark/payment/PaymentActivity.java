package com.comp231.easypark.payment;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.comp231.easypark.R;
import com.comp231.easypark.reservation.ParkingLot;
import com.comp231.easypark.reservation.ParkingSpot;
import com.comp231.easypark.userprofile.Driver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;
import static com.comp231.easypark.Login.userDocRef;


public class PaymentActivity extends AppCompatActivity {

    public static Driver currentDriver;
    TextView parkingSpotName, spotNumberText, priceText,txt4,txtReserveTime;
    Button btn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference noteRef = db.collection("Reservation");
    List<ParkingSpot> parkingSpotsList;
    List<ParkingSpot> displayPriceList;
    Map.Entry<String, Long> displayPriceMap;
    Map.Entry<Integer, String> displaySpotMap;


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
        txtReserveTime = findViewById(R.id.parkingDuration);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String RetrievedReservationIdForPayment = bundle.getString("ReservationIdForPayment");


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
                        txtReserveTime.setText(reserveTime.toString());
                        long spotNumber = (long)document.get("parkingSpotId");

                        spotNumberText.setText("" + spotNumber);
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
                                        List<ParkingSpot> parkingSpots= p.getSpots();
//                                        for(int i=0;i<parkingSpots.size();i++){
//                                            long id = Long.parseLong(String.valueOf(parkingSpots.get(i).getId()));
//                                            String status = parkingSpots.get(i).getStatus();
//                                            Log.d("TAG","Id: "+id+ "Status: "+status);
//                                            if (status.equals("booked")){
//                                                spotNumberText.setText(String.valueOf(id));
//                                                break;
//                                            }
//                                            else{
//                                                Log.d("TAG", "OOF! man");
//                                            }
//                                        }

                                        Map<String,Long> map = p.getPrice();
                                        String cost = String.valueOf(map.get("6hour"));        // get dynamic value from previous intent "6hour"

                                        priceText.setText(cost);
                                      // String duration = String.valueOf(map.get(Duration));
                                        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                                        String currentTime = sdf.format(new Date());
                                        txtReserveTime.setText(currentTime);

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

    public void sendToConfirmation(View view) {
        //   Intent intent = new Intent(PaymentActivity.this, ConfirmationActivity.class);
        // startActivity(intent);
    }


}


