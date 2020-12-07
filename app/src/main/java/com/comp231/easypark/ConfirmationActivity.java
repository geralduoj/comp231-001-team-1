package com.comp231.easypark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.comp231.easypark.reservation.ParkingLot;
import com.comp231.easypark.reservation.ParkingSpot;
import com.comp231.easypark.userprofile.Reservation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import android.content.Intent;

import java.util.List;

public class ConfirmationActivity extends AppCompatActivity {

    private String reservationId;
    private String ParkingLotIdFromBooking;
    private Button btnArrived;
    private TextView txtReservationId;
    private static final long DURATION = 10 * 1000;
    private TextView timer;
    private Handler timerHandler;
    long endTime, updateTime = 0L;
    int mSec, sec, min;

    private static final String TAG = "ConfirmationActivity";

    private FirebaseFirestore db;
    private Reservation reservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        db = FirebaseFirestore.getInstance();

        txtReservationId = findViewById(R.id.txtReservationId);
        timer = findViewById(R.id.timer);
        timerHandler = new Handler();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        reservationId = bundle.getString("reservationId");
        Log.e("RESERVATION ID:", reservationId);
        txtReservationId.setText(reservationId);

        btnArrived = findViewById(R.id.btnArrived);
        btnArrived.setOnClickListener(v -> {
            checkReservation();
            Intent intentMap = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(intentMap);
        });

        DocumentReference docRef =  db.collection("Users").document(Login.userDocRef.getId()).collection("Reservation").document(reservationId);
        docRef.get().addOnCompleteListener(task->{
           if(task.isSuccessful()){
               DocumentSnapshot document = task.getResult();
               if (document.exists()) {
                   reservation = new Reservation(document.getTimestamp("reserveTime"), document.getString("parkingLotId"), document.getString("userId"), (long) document.get("parkingSpotId"), (long) document.get("cost"));
                   ParkingLotIdFromBooking = reservation.getParkingLotId();
                   Task insert = docRef.set(reservation);
                   insert.addOnSuccessListener(o -> {
                       Log.d(TAG, "YAY! timer added");
                       endTime = SystemClock.uptimeMillis() + DURATION;
                       timerHandler.postDelayed(timerRun, 0);
                   });
                   insert.addOnFailureListener(o->{
                       Log.e(TAG, "OhO.. timer escaped..");
                   });
               } else {
                   Log.d(TAG, "No such reservation");
               }
           }
        });
    }

    public Runnable timerRun = new Runnable() {
        @Override
        public void run() {
            updateTime = endTime - SystemClock.uptimeMillis();
            sec = (int) (updateTime/1000);
            min = sec /60;
            sec = sec%60;
            mSec = (int) (updateTime%100);
            timer.setText(String.format("%02d:%02d:%02d", min, sec, mSec));
            timerHandler.postDelayed(this,50);
            if(updateTime <= 0){
                timerHandler.removeCallbacks(timerRun);
                timer.setText("00:00:00");
                Log.e(TAG, "times up");
                deleteReservation();
            }
        }
    };

    private void deleteReservation(){
        // delete reservation
        if(!txtReservationId.getText().toString().equals("")){
            reservationId = txtReservationId.getText().toString();
            DocumentReference docRef =  db.collection("Users").document(Login.userDocRef.getId()).collection("Reservation").document(reservationId);
            docRef.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "reservation data: " + document.getData());
                        Log.e(TAG, "RESERVATION EXPIRED!");
                        docRef.delete().addOnSuccessListener(o->{
                            updateParkingSpotWithinParkingLotForDelete();
                            Log.d(TAG, "RESERVATION DELETED!");
                            Intent intentToMap = new Intent(getApplicationContext(), MapActivity.class);
                            startActivity(intentToMap);
                        }).addOnFailureListener(o -> {
                            Log.e(TAG, "oops, error on reservation deletion");
                        });
                    } else {
                        Log.d(TAG, "No such reservation");
                    }
                }else{
                    Log.d(TAG, "get reservation failed with ", task.getException());
                }
            });
        }else{
            Log.d(TAG, "Invalid Reservation ID");
        }
    }

    private void updateParkingSpotWithinParkingLotForDelete() {

        DocumentReference lotDocRef = db.collection("ParkingLotDemo").document(ParkingLotIdFromBooking);

        lotDocRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("TAG", "reservation data: " + document.getData());

                    ParkingLot existingParkingLot = document.toObject(ParkingLot.class);

                    List<ParkingSpot> spots=existingParkingLot.getSpots();

                    if(spots != null){
                        for(ParkingSpot s : spots){
                            if(s.getStatus().equals("pending")){
                                s.setStatus("free");
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

    private void updateParkingSpotWithinParkingLot() {

        DocumentReference lotDocRef = db.collection("ParkingLotDemo").document(ParkingLotIdFromBooking);

        lotDocRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("TAG", "reservation data: " + document.getData());

                    ParkingLot existingParkingLot = document.toObject(ParkingLot.class);

                    List<ParkingSpot> spots=existingParkingLot.getSpots();

                    if(spots != null){
                        for(ParkingSpot s : spots){
                            if(s.getStatus().equals("pending")){
                                s.setStatus("booked");
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

    private void checkReservation(){
        if(!txtReservationId.getText().toString().equals("")){
            reservationId = txtReservationId.getText().toString();
            DocumentReference docRef =  db.collection("Users").document(Login.userDocRef.getId()).collection("Reservation").document(reservationId);
            docRef.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "reservation data: " + document.getData());
                        reservation = new Reservation(document.getTimestamp("reserveTime"), document.getString("parkingLotId"), document.getString("userId"), (long) document.get("parkingSpotId"), (long) document.get("cost"));
                        //reservation = document.toObject(Reservation.class);
                        if(reservation.getReserveTime().getSeconds() + 15 * 60 > Timestamp.now().getSeconds()){
                            Log.d(TAG, "RESERVATION OK!");
                            updateParkingSpotWithinParkingLot();
                            timerHandler.removeCallbacks(timerRun);
                            timer.setText("00:00:00");
                            endTime=0L;
                            updateTime=0L;
                            sec=0;
                            min=0;
                            mSec=0;
                        }else {
                            Log.e(TAG, "RESERVATION EXPIRED!");
                            docRef.delete().addOnSuccessListener(o->{
                                Log.d(TAG, "RESERVATION DELETED!");
                            }).addOnFailureListener(o -> {
                                Log.e(TAG, "oops, error on reservation deletion");
                            });
                        }
                    } else {
                        Log.d(TAG, "No such reservation");
                    }
                }else{
                    Log.d(TAG, "get reservation failed with ", task.getException());
                }
            });
        }else{
            Log.d(TAG, "Invalid Reservation ID");
        }
    }
}