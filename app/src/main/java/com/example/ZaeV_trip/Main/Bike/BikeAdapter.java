package com.example.ZaeV_trip.Main.Bike;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ZaeV_trip.Plogging.PloggingAdapter;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.Plogging;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    ArrayList<Plogging> bikes;
    FirebaseFirestore mDatabase;

    public BikeAdapter() {

    }


    public BikeAdapter(Context context, ArrayList<Plogging> bikes) {
        this.context = context;
        this.bikes = new ArrayList<>(bikes);
    }

    @NonNull
    @Override
    public BikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_bike_course, parent, false);
        BikeAdapter.ViewHolder viewHolder = new BikeAdapter.ViewHolder(context, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BikeAdapter.ViewHolder holder, int position) {
        holder.nameview.setText(bikes.get(position).getCrsKorNm());
        holder.locview.setText(bikes.get(position).getSigun());
        int level = Integer.parseInt(bikes.get(position).getCrsLevel());
        String lev="";
        switch (level){
            case 1:
                lev = "난이도: 쉬움";
                break;
            case 2:
                lev = "난이도: 보통";
                break;
            case 3:
                lev = "난이도: 어려움";
                break;
        }
        holder.catview.setText(lev);
    }

    @Override
    public int getItemCount() {
        return bikes.size();
    }


    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private BikeAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(BikeAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameview;
        public TextView locview;
        public TextView catview;
        public ImageView imgView;

        public ViewHolder(Context context, @NonNull View itemView) {
            super(itemView);

            nameview = itemView.findViewById(R.id.routeText);
            locview = itemView.findViewById(R.id.addressText);
            catview = itemView.findViewById(R.id.levelText);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(view, pos);
                        }
                    }
                }
            });
        }
    }
}
