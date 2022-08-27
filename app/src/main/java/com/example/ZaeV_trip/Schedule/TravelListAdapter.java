package com.example.ZaeV_trip.Schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.util.ItemTouchHelperListener;

import java.util.ArrayList;

public class TravelListAdapter extends RecyclerView.Adapter<TravelListAdapter.ItemViewHolder>
        implements ItemTouchHelperListener {

    ArrayList<TravelItem> items = new ArrayList<>();
    OnTravelItemClickListener listener;

    public TravelListAdapter(ArrayList<TravelItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater를 이용해서 원하는 레이아웃을 띄워줌
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_travel, parent, false);
        return new ItemViewHolder(view, this::onItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        //ItemViewHolder가 생성되고 넣어야할 코드들을 넣어준다.
        holder.onBind(items.get(position));
        Glide.with(holder.itemView.getContext())
                .load(items.get(position).getImage())
                .placeholder(R.drawable.default_bird_img)
                .error(R.drawable.default_bird_img)
                .fallback(R.drawable.default_bird_img)
                .into(holder.list_image);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnTravelItemClickListener listener) {
        this.listener = listener;
    }

    public void onItemClick(ItemViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    public void addItem(TravelItem travelItem) {
        items.add(travelItem);
    }


    @Override
    public boolean onItemMove(int from_position, int to_position) {
        //이동할 객체 저장
        TravelItem travelItem = items.get(from_position);
        //이동할 객체 삭제
        items.remove(from_position);
        //이동하고 싶은 position에 추가
        items.add(to_position, travelItem);

        //Adapter에 데이터 이동알림
        notifyItemMoved(from_position, to_position);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView list_name, list_date;
        ImageView list_image;

        public ItemViewHolder(View itemView, final OnTravelItemClickListener listener) {
            super(itemView);
            list_name = itemView.findViewById(R.id.travel_name);
            list_date = itemView.findViewById(R.id.travel_date);
            list_image = itemView.findViewById(R.id.travel_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(ItemViewHolder.this, view, position);
                    }
                }
            });
        }

        public void onBind(TravelItem travelItem) {
            list_name.setText(travelItem.getName());
            list_date.setText(travelItem.getDate());
//            list_image.setImageResource(travelItem.getImage());
        }

    }

    public TravelItem getItem(int position) {
        return items.get(position);
    }
}
