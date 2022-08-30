package com.zaev.ZaeV_trip.Cafe;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.model.Cafe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class CafeAdapter extends RecyclerView.Adapter<CafeAdapter.ViewHolder> implements Filterable {
    CafeFilter mfilter = new CafeFilter();
    Context context;
    LayoutInflater inflater;
    ArrayList<Cafe> cafes;
    ArrayList<Cafe> filtered;

    FirebaseFirestore mDatabase=FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


    public CafeAdapter() {

    }

    public CafeAdapter(Context context, ArrayList<Cafe> cafes) {
        this.context = context;
        this.cafes = new ArrayList<>(cafes);
        this.filtered = cafes;
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

        mDatabase.collection("BookmarkItem").document(userId).collection("cafe").document(filtered.get(position).getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

    private class CafeFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            ArrayList<Cafe> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(cafes);
            } else {
                String filterString = charSequence.toString().toLowerCase();
                String filterableString;
                for (int i = 0; i < cafes.size(); i++) {
                    filterableString = cafes.get(i).getName();
                    if (filterableString.contains(filterString)) {
                        filteredList.add(cafes.get(i));
                    }
                }

            }
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filtered.clear();
            filtered.addAll((ArrayList<Cafe>) filterResults.values);

            if(filtered.size() == 0) {
                CafeActivity.notDataImage.setVisibility(View.VISIBLE);
                CafeActivity.notDataText.setVisibility(View.VISIBLE);
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
            info.put("name", filtered.get(position).getName());
            info.put("type", filtered.get(position).getCategory());
            info.put("address", filtered.get(position).getLocation());
            info.put("position_x", filtered.get(position).getMapX());
            info.put("position_y", filtered.get(position).getMapY());
            info.put("serialNumber", filtered.get(position).getId());
            info.put("tel", filtered.get(position).getNumber());
            info.put("menu", filtered.get(position).getMenu());

            mDatabase.collection("BookmarkItem").document(userId).collection("cafe").document(filtered.get(position).getId()).set(info);
        }

        private void deleteBookmark(int position) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Map<String, Object> info = new HashMap<>();
            mDatabase.collection("BookmarkItem").document(userId).collection("cafe").document(filtered.get(position).getId()).delete();
        }
    }
}

