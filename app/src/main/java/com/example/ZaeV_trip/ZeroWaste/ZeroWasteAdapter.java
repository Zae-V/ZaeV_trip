package com.example.ZaeV_trip.ZeroWaste;

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
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.ZeroWaste;
import com.example.ZaeV_trip.model.ZeroWasteDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ZeroWasteAdapter extends RecyclerView.Adapter<ZeroWasteAdapter.ViewHolder> implements Filterable {
    ZeroWasteAdapter.ZeroWasteFilter filter = new ZeroWasteAdapter.ZeroWasteFilter();
    Context context;
    LayoutInflater inflater;
    ArrayList<ZeroWasteDetail> zeroWastes;
    ArrayList<ZeroWasteDetail> filtered;
    FirebaseFirestore mDatabase =FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public ZeroWasteAdapter() {

    }


    public ZeroWasteAdapter(Context context, ArrayList<ZeroWasteDetail> zeroWastes) {
        this.context = context;
        this.zeroWastes = new ArrayList<>(zeroWastes);
        this.filtered = zeroWastes;
    }

    @NonNull
    @Override
    public ZeroWasteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_zero_waste, parent, false);
        ZeroWasteAdapter.ViewHolder viewHolder = new ZeroWasteAdapter.ViewHolder(context, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ZeroWasteAdapter.ViewHolder holder, int position) {

        Glide.with(holder.itemView.getContext())
                .load(filtered.get(position).getImage())
                .placeholder(R.drawable.default_bird_img)
                .error(R.drawable.default_bird_img)
                .fallback(R.drawable.default_bird_img)
                .into(holder.imgView);

        holder.nameview.setText(filtered.get(position).getName());
        holder.locview.setText(filtered.get(position).getAddr1());


        switch (filtered.get(position).getThemeSubID()) {
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
            ArrayList<ZeroWasteDetail> filteredList = new ArrayList<>();

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
            filtered.addAll((ArrayList<ZeroWasteDetail>) filterResults.values);
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private ZeroWasteAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(ZeroWasteAdapter.OnItemClickListener listener) {
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
        info.put("telephone", filtered.get(position).getTelephone());

        mDatabase.collection("BookmarkItem").document(userId).collection("zeroWaste").document(filtered.get(position).getName()).set(info);
    }
    private void deleteBookmark(int position){
        mDatabase.collection("BookmarkItem").document(userId).collection("zeroWaste").document(filtered.get(position).getName()).delete();
    }
}
