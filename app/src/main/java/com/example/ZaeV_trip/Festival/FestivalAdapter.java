package com.example.ZaeV_trip.Festival;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ZaeV_trip.Cafe.CafeAdapter;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.TouristSpot.TouristSpotActivity;
import com.example.ZaeV_trip.model.Cafe;
import com.example.ZaeV_trip.model.Festival;
import com.example.ZaeV_trip.model.TouristSpot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FestivalAdapter extends RecyclerView.Adapter<FestivalAdapter.ViewHolder> implements Filterable {
    Context context;
    LayoutInflater inflater;

    ArrayList<Festival> festivals;
    ArrayList<Festival> filtered;

    FestivalFilter mfilter = new FestivalFilter();

    FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public FestivalAdapter() {

    }

    public FestivalAdapter(FestivalActivity festivalActivity, ArrayList<Festival> festivals) {
        this.context = festivalActivity;
        this.filtered = festivals;
        this.festivals = new ArrayList<Festival>(festivals);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_festival,parent,false);
        ViewHolder viewHolder = new ViewHolder(context, view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull FestivalAdapter.ViewHolder holder, int position) {
        holder.nameview.setText(festivals.get(position).getTitle());
        holder.locview.setText(festivals.get(position).getAddr1());
        holder.catview.setText(festivals.get(position).getStartDate()+"~"+festivals.get(position).getEndDate());

        Glide.with(context)
                .load(festivals.get(position).getFirstImage())
                .placeholder(R.drawable.default_profile_image)
                .error(R.drawable.default_profile_image)
                .fallback(R.drawable.default_profile_image)
                .into(holder.imageView);

        mDatabase.collection("BookmarkItem").document(userId).collection("festival").document(filtered.get(position).getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
        return mfilter;
    }

    private class FestivalFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            ArrayList<Festival> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(festivals);
            } else {
                String filterString = charSequence.toString().toLowerCase();
                String filterableString;
                for (int i = 0; i < festivals.size(); i++) {
                    filterableString = festivals.get(i).getTitle();
                    if (filterableString.contains(filterString)) {
                        filteredList.add(festivals.get(i));
                    }
                }

            }
            results.values = filteredList;

            return results;
        }


        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filtered.clear();
            filtered.addAll((ArrayList<Festival>) filterResults.values);
            notifyDataSetChanged();
        }
    }
    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(FestivalAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }


    public class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView nameview;
        public TextView locview;
        public TextView catview;
        public ImageView bookmarkbtn;

        public ViewHolder(Context context, @NonNull View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.list_image);
            nameview = itemView.findViewById(R.id.list_name);
            locview = itemView.findViewById(R.id.list_location);
            catview = itemView.findViewById(R.id.list_date);

            bookmarkbtn = itemView.findViewById(R.id.bookmarkBtn);

            bookmarkbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bookmarkbtn.setActivated(!bookmarkbtn.isActivated());
                    int pos = getAdapterPosition();

                    if (!bookmarkbtn.isActivated()) {
                        deleteBookmark(pos);

                    } else {
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
        private void writeBookmark(int position) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Map<String, Object> info = new HashMap<>();
            info.put("name", filtered.get(position).getTitle());
            info.put("img", filtered.get(position).getFirstImage());
            info.put("address", filtered.get(position).getAddr1());
            info.put("position_x", filtered.get(position).getMapX());
            info.put("position_y", filtered.get(position).getMapY());
            info.put("serialNumber", filtered.get(position).getId());
            info.put("start_date", filtered.get(position).getStartDate());
            info.put("end_date", filtered.get(position).getEndDate());

            mDatabase.collection("BookmarkItem").document(userId).collection("festival").document(filtered.get(position).getId()).set(info);
        }

        private void deleteBookmark(int position) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Map<String, Object> info = new HashMap<>();
            mDatabase.collection("BookmarkItem").document(userId).collection("festival").document(filtered.get(position).getId()).delete();
        }

    }
}

