package com.comp231.easypark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.comp231.easypark.userprofile.Reservation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AutoCancellationActivity extends AppCompatActivity {
    private Button btnArrived;
    private Button btnAddDummy;
    private TextView txtReservationId;
    private static final long DURATION = 10 * 1000;
    private TextView timer;
    private Handler timerHandler;
    long endTime, updateTime = 0L;
    int mSec, sec, min;

    private static final String TAG = "AutoCancelActivity";

    private String reservationId;
    private FirebaseFirestore db;
    private Reservation reservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_cancellation);
        db = FirebaseFirestore.getInstance();

        txtReservationId = findViewById(R.id.txtReservationId);
        timer = findViewById(R.id.timer);
        timerHandler = new Handler();

        btnAddDummy = findViewById(R.id.btnAddDummy);
        btnAddDummy.setOnClickListener(v->{
//            String s = Login.userDocRef.getId();
            DocumentReference docRef =  db.collection("Users").document(Login.userDocRef.getId()).collection("Reservation").document();
            Log.d(TAG, "docId: " + docRef.getId());
            txtReservationId.setText(docRef.getId());
            Reservation r = new Reservation(Timestamp.now(), "CLCzWcWb86AUAVdyEDjm", "QLdaWjjxgZONlaEPd6o3HJ9rUud2", 1, 12);

            Task insert = docRef.set(r);
            insert.addOnSuccessListener(o -> {
                Log.d(TAG, "YAY! dummy added");
                endTime = SystemClock.uptimeMillis() + DURATION;
                timerHandler.postDelayed(timerRun, 0);
            });
            insert.addOnFailureListener(o->{
                Log.e(TAG, "OhO.. dummy escaped..");
            });
        });

        btnArrived = findViewById(R.id.btnArrived);
        btnArrived.setOnClickListener(v -> {
            checkReservation();
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
                            Log.d(TAG, "RESERVATION DELETED!");
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