package com.example.ZaeV_trip.Festival;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.TouristSpot.TouristSpotActivity;
import com.example.ZaeV_trip.model.Festival;
import com.example.ZaeV_trip.model.TouristSpot;

import java.util.ArrayList;

public class FestivalAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;

    ArrayList<Festival> festivals = new ArrayList<>();

    public FestivalAdapter() {

    }

    public FestivalAdapter(FestivalActivity touristSpotActivity) {
        this.context = touristSpotActivity;
    }

    public FestivalAdapter(FestivalActivity touristSpotActivity, Festival festivals) {
        this.context = touristSpotActivity;
        this.festivals.add(festivals);
    }

    public void addItem(Festival item){
        festivals.add(item);
    }

    @Override
    public int getCount() {
        return festivals.size();
    }

    @Override
    public Object getItem(int i) {
        return festivals.get(i);
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
            view = inflater.inflate(R.layout.item_festival,null);
        }

        ImageView imageView = view.findViewById(R.id.list_image);
        TextView nameview = view.findViewById(R.id.list_name);
        TextView locview = view.findViewById(R.id.list_location);
        TextView catview = view.findViewById(R.id.list_date);

        Glide.with(view)
                .load(festivals.get(i).getFirstImage())
                .placeholder(R.drawable.default_profile_image)
                .error(R.drawable.default_profile_image)
                .fallback(R.drawable.default_profile_image)
                .into(imageView);

        nameview.setText(festivals.get(i).getTitle());
        locview.setText(festivals.get(i).getAddr1());
        catview.setText(festivals.get(i).getStartDate()+"~"+festivals.get(i).getEndDate());

        return view;
    }
}
