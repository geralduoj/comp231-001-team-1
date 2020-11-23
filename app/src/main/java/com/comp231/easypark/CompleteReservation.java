package com.comp231.easypark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
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
    Reservation reservation;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference resDocRef = db.collection("Reservation").document();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_reservation);
        reservation = new Reservation();


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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String currentDateandTime = sdf.format(new Date());
        showReservationTime.setText(currentDateandTime);


    }

    public void insertReservation()
    {

    }
}