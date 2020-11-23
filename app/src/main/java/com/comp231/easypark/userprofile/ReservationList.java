package com.comp231.easypark.userprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.comp231.easypark.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.comp231.easypark.userprofile.DriverProfile.userDocRef;

public class ReservationList extends AppCompatActivity {

    private ReservationListAdapter listAdapter;

    private ArrayList<Reservation> reservationList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reservation_list);
        listAdapter = new ReservationListAdapter(this, reservationList);

        initList();

        RecyclerView recyclerView = findViewById(R.id.userActivityRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);


    }

    private void initList()
    {
        CollectionReference reservations = userDocRef.collection("Reservation");
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
                            Reservation reservation = new Reservation( doc.getTimestamp("reserveTime"),
                                    doc.getString("parkingLotId"),
                                    doc.getString("userId"), doc.getString("parkingSpotId"),(double) doc.get("cost"));
                            reservationList.add(reservation);
                        }
                    }

                }
                Log.d("LOGGER", "received reservation List");
                listAdapter.notifyDataSetChanged();

            }
        });
    }

}