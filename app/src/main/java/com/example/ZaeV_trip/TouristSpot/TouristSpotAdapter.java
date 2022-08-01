package com.example.ZaeV_trip.TouristSpot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.TouristSpot;

import java.util.ArrayList;

public class TouristSpotAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;

    ArrayList<TouristSpot> touristSpots = new ArrayList<>();

    public TouristSpotAdapter() {

    }

    public TouristSpotAdapter(TouristSpotActivity touristSpotActivity) {
        this.context = touristSpotActivity;
    }

    public TouristSpotAdapter(TouristSpotActivity touristSpotActivity, ArrayList<TouristSpot> touristSpots) {
        this.context = touristSpotActivity;
        this.touristSpots = touristSpots;
    }

    public void addItem(TouristSpot item){
        touristSpots.add(item);
    }

    @Override
    public int getCount() {
        return touristSpots.size();
    }

    @Override
    public Object getItem(int i) {
        return touristSpots.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null){
            view = inflater.inflate(R.layout.item_tourist_spot,null);
        }

        ImageView imageView = view.findViewById(R.id.list_image);
        TextView nameview = view.findViewById(R.id.list_name);
        TextView locview = view.findViewById(R.id.list_location);
        TextView catview = view.findViewById(R.id.list_category);

        Glide.with(view)
                .load(touristSpots.get(i).getFirstImage())
                .placeholder(R.drawable.default_profile_image)
                .error(R.drawable.default_profile_image)
                .fallback(R.drawable.default_profile_image)
                .into(imageView);

        nameview.setText(touristSpots.get(i).getTitle());
        locview.setText(touristSpots.get(i).getAddr1());
        catview.setText(touristSpots.get(i).getAddr2());

        return view;
    }
}
