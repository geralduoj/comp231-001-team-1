package com.comp231.easypark.userprofile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.comp231.easypark.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.comp231.easypark.Login.userDocRef;


public class ReservationListAdapter extends RecyclerView.Adapter<ReservationListAdapter.MyViewHolder> {

    private ArrayList<Reservation> reservationList;
    Context context;
    FirebaseFirestore db;

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView indexText;
        public TextView priceText;
        public TextView dateText;
        public TextView placeText;

        public MyViewHolder (View itemView){
            super(itemView);
            indexText = itemView.findViewById(R.id.indexText);
            priceText = itemView.findViewById(R.id.priceText);
            dateText = itemView.findViewById(R.id.dateText);
            placeText = itemView.findViewById(R.id.placeText);

        }
    }


    public ReservationListAdapter(Context context, ArrayList<Reservation> reservationList)
    {
        this.reservationList = reservationList;
        this.context = context;

    }

    @Override
    public ReservationListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_user_reservation_list_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Reservation reservation = reservationList.get(position);

        // replace the contents of the layout_listitem view with elements from data set
        holder.indexText.setText("" + (position + 1));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reservation.reserveTime.toDate());
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(reservation.reserveTime.toDate());
        holder.dateText.setText(date);
        holder.priceText.setText("$"+ reservation.cost);

        db = FirebaseFirestore.getInstance();
        DocumentReference reservations = db.collection("ParkingLotDemo").document(reservation.parkingLotId);
        reservations.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document != null)
                    {
                        Log.d("LOGGER", "parking lot id: " + reservation.parkingLotId);
                        Log.d("LOGGER", "parking lot name: " + document.getString("name"));

                        holder.placeText.setText("Parking Lot: " + document.getString("name"));

                    }
                    else
                    {
                        Log.d("LOGGER", "No such document");
                    }
                }
                else
                {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }


}