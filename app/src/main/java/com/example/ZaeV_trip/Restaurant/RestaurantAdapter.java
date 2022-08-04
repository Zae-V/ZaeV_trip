package com.example.ZaeV_trip.Restaurant;

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
import com.example.ZaeV_trip.model.Restaurant;
import com.example.ZaeV_trip.model.TouristSpot;

import java.util.ArrayList;

public class RestaurantAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;

    ArrayList<Restaurant> restaurants = new ArrayList<>();

    public RestaurantAdapter() {

    }

    public RestaurantAdapter(RestaurantActivity restaurantActivity) {
        this.context = restaurantActivity;
    }

    public RestaurantAdapter(RestaurantActivity restaurantActivity, ArrayList<Restaurant> restaurants) {
        this.context = restaurantActivity;
        this.restaurants = restaurants;
    }

    public void addItem(Restaurant item){
        restaurants.add(item);
    }

    @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public Object getItem(int i) {
        return restaurants.get(i);
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
                .load(restaurants.get(i).getFirstImage())
                .placeholder(R.drawable.default_profile_image)
                .error(R.drawable.default_profile_image)
                .fallback(R.drawable.default_profile_image)
                .into(imageView);

        nameview.setText(restaurants.get(i).getTitle());
        locview.setText(restaurants.get(i).getAddr1());
        catview.setText(restaurants.get(i).getAddr2());

        return view;
    }
}
