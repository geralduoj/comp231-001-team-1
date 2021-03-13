package com.comp231.easypark.psm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.comp231.easypark.R;

public class FeedbackListAdapter extends BaseAdapter {

    Context context;
    private final String[] names;
    private final String[] ratings;
    private final String[] reviews;

    public FeedbackListAdapter(Context context, String[] names, String[] ratings, String[] reviews) {
        this.context = context;
        this.names = names;
        this.ratings = ratings;
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        FeedbackListAdapter.ViewHolder viewHolder;
        final View result;
        if (convertView == null) {
            // Create new view holder and link with the views
            viewHolder = new FeedbackListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.feedback_list_item, parent, false);
            viewHolder.name = (TextView)convertView.findViewById(R.id.txtName);
            viewHolder.rating = (TextView)convertView.findViewById(R.id.txtRating);
            viewHolder.review = (TextView)convertView.findViewById(R.id.txtReview);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FeedbackListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.name.setText(names[position]);
        viewHolder.rating.setText(ratings[position]);
        viewHolder.review.setText(reviews[position]);

        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        TextView rating;
        TextView review;
    }

}
