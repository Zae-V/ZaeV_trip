package com.zaev.ZaeV_trip.Cafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.model.Cafe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CafeMenuAdapter extends RecyclerView.Adapter<CafeMenuAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    ArrayList<Cafe> cafes;
    FirebaseFirestore mDatabase =FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public CafeMenuAdapter() {

    }

    public CafeMenuAdapter(Context context, ArrayList<Cafe> cafes) {
        this.context = context;
        this.cafes = new ArrayList<>(cafes);
    }

    public CafeMenuAdapter(CafeFragment cafeFragment, ArrayList<Cafe> cafes) {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_vegan_menu, parent, false);
        ViewHolder viewHolder = new ViewHolder(context, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameview.setText(cafes.get(position).getMenu());
    }

    @Override
    public int getItemCount() {
        return cafes.size();
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

        public ViewHolder(Context context, @NonNull View itemView) {
            super(itemView);

            nameview = itemView.findViewById(R.id.menuName);
        }
    }
}
