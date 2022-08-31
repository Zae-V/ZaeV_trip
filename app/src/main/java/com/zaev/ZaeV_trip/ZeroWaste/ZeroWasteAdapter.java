package com.zaev.ZaeV_trip.ZeroWaste;

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
import com.zaev.ZaeV_trip.model.ZeroWaste;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ZeroWasteAdapter extends RecyclerView.Adapter<com.zaev.ZaeV_trip.ZeroWaste.ZeroWasteAdapter.ViewHolder> implements Filterable {
    com.zaev.ZaeV_trip.ZeroWaste.ZeroWasteAdapter.ZeroWasteFilter filter = new com.zaev.ZaeV_trip.ZeroWaste.ZeroWasteAdapter.ZeroWasteFilter();
    Context context;
    LayoutInflater inflater;
    ArrayList<ZeroWaste> zeroWastes;
    ArrayList<ZeroWaste> filtered;
    FirebaseFirestore mDatabase =FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public ZeroWasteAdapter() {

    }


    public ZeroWasteAdapter(Context context, ArrayList<ZeroWaste> zeroWastes) {
        this.context = context;
        this.zeroWastes = new ArrayList<>(zeroWastes);
        this.filtered = zeroWastes;
    }

    @NonNull
    @Override
    public com.zaev.ZaeV_trip.ZeroWaste.ZeroWasteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_zero_waste, parent, false);
        com.zaev.ZaeV_trip.ZeroWaste.ZeroWasteAdapter.ViewHolder viewHolder = new com.zaev.ZaeV_trip.ZeroWaste.ZeroWasteAdapter.ViewHolder(context, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull com.zaev.ZaeV_trip.ZeroWaste.ZeroWasteAdapter.ViewHolder holder, int position) {

        String imageUrl = filtered.get(position).getImage();

        Glide.with(holder.itemView.getContext())
                .load("https://map.seoul.go.kr" + imageUrl)
                .placeholder(R.drawable.default_bird_img)
                .error(R.drawable.default_bird_img)
                .fallback(R.drawable.default_bird_img)
                .into(holder.imgView);

        holder.nameview.setText(filtered.get(position).getName());
        holder.locview.setText(filtered.get(position).getAddr1());

        String theme_sub_id = String.valueOf(filtered.get(position).getThemeSubID());
        switch (theme_sub_id) {
            case "1":
                holder.catview.setText("카페");
                break;
            case "2":
                holder.catview.setText("식당");
                break;
            case "3":
                holder.catview.setText("리필샵");
                break;
            case "4":
                holder.catview.setText("친환경생필품점");
                break;
            case "5":
                holder.catview.setText("기타");
                break;
            default:
                holder.catview.setText("");
                break;
        }
        
        mDatabase.collection("BookmarkItem").document(userId).collection("zeroWaste").document(filtered.get(position).getName()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

    public void clear() {
        zeroWastes.clear();
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class ZeroWasteFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            ArrayList<ZeroWaste> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(zeroWastes);
            } else {
                String filterString = charSequence.toString().toLowerCase();
                String filterableString;
                for (int i = 0; i < zeroWastes.size(); i++) {
                    filterableString = zeroWastes.get(i).getName();
                    if (filterableString.contains(filterString)) {
                        filteredList.add(zeroWastes.get(i));
                    }
                }

            }
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filtered.clear();
            filtered.addAll((ArrayList<ZeroWaste>) filterResults.values);
            if(filtered.size() == 0) {
                ZeroWasteActivity.notDataImage.setVisibility(View.VISIBLE);
                ZeroWasteActivity.notDataText.setVisibility(View.VISIBLE);
            }
            notifyDataSetChanged();

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private com.zaev.ZaeV_trip.ZeroWaste.ZeroWasteAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(com.zaev.ZaeV_trip.ZeroWaste.ZeroWasteAdapter.OnItemClickListener listener) {
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
        Map<String, Object> info = new HashMap<>();
        info.put("name", filtered.get(position).getName());
        info.put("type", "제로웨이스트");
        info.put("address", filtered.get(position).getAddr1());
        info.put("position_x", filtered.get(position).getMapX());
        info.put("position_y", filtered.get(position).getMapY());
        info.put("serialNumber", filtered.get(position).getContentID());
        info.put("keyword", filtered.get(position).getTelephone());

        mDatabase.collection("BookmarkItem").document(userId).collection("zeroWaste").document(filtered.get(position).getName()).set(info);
    }
    private void deleteBookmark(int position){
        mDatabase.collection("BookmarkItem").document(userId).collection("zeroWaste").document(filtered.get(position).getName()).delete();
    }
}
