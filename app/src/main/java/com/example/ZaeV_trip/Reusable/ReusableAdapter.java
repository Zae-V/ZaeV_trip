package com.example.ZaeV_trip.Reusable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ZaeV_trip.R;

import java.util.ArrayList;

public class ReusableAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;

    ArrayList<String> name;
    ArrayList<String> location;
    ArrayList<String> reason;

    public ReusableAdapter() {

    }

    public ReusableAdapter(ReusableActivity reusableActivity, ArrayList<String> names, ArrayList<String> locations, ArrayList<String> reasons) {
        this.context = reusableActivity;
        this.name = names;
        this.location = locations;
        this.reason = reasons;
    }

    @Override
    public int getCount() {
        return name.size();
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
            view = inflater.inflate(R.layout.item_cafe,null);
        }
        TextView nameview = view.findViewById(R.id.list_name);
        TextView locview = view.findViewById(R.id.list_location);
        TextView catview = view.findViewById(R.id.list_category);

        nameview.setText(name.get(i));
        locview.setText(location.get(i));
        catview.setText(reason.get(i));

        return view;
    }

}
