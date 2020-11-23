package com.comp231.easypark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CompleteReservation extends AppCompatActivity {

    //this userId that is hard-coded now, will be later changed by retrieving userId of the user that is logged in
    String userId="poKNGutWzzK8YjW51hOT";

    //this parkingLotId that is hard-coded now, will be later changed by retrieving information from previous page
    String parkingLotId="HBsJKENagjA6FHwSJUJh";

    TextView showUserId;
    TextView showParkingLotId;
    TextView showParkingSpotId;
    TextView showReservationTime;
    TextView showCost;
    TextView showReservationId;
    private long cost;
    Reservation reservation;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference resDocRef = db.collection("Reservation").document();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_reservation);
        reservation = new Reservation();
        cost = 0;


        showUserId = (TextView) findViewById(R.id.textViewUserId);
        showParkingLotId = (TextView) findViewById(R.id.textViewParkingLotId);
        showParkingSpotId = (TextView) findViewById(R.id.textViewParkingSpotId);
        showReservationTime = (TextView) findViewById(R.id.textViewReservationTime);
        showCost = (TextView) findViewById(R.id.textViewCost);
        showReservationId = (TextView) findViewById(R.id.textViewReservationId);



        showUserId.setText(userId);
        showParkingLotId.setText(SeeReservation.newReservation.getParkingLotID());
        showParkingSpotId.setText(String.valueOf(SeeReservation.newReservation.getAvailableParkingSpot()));
        showCost.setText(SeeReservation.newReservation.getParkingCost());
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String currentDateandTime = sdf.format(new Date());
        showReservationTime.setText(currentDateandTime);


    }

    public void insertReservation(View view)
    {
        //DocumentReference docRef =  db.collection("ReservationDemo").document();
        //Log.d(TAG, "docId: " + resDocRef.getId());
        //txtReservationId.setText(docRef.getId());
        //Reservation r = new Reservation();
        reservation.setCost(cost);
        reservation.setParkingLotId(parkingLotId);
        reservation.setParkingSpotId(SeeReservation.newReservation.getAvailableParkingSpot());
        reservation.setReserveTime(Timestamp.now());
        reservation.setUserId(userId);
        Task insert = resDocRef.set(reservation);
        insert.addOnSuccessListener(o -> {
            Context context = getApplicationContext();
            CharSequence text = "Success!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            //Log.d(TAG, "YAY! dummy added");
            //endTime = SystemClock.uptimeMillis() + DURATION;
            //timerHandler.postDelayed(timerRun, 0);
        });
        insert.addOnFailureListener(o->{
            //Log.e(TAG, "OhO.. dummy escaped..");
        });
    }


}