package com.example.ZaeV_trip.Lodging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ZaeV_trip.Lodging.LodgingActivity;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.Lodging;

import java.util.ArrayList;

public class LodgingAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;

    ArrayList<Lodging> lodgings = new ArrayList<>();

    public LodgingAdapter() {

    }

    public LodgingAdapter(LodgingActivity touristSpotActivity) {
        this.context = touristSpotActivity;
    }

    public LodgingAdapter(LodgingActivity touristSpotActivity, Lodging lodgings) {
        this.context = touristSpotActivity;
        this.lodgings.add(lodgings);
    }

    public void addItem(Lodging item){
        lodgings.add(item);
    }

    @Override
    public int getCount() {
        return lodgings.size();
    }

    @Override
    public Object getItem(int i) {
        return lodgings.get(i);
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
            view = inflater.inflate(R.layout.item_lodging,null);
        }

        ImageView imageView = view.findViewById(R.id.list_image);
        TextView nameview = view.findViewById(R.id.list_name);
        TextView locview = view.findViewById(R.id.list_location);
        TextView catview = view.findViewById(R.id.list_date);

        Glide.with(view)
                .load(lodgings.get(i).getFirstImage())
                .placeholder(R.drawable.default_profile_image)
                .error(R.drawable.default_profile_image)
                .fallback(R.drawable.default_profile_image)
                .into(imageView);

        nameview.setText(lodgings.get(i).getTitle());
        locview.setText(lodgings.get(i).getAddr1());
        catview.setText(lodgings.get(i).getStartDate()+"~"+lodgings.get(i).getEndDate());

        return view;
    }
}

