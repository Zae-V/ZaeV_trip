package com.example.ZaeV_trip.Main.VeganRestaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.VeganRestaurant;

import java.util.ArrayList;

public class VeganRestaurantAdapter extends RecyclerView.Adapter<VeganRestaurantAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    ArrayList<VeganRestaurant> veganRestaurants;
    ArrayList<VeganRestaurant> filtered;

    public VeganRestaurantAdapter() {

    }


    public VeganRestaurantAdapter(Context context, ArrayList<VeganRestaurant> veganRestaurants) {
        this.context = context;
        this.veganRestaurants = new ArrayList<>(veganRestaurants);
        this.filtered = veganRestaurants;
    }

    @NonNull
    @Override
    public VeganRestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_vegan_restaurant, parent, false);
        VeganRestaurantAdapter.ViewHolder viewHolder = new VeganRestaurantAdapter.ViewHolder(context, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VeganRestaurantAdapter.ViewHolder holder, int position) {
        holder.nameview.setText(veganRestaurants.get(position).getName());

        holder.locview.setText(veganRestaurants.get(position).getLocation());

        String menu = veganRestaurants.get(position).getMenu();
        String[] strArr = menu.split(", ");
        String mainMenu = strArr[0];
        String[] strArr2 = mainMenu.split("\\(");
        holder.menuview.setText(strArr2[0]);
    }

    @Override
    public int getItemCount() {
        return veganRestaurants.size();
    }


    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private VeganRestaurantAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(VeganRestaurantAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameview;
        public TextView locview;
        public TextView menuview;

        public ViewHolder(Context context, @NonNull View itemView) {
            super(itemView);

            nameview = itemView.findViewById(R.id.restaurantText);
            locview = itemView.findViewById(R.id.addressText);
            menuview = itemView.findViewById(R.id.menuText);

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
