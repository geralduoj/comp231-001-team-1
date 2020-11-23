package com.comp231.easypark.userprofile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.comp231.easypark.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ReservationListAdapter extends RecyclerView.Adapter<ReservationListAdapter.MyViewHolder> {

    private ArrayList<Reservation> reservationList;
    Context context;

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
        holder.placeText.setText("Parking Lot: " + reservation.parkingLotId);

    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }


}