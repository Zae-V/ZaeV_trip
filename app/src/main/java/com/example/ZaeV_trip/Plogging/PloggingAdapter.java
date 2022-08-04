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
import com.example.ZaeV_trip.model.Plogging;

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

    public PloggingAdapter(PloggingActivity ploggingActivity, ArrayList<Plogging> ploggings) {
        this.context = ploggingActivity;
        this.ploggings = ploggings;
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

        TextView plogging_name = view.findViewById(R.id.plogging_name);
        TextView plogging_address = view.findViewById(R.id.plogging_address);
        TextView plogging_level = view.findViewById(R.id.plogging_level);

        plogging_name.setText(ploggings.get(i).getCrsKorNm());
        plogging_address.setText(ploggings.get(i).getSigun());
        plogging_level.setText("난이도: " + ploggings.get(i).getCrsLevel());

        return view;
    }
}