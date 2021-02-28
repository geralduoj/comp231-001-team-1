package com.comp231.easypark.psm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.comp231.easypark.R;

public class ParkingLotListAdapter extends BaseAdapter {

    Context context;
    private final String[] names;
    private final String[] descriptions;

    public ParkingLotListAdapter(Context context, String[] names, String[] descriptions) {
        this.context = context;
        this.names = names;
        this.descriptions = descriptions;
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
        ViewHolder viewHolder;
        final View result;
        if (convertView == null) {
            // Create new view holder and link with the views
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.parking_lot_list_item, parent, false);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.imgLot);
            viewHolder.name = (TextView)convertView.findViewById(R.id.txtName);
            viewHolder.description = (TextView)convertView.findViewById(R.id.txtDescription);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.name.setText(names[position]);
        viewHolder.description.setText(descriptions[position]);

        return convertView;
    }


    private static class ViewHolder {
        ImageView image;
        TextView name;
        TextView description;
    }
}
