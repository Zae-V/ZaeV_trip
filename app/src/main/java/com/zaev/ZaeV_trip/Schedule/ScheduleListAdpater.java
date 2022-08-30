package com.zaev.ZaeV_trip.Schedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.zaev.ZaeV_trip.R;

import java.util.ArrayList;

public class ScheduleListAdpater extends RecyclerView.Adapter<ScheduleListAdpater.ViewHolder> {
    Context context;
    ArrayList<Integer> number;
    LayoutInflater inflater;

    public ScheduleListAdpater(Context context,ArrayList<Integer> num){
        this.context = context;
        this.number = num;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_schedule, parent, false);
        ViewHolder viewHolder = new ViewHolder(context, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.day.setText("Day " + number.get(position));
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putInt("day",position+1);

                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                AddScheduleFragment addScheduleFragment = new AddScheduleFragment();
                addScheduleFragment.setArguments(bundle);

                transaction.replace(R.id.container_travel, addScheduleFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return number.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView day;
        public Button addBtn;
        public RecyclerView listview;

        public ViewHolder(Context context, @NonNull View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.dday);
            addBtn = itemView.findViewById(R.id.addBtn);
            listview = itemView.findViewById(R.id.listView);

        }
    }

}
