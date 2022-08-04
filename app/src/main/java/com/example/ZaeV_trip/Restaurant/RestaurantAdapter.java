package com.example.ZaeV_trip.Restaurant;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ZaeV_trip.Cafe.CafeAdapter;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.TouristSpot.TouristSpotActivity;
import com.example.ZaeV_trip.model.Cafe;
import com.example.ZaeV_trip.model.Restaurant;
import com.example.ZaeV_trip.model.TouristSpot;
import com.example.ZaeV_trip.util.MySharedPreferences;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> implements Filterable {
    RestaurantFilter filter = new RestaurantFilter();
    Context context;
    LayoutInflater inflater;
    ArrayList<Restaurant> restaurants;
    ArrayList<Restaurant> filtered;
    FirebaseFirestore mDatabase;

    public RestaurantAdapter() {

    }


    public RestaurantAdapter(Context context, ArrayList<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = new ArrayList<>(restaurants);
        this.filtered = restaurants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_cafe, parent, false);
        ViewHolder viewHolder = new ViewHolder(context, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(holder.itemView.getContext())
                .load(restaurants.get(position).getFirstImage())
                .placeholder(R.drawable.default_profile_image)
                .error(R.drawable.default_profile_image)
                .fallback(R.drawable.default_profile_image)
                .into(holder.imgView);
        holder.nameview.setText(filtered.get(position).getTitle());
        holder.locview.setText(filtered.get(position).getAddr1());
        holder.catview.setText(filtered.get(position).getAddr2());
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class RestaurantFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            ArrayList<Restaurant> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(restaurants);
            } else {
                String filterString = charSequence.toString().toLowerCase();
                String filterableString;
                for (int i = 0; i < restaurants.size(); i++) {
                    filterableString = restaurants.get(i).getTitle();
                    if (filterableString.contains(filterString)) {
                        filteredList.add(restaurants.get(i));
                    }
                }

            }
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filtered.clear();
            filtered.addAll((ArrayList<Restaurant>) filterResults.values);
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameview;
        public TextView locview;
        public TextView catview;
        public ImageView imgView;

        public ViewHolder(Context context, @NonNull View itemView) {
            super(itemView);

            nameview = itemView.findViewById(R.id.list_name);
            locview = itemView.findViewById(R.id.list_location);
            catview = itemView.findViewById(R.id.list_category);

            ImageView bookmarkbtn = itemView.findViewById(R.id.bookmarkBtn);
            imgView = itemView.findViewById(R.id.list_image);


            bookmarkbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bookmarkbtn.setActivated(!bookmarkbtn.isActivated());
                    int pos = getAdapterPosition();
                    if (!bookmarkbtn.isActivated()){
                        // 취소 동작
                        deleteBookmark(pos);
                    }
                    else if(bookmarkbtn.isActivated()){
                        // 선택 동작
                        writeBookmark(pos);
                    }

                }
            });

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

    private void writeBookmark(int position){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseFirestore.getInstance();
        Map<String, Object> info = new HashMap<>();
        info.put("name", filtered.get(position).getTitle());
        info.put("type", "식당");
        info.put("address", filtered.get(position).getAddr1());
        info.put("position_x", filtered.get(position).getMapX());
        info.put("position_y", filtered.get(position).getMapY());
        info.put("serialNumber", filtered.get(position).getContentID());
        info.put("image", filtered.get(position).getFirstImage());
        info.put("tel", filtered.get(position).getNumber());

        String userId = user.getUid();
        mDatabase.collection("BookmarkItem").document(userId).collection("restaurant").document(filtered.get(position).getContentID()).set(info);
    }
    private void deleteBookmark(int position){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseFirestore.getInstance();
        Map<String, Object> info = new HashMap<>();
        String userId = user.getUid();
        mDatabase.collection("BookmarkItem").document(userId).collection("restaurant").document(filtered.get(position).getContentID()).delete();
    }
}
