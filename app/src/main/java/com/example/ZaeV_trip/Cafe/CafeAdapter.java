package com.example.ZaeV_trip.Cafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import com.example.ZaeV_trip.R;


public class CafeAdapter extends BaseAdapter{

    Context context;
    LayoutInflater inflater;

    ArrayList<String> name;
    ArrayList<String> location;
    ArrayList<String> category;

    public CafeAdapter() {

    }

    public CafeAdapter(CafeActivity cafeActivity, ArrayList<String> names, ArrayList<String> locations, ArrayList<String> categories) {
        this.context = cafeActivity;
        this.name = names;
        this.location = locations;
        this.category = categories;
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
        catview.setText(category.get(i));

        return view;
    }
    
}
