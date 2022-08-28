package com.example.ZaeV_trip.Bookmark;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Search.CategoryAdapter;
import com.example.ZaeV_trip.Search.RestaurantCategoryItem;
import com.example.ZaeV_trip.util.ItemTouchHelperListener;

import java.util.ArrayList;

public class BookmarkListAdapter extends RecyclerView.Adapter<BookmarkListAdapter.ItemViewHolder>{

    ArrayList<BookmarkItem> items = new ArrayList<>();
    ArrayList<RestaurantCategoryItem> restaurantsList = new ArrayList<>();
    Context context;

    public BookmarkListAdapter(Context context ,ArrayList<BookmarkItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater를 이용해서 원하는 레이아웃을 띄워줌
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_bookmark, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        //ItemViewHolder가 생성되고 넣어야할 코드들을 넣어준다.
        holder.onBind(items.get(position));

        Glide.with(context)
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
//        items.add(to_position, bookmarkItem);
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

        TextView list_name,list_location,list_hours;
        ImageView list_image;



        public ItemViewHolder(View itemView) {
            super(itemView);
            list_name = itemView.findViewById(R.id.list_name);
            list_location = itemView.findViewById(R.id.list_location);
            list_hours = itemView.findViewById(R.id.list_hours);
            list_image = itemView.findViewById(R.id.list_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Toast.makeText(view.getContext(), items.get(pos).getName(), Toast.LENGTH_SHORT).show();
                        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View popupView = inflater.inflate(R.layout.popup_window,null);

                        // create the popup window
                        int width = LinearLayout.LayoutParams.MATCH_PARENT;
                        int height = LinearLayout.LayoutParams.MATCH_PARENT;
                        boolean focusable = true; // lets taps outside the popup also dismiss it
                        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                        // show the popup window
                        // which view you pass in doesn't matter, it is only used for the window tolken
                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                        TextView name = popupView.findViewById(R.id.list_name);
                        TextView location = popupView.findViewById(R.id.list_location);
                        TextView hours = popupView.findViewById(R.id.list_hours);

                        name.setText(items.get(pos).getName());
                        location.setText(items.get(pos).getLocation());
                        hours.setText(items.get(pos).getHours());

                        ImageView closeBtn = popupView.findViewById(R.id.close_btn);

                        closeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                            }
                        });

                        RecyclerView recyclerView = (RecyclerView) popupView.findViewById(R.id.detailRecyclerView_restaurant);
                        recyclerView.setHasFixedSize(true);
                        CategoryAdapter categoryAdapter = new CategoryAdapter(restaurantsList);

                        restaurantsList.add(new RestaurantCategoryItem(R.drawable.vegan_burger,"비건 버거","버섯 버거, 콩 버거"));
                        restaurantsList.add(new RestaurantCategoryItem(R.drawable.vegan_burger,"비건 버거","버섯 버거, 콩 버거"));

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(categoryAdapter);
                    }
                }
            });
        }

        public void onBind(BookmarkItem bookmarkItem) {
            list_name.setText(bookmarkItem.getName());
            list_location.setText(String.valueOf(bookmarkItem.getLocation()));
            list_hours.setText(String.valueOf(bookmarkItem.getHours()));
        }
    }
}