package com.example.ZaeV_trip.Schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ZaeV_trip.Bookmark.BookmarkItem;
import com.example.ZaeV_trip.R;


import java.util.ArrayList;

public class SelectListAdapter extends RecyclerView.Adapter<SelectListAdapter.ItemViewHolder>
        {

    ArrayList<BookmarkItem> items = new ArrayList<>();
//    OnTravelItemClickListener listener;

    public SelectListAdapter(ArrayList<BookmarkItem> items){
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater를 이용해서 원하는 레이아웃을 띄워줌
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_add_schedule, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        //ItemViewHolder가 생성되고 넣어야할 코드들을 넣어준다.
        holder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

//    public void setOnItemClickListener(OnTravelItemClickListener listener){
//        this.listener = listener;
//    }
//
//    public void onItemClick(ItemViewHolder holder, View view, int position) {
//        if(listener != null){
//            listener.onItemClick(holder, view, position);
//        }
//    }

    public void addItem(BookmarkItem bookmarkItem){
        items.add(bookmarkItem);
    }


//
//    @Override
//    public boolean onItemMove(int from_position, int to_position) {
//        //이동할 객체 저장
//        BookmarkItem bookmarkItem = items.get(from_position);
//        //이동할 객체 삭제
//        items.remove(from_position);
//        //이동하고 싶은 position에 추가
//        items.add(to_position, travelItem);
//
//        //Adapter에 데이터 이동알림
//        notifyItemMoved(from_position,to_position);
//        return true;
//    }
//
//    @Override
//    public void onItemSwipe(int position) {
//        items.remove(position);
//        notifyItemRemoved(position);
//    }



    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView list_name, list_location, list_hours;
        ImageView list_image;

        public ItemViewHolder(View itemView) {
            super(itemView);
            list_name = itemView.findViewById(R.id.list_name);
            list_location = itemView.findViewById(R.id.list_location);
            list_hours = itemView.findViewById(R.id.list_hours);
            list_image = itemView.findViewById(R.id.list_image);

            ImageButton toggleBtn = itemView.findViewById(R.id.plus_btn);
            toggleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (toggleBtn.isSelected()) {
                        toggleBtn.setImageResource(R.drawable.checked_btn);
                    }
                    else{
                        toggleBtn.setImageResource(R.drawable.fab_btn);
                    }
                    toggleBtn.setSelected(!toggleBtn.isSelected());
                }
            });

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position = getAdapterPosition();
//                    if(listener != null){
//                        listener.onItemClick(ItemViewHolder.this, view, position);
//                    }
//                }
//            });
        }

        public void onBind(BookmarkItem bookmarkItem) {
            list_name.setText(bookmarkItem.getName());
            list_location.setText(String.valueOf(bookmarkItem.getLocation()));
            list_hours.setText(String.valueOf(bookmarkItem.getHours()));
//            list_image.setImageResource(bookmarkItem.getImage());
        }

    }
//    public TravelItem getItem(int position){
//        return items.get(position);
//    }
}