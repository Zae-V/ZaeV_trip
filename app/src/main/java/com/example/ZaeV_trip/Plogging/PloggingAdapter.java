package com.example.ZaeV_trip.Plogging;

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
import com.example.ZaeV_trip.model.Plogging;
import com.example.ZaeV_trip.model.TouristSpot;

import java.util.ArrayList;

public class PloggingAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;

    ArrayList<Plogging> ploggings = new ArrayList<>();

    public PloggingAdapter() {

    }

    public PloggingAdapter(PloggingActivity ploggingActivity) {
        this.context = ploggingActivity;
    }

    public PloggingAdapter(PloggingActivity ploggingActivity, Plogging plogging) {
        this.context = ploggingActivity;
        this.ploggings.add(plogging);
    }

    public void addItem(Plogging item) {
        ploggings.add(item);
    }

    @Override
    public int getCount() {
        return ploggings.size();
    }

    @Override
    public Object getItem(int i) {
        return ploggings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = inflater.inflate(R.layout.item_plogging, null);
        }

        ImageView imageView = view.findViewById(R.id.list_image);
        TextView nameview = view.findViewById(R.id.list_name);
        TextView locview = view.findViewById(R.id.list_location);
        TextView catview = view.findViewById(R.id.list_category);

        Glide.with(view)
                .load(R.drawable.default_profile_image)
                .placeholder(R.drawable.default_profile_image)
                .error(R.drawable.default_profile_image)
                .fallback(R.drawable.default_profile_image)
                .into(imageView);

        nameview.setText(ploggings.get(i).getCrsKorNm());
        locview.setText(ploggings.get(i).getSigun());
        catview.setText("난이도: " + Integer.toString(ploggings.get(i).getCrsLevel()));

        return view;
    }
}