package com.zaev.ZaeV_trip.Restaurant;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.model.Restaurant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
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
    FirebaseFirestore mDatabase =FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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

        holder.nameview.setText(filtered.get(position).getName());
        holder.locview.setText(filtered.get(position).getLocation());
        holder.catview.setText(filtered.get(position).getCategory());

        mDatabase.collection("BookmarkItem").document(userId).collection("restaurant").document(filtered.get(position).getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        holder.bookmarkbtn.setActivated(true);
                    } else {
                        holder.bookmarkbtn.setActivated(false);
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
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
                    filterableString = restaurants.get(i).getName();
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

            if(filtered.size() ==0) {
                RestaurantActivity.notDataImage.setVisibility(View.VISIBLE);
                RestaurantActivity.notDataText.setVisibility(View.VISIBLE);
            }
            Log.d("어댑터 테스트 필터", String.valueOf(filtered.size()));

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
        public ImageView bookmarkbtn;

        public ViewHolder(Context context, @NonNull View itemView) {
            super(itemView);

            nameview = itemView.findViewById(R.id.plogging_name);
            locview = itemView.findViewById(R.id.plogging_address);
            catview = itemView.findViewById(R.id.plogging_level);
            bookmarkbtn = itemView.findViewById(R.id.bookmarkBtn);

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
        Map<String, Object> info = new HashMap<>();
        info.put("name", filtered.get(position).getName());
        info.put("type", "식당");
        info.put("address", filtered.get(position).getLocation());
        info.put("position_x", filtered.get(position).getMapX());
        info.put("position_y", filtered.get(position).getMapY());
        info.put("serialNumber", filtered.get(position).getId());
        info.put("tel", filtered.get(position).getNumber());
        info.put("menu", filtered.get(position).getMenu());
        info.put("category", filtered.get(position).getCategory());

        mDatabase.collection("BookmarkItem").document(userId).collection("restaurant").document(filtered.get(position).getId()).set(info);
    }
    private void deleteBookmark(int position){
        mDatabase.collection("BookmarkItem").document(userId).collection("restaurant").document(filtered.get(position).getId()).delete();
    }
}
