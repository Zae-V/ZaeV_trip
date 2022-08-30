package com.zaev.ZaeV_trip.Search.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zaev.ZaeV_trip.R;

import java.util.ArrayList;

public class CityAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> cityName;

    LayoutInflater inflater;

    public CityAdapter(Context context, ArrayList<String> cityName) {
        this.context = context;
        this.cityName = cityName;
    }

    @Override
    public int getCount() {
        return cityName.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null){
            view = inflater.inflate(R.layout.item_city,null);
        }
        TextView textView = view.findViewById(R.id.cityName);
        textView.setText(cityName.get(i));

        return view;
    }
}
