package com.example.ZaeV_trip.Plogging;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.Plogging;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PloggingAdapter extends RecyclerView.Adapter<PloggingAdapter.ViewHolder> implements Filterable {
    PloggingFilter filter = new PloggingFilter();
    Context context;
    LayoutInflater inflater;
    ArrayList<Plogging> ploggings;
    ArrayList<Plogging> filtered;
    FirebaseFirestore mDatabase =FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public PloggingAdapter() {

    }


    public PloggingAdapter(Context context, ArrayList<Plogging> ploggings) {
        this.context = context;
        this.ploggings = new ArrayList<>(ploggings);
        this.filtered = ploggings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_plogging, parent, false);
        ViewHolder viewHolder = new ViewHolder(context, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameview.setText(filtered.get(position).getCrsKorNm());
        holder.locview.setText(filtered.get(position).getSigun());
        int level = Integer.parseInt(filtered.get(position).getCrsLevel());
        String lev="";
        String word = "";
        String color = "";
        int term = 2;
        switch (level){
            case 1:
                lev = "난이도 쉬움";
                word = "쉬움";
                color = "#418EE8";
                break;
            case 2:
                lev = "난이도 보통";
                word = "보통";
                color = "#FF9C41";
                break;
            case 3:
                lev = "난이도 어려움";
                word = "어려움";
                color = "#F255A0";
                term = 3;
                break;
        }
        // 글자색 바꾸기
        SpannableString spannableString = new SpannableString(lev);

        int loc = lev.indexOf(word);

        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(color)),
                loc, loc + term, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.catview.setText(spannableString);

        mDatabase.collection("BookmarkItem").document(userId).collection("plogging").document(filtered.get(position).getCrsKorNm()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

    private class PloggingFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            ArrayList<Plogging> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(ploggings);
            } else {
                String filterString = charSequence.toString().toLowerCase();
                String filterableString;
                for (int i = 0; i < ploggings.size(); i++) {
                    filterableString = ploggings.get(i).getCrsKorNm();
                    if (filterableString.contains(filterString)) {
                        filteredList.add(ploggings.get(i));
                    }
                }

            }
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filtered.clear();
            filtered.addAll((ArrayList<Plogging>) filterResults.values);
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
        info.put("name", filtered.get(position).getCrsKorNm());
        info.put("info", filtered.get(position).getCrsContents());
        info.put("type", "플로깅");

        String userId = user.getUid();
        mDatabase.collection("BookmarkItem").document(userId).collection("plogging").document(filtered.get(position).getCrsKorNm()).set(info);
    }
    private void deleteBookmark(int position){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseFirestore.getInstance();
        Map<String, Object> info = new HashMap<>();
        String userId = user.getUid();
        mDatabase.collection("BookmarkItem").document(userId).collection("plogging").document(filtered.get(position).getCrsKorNm()).delete();
    }
}