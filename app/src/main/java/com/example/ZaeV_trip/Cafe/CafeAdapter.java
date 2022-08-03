package com.example.ZaeV_trip.Cafe;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.Cafe;


public class CafeAdapter extends BaseAdapter{

    Context context;
    LayoutInflater inflater;

    ArrayList<Cafe> cafes = new ArrayList<>();


    public CafeAdapter() {

    }

    public CafeAdapter(CafeActivity cafeActivity){
        this.context = cafeActivity;
    }

    public CafeAdapter(CafeActivity cafeActivity, ArrayList cafes) {
        this.context = cafeActivity;
        this.cafes = cafes;
    }

    public void addItem(Cafe item){
        cafes.add(item);
    }

    @Override
    public int getCount() {
        return cafes.size();
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

        ImageView bookmarkbtn = view.findViewById(R.id.bookmarkBtn);

        nameview.setText(cafes.get(i).getName());
        locview.setText(cafes.get(i).getLocation());
        catview.setText(cafes.get(i).getCategory());

        bookmarkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookmarkbtn.setActivated(!bookmarkbtn.isActivated());
            }
        });

        return view;
    }
    
}
