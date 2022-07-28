package com.example.ZaeV_trip.Reusable;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ZaeV_trip.R;

public class ReusableViewHolder {
    public ImageView listImage;

    public TextView listName;

    public TextView listLocation;

    public TextView listReason;

    public ReusableViewHolder(View a_view){
        listImage = a_view.findViewById(R.id.list_image);
        listName = a_view.findViewById(R.id.list_name);
        listLocation = a_view.findViewById(R.id.list_location);
        listReason = a_view.findViewById(R.id.list_category);
    }
}
