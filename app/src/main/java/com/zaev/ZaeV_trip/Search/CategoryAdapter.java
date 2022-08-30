package com.zaev.ZaeV_trip.Search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.util.ItemTouchHelperListener;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemViewHolder>
        implements ItemTouchHelperListener {

    ArrayList<RestaurantCategoryItem> items = new ArrayList<>();

    public CategoryAdapter(ArrayList<RestaurantCategoryItem> items){
        this.items = items;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater를 이용해서 원하는 레이아웃을 띄워줌
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_restaurant, parent, false);
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

    public void addItem(RestaurantCategoryItem categoryRestaurant){
        items.add(categoryRestaurant);
    }



    @Override
    public boolean onItemMove(int from_position, int to_position) {
        //이동할 객체 저장
        RestaurantCategoryItem restaurantCategoryItem = items.get(from_position);
        //이동할 객체 삭제
        items.remove(from_position);
        //이동하고 싶은 position에 추가
        items.add(to_position, restaurantCategoryItem);

        //Adapter에 데이터 이동알림
        notifyItemMoved(from_position,to_position);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView list_name, list_menu;
        ImageView list_image;

        public ItemViewHolder(View itemView) {
            super(itemView);
            list_name = (TextView) itemView.findViewById(R.id.list_name);
            list_menu = (TextView) itemView.findViewById(R.id.list_menu);
            list_image = itemView.findViewById(R.id.list_image);
        }

        public void onBind(RestaurantCategoryItem categoryRestaurant) {
            list_name.setText(categoryRestaurant.getName());
            list_menu.setText(categoryRestaurant.getMenu());
            list_image.setImageResource(categoryRestaurant.getImage());
        }
    }
}
