package com.zaev.ZaeV_trip.TouristSpot;

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

import com.bumptech.glide.Glide;
import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.model.TouristSpot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TouristSpotAdapter extends RecyclerView.Adapter<TouristSpotAdapter.ViewHolder> implements Filterable {
    TouristSpotFilter filter = new TouristSpotFilter();
    Context context;
    LayoutInflater inflater;
    ArrayList<TouristSpot> touristSpots;
    ArrayList<TouristSpot> filtered;
    FirebaseFirestore mDatabase =FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public TouristSpotAdapter() {

    }


    public TouristSpotAdapter(Context context, ArrayList<TouristSpot> touristSpots) {
        this.context = context;
        this.touristSpots = new ArrayList<>(touristSpots);
        this.filtered = touristSpots;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_tourist_spot, parent, false);
        ViewHolder viewHolder = new ViewHolder(context, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(holder.itemView.getContext())
                .load(filtered.get(position).getFirstImage())
                .placeholder(R.drawable.default_bird_img)
                .error(R.drawable.default_bird_img)
                .fallback(R.drawable.default_bird_img)
                .into(holder.imgView);

        holder.nameview.setText(filtered.get(position).getTitle());
        holder.locview.setText(filtered.get(position).getAddr1());
        holder.catview.setText(filtered.get(position).getAddr2());

        mDatabase.collection("BookmarkItem").document(userId).collection("touristSpot").document(filtered.get(position).getContentID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

    private class TouristSpotFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            ArrayList<TouristSpot> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(touristSpots);
            } else {
                String filterString = charSequence.toString().toLowerCase();
                String filterableString;
                for (int i = 0; i < touristSpots.size(); i++) {
                    filterableString = touristSpots.get(i).getTitle();
                    if (filterableString.contains(filterString)) {
                        filteredList.add(touristSpots.get(i));
                    }
                }

            }
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filtered.clear();
            filtered.addAll((ArrayList<TouristSpot>) filterResults.values);

            if(filtered.size() == 0) {
                TouristSpotActivity.notDataImage.setVisibility(View.VISIBLE);
                TouristSpotActivity.notDataText.setVisibility(View.VISIBLE);
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

            nameview = itemView.findViewById(R.id.list_name);
            locview = itemView.findViewById(R.id.list_location);
            catview = itemView.findViewById(R.id.list_category);

            bookmarkbtn = itemView.findViewById(R.id.bookmarkBtn);
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
        info.put("type", "관광명소");
        info.put("address", filtered.get(position).getAddr1());
        info.put("address2", filtered.get(position).getAddr2());
        info.put("position_x", filtered.get(position).getMapX());
        info.put("position_y", filtered.get(position).getMapY());
        info.put("serialNumber", filtered.get(position).getContentID());
        info.put("image", filtered.get(position).getFirstImage());

        String userId = user.getUid();
        mDatabase.collection("BookmarkItem").document(userId).collection("touristSpot").document(filtered.get(position).getContentID()).set(info);
    }
    private void deleteBookmark(int position){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseFirestore.getInstance();
        Map<String, Object> info = new HashMap<>();
        String userId = user.getUid();
        mDatabase.collection("BookmarkItem").document(userId).collection("touristSpot").document(filtered.get(position).getContentID()).delete();
    }
}
