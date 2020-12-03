package com.comp231.easypark.userprofile;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.comp231.easypark.R;

import java.util.ArrayList;


public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.MyViewHolder> {

    private ArrayList<PaymentMethod> methodList;
    private OnCardListener mOnCardListener;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView type;
        public TextView cardEnding;
        public ImageView defaultMethodImage;
        OnCardListener onCardListener;

        public MyViewHolder (View itemView, OnCardListener onCardListener){
            super(itemView);
            type = itemView.findViewById(R.id.paymentType);
            cardEnding = itemView.findViewById(R.id.cardEnd);
            defaultMethodImage = itemView.findViewById(R.id.defaultMethodImage);
            this.onCardListener = onCardListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onCardListener.onCardClick(getAdapterPosition());
        }
    }

    public interface OnCardListener{
        void onCardClick(int position);
    }

    public PaymentMethodAdapter(Context context, ArrayList<PaymentMethod> paymentMethods, OnCardListener onCardListener)
    {
        this.methodList = paymentMethods;
        this.context = context;
        this.mOnCardListener = onCardListener;
    }

    @Override
    public PaymentMethodAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_payment_method_item, parent, false);

        return new MyViewHolder(v, mOnCardListener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        // replace the contents of the layout_listitem view with elements from data set
        holder.type.setText(methodList.get(position).type);
        holder.cardEnding.setText("Ending: " + methodList.get(position).cardNumber.substring(12));
        holder.defaultMethodImage.setVisibility(methodList.get(position).isDefault ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return methodList.size();
    }


}