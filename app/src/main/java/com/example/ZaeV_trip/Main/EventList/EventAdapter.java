package com.example.ZaeV_trip.Main.EventList;

import android.content.Context;
import android.util.AttributeSet;
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
import com.example.ZaeV_trip.Festival.FestivalActivity;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.Festival;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;

    ArrayList<Festival> eventLists;

    public EventAdapter() {

    }

    public EventAdapter(Context context, ArrayList<Festival> eventLists) {
        this.context = context;
        this.eventLists = new ArrayList<>(eventLists);
    }


    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_event_list,parent,false);
        EventAdapter.ViewHolder viewHolder = new EventAdapter.ViewHolder(context, view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        String eventName = eventLists.get(position).getTitle();
        holder.nameview.setText(eventName);
        String startDate = eventLists.get(position).getStartDate().substring(4,6) +"."+ eventLists.get(position).getStartDate().substring(6);
        String endDate = eventLists.get(position).getEndDate().substring(4,6) + "." + eventLists.get(position).getEndDate().substring(6);
        String date = startDate + " ~ " + endDate;
        holder.catview.setText(date);

        Glide.with(context)
                .load(eventLists.get(position).getFirstImage())
                .placeholder(R.drawable.default_bird_img)
                .error(R.drawable.default_bird_img)
                .fallback(R.drawable.default_bird_img)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return eventLists.size();
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private EventAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(EventAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }


    public class ViewHolder extends  RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView nameview;
        public TextView catview;
        public ImageView bookmarkbtn;

        public ViewHolder(Context context, @NonNull View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.eventImage);
            nameview = itemView.findViewById(R.id.eventName);
            catview = itemView.findViewById(R.id.eventDate);

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
}

