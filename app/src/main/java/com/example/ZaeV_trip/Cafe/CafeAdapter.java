package com.example.ZaeV_trip.Cafe;

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

import java.util.ArrayList;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.Cafe;


public class CafeAdapter extends RecyclerView.Adapter<CafeAdapter.ViewHolder> implements Filterable {
    CafeFilter mfilter = new CafeFilter();
    Context context;
    LayoutInflater inflater;
    ArrayList<Cafe> cafes ;
    ArrayList<Cafe> filtered;


    public CafeAdapter() {

    }

    public CafeAdapter(Context context, ArrayList<Cafe> cafes){
        this.context = context;
        this.cafes = new ArrayList<>(cafes);
        this.filtered = cafes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_cafe,parent,false);
        ViewHolder viewHolder = new ViewHolder(context, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.nameview.setText(filtered.get(position).getName());
        holder.locview.setText(filtered.get(position).getLocation());
        holder.catview.setText(filtered.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }


    @Override
    public Filter getFilter() {
        return mfilter;
    }

    private class CafeFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            ArrayList<Cafe> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(cafes);
            }else{
                String filterString = charSequence.toString().toLowerCase();
                String filterableString;
                for(int i = 0 ; i < cafes.size(); i++){
                    filterableString = cafes.get(i).getName();
                    if(filterableString.contains(filterString)){
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
            filtered.addAll((ArrayList<Cafe>)filterResults.values);
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener{
        void  onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameview;
        public TextView locview;
        public TextView catview;

        public ViewHolder(Context context, @NonNull View itemView) {
            super(itemView);

            nameview = itemView.findViewById(R.id.list_name);
            locview = itemView.findViewById(R.id.list_location);
            catview = itemView.findViewById(R.id.list_category);

            ImageView bookmarkbtn = itemView.findViewById(R.id.bookmarkBtn);


            bookmarkbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bookmarkbtn.setActivated(!bookmarkbtn.isActivated());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(view,pos);
                        }
                    }
                }
            });


        }


    }
}


