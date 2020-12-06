package com.comp231.easypark.reservation;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.comp231.easypark.OptionsMenuActivity;
import com.comp231.easypark.R;
import com.comp231.easypark.userprofile.Home;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import static com.comp231.easypark.Login.user;

public class ReservationActivity extends OptionsMenuActivity {

    List<ParkingSpot> spots;
    ParkingLot existingParkingLot;
    TextView showParkingLotName;
    TextView showParkingSpotId;
    TextView showReservationTime;
    TextView showCost;

    private long cost;
    Reservation reservation;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference resDocRef = db.collection("ReservationDemo").document();
    String ParkingLotIdFromBooking;
    String ParkingLotNameFromBooking;
    String ReservationIdForPayment;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        reservation = new Reservation();
        cost = 0;
        showParkingLotName = (TextView) findViewById(R.id.parkingLotField);
        showParkingSpotId = (TextView) findViewById(R.id.spotNumberField);
        showReservationTime = (TextView) findViewById(R.id.reservationTime);
        showCost = (TextView) findViewById(R.id.parkingCost);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ParkingLotIdFromBooking = bundle.getString("retrievedParkingLotID");
        ParkingLotNameFromBooking = bundle.getString("retrievedParkingLotName");

        showParkingLotName.setText(ParkingLotNameFromBooking);

        showParkingSpotId.setText(String.valueOf(BookingActivity.newReservation.getAvailableParkingSpot()));
        showCost.setText(BookingActivity.newReservation.getParkingCost());
        String costType = showCost.getText().toString();
        switch (costType){
            case "12hour - $20":
                cost = 20;
                break;
            case "1hour - $4":
                cost = 4;
                break;
            case "24hour - $30":
                cost = 30;
                break;
            case "6hour - $12":
                cost = 12;
                break;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        String currentTime = sdf.format(new Date());
        showReservationTime.setText(currentTime);
    }

    public void insertReservation(View view)
    {
        reservation.setCost(cost);
        reservation.setParkingLotId(ParkingLotIdFromBooking);
        reservation.setParkingSpotId(BookingActivity.newReservation.getAvailableParkingSpot());
        reservation.setReserveTime(Timestamp.now());
        reservation.setUserId(user.getUid());
        Task insert = resDocRef.set(reservation);
        insert.addOnSuccessListener(o -> {
            Context context = getApplicationContext();
            CharSequence text = "Success!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            updateParkingSpotWithinParkingLot();

            Bundle bundle = new Bundle();
            bundle.putString("ReservationIdForPayment", resDocRef.getId());
            //this needs to be changed by Jyoti to PaymentActivity - for the moment I have linked to MainActivity
            Intent intent = new Intent(getApplicationContext(), Home.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        insert.addOnFailureListener(o->{
        });
    }

    public void updateParkingSpotWithinParkingLot() {

        DocumentReference lotDocRef = db.collection("ParkingLotDemo").document(ParkingLotIdFromBooking);

        lotDocRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("TAG", "reservation data: " + document.getData());

                    existingParkingLot = document.toObject(ParkingLot.class);

                    spots=existingParkingLot.getSpots();

                    if(spots != null){
                        for(ParkingSpot s : spots){
                            if(s.getStatus().equals("free")){
                                s.setStatus("pending");
                                break;
                            }
                        }

                        lotDocRef.update("spots", spots)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error updating document", e);
                                    }
                                });
                    }

                } else {
                    Log.d("TAG", "No such Parking Lot");
                }
            }else{
                Log.d("TAG", "get parking lot failed with ", task.getException());
            }
        });
    }
}