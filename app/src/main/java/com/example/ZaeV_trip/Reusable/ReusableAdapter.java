package com.example.ZaeV_trip.Reusable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Schedule.TravelListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReusableAdapter extends ArrayAdapter<ReusableItem> {
    private static final int LAYOUT_RESOURCE_ID = R.layout.item_cafe;

    LayoutInflater inflater;

    ArrayList<String> name;
    ArrayList<String> location;
    ArrayList<String> reason;

    private Context mContext;
    private List<ReusableItem> mItemList;
    private ReusableActivity.ReusableItemClickListener reusableItemClickListener;

    public ReusableAdapter(Context a_context, List<ReusableItem> a_itemList) {
        super(a_context, LAYOUT_RESOURCE_ID, a_itemList);

        mContext = a_context;
        mItemList = a_itemList;
    }

//    public ReusableAdapter(ReusableActivity reusableActivity, ArrayList<String> names, ArrayList<String> locations, ArrayList<String> reasons) {
//        this.context = reusableActivity;
//        this.name = names;
//        this.location = locations;
//        this.reason = reasons;
//    }
//
//    @Override
//    public int getCount() {
//        return name.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }

    @Override
    public View getView(int a_position, View view, ViewGroup viewGroup) {
        ReusableViewHolder viewHolder;

        if(view == null) {
            view = LayoutInflater.from(mContext).inflate(LAYOUT_RESOURCE_ID, viewGroup, false);

            viewHolder = new ReusableViewHolder(view);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ReusableViewHolder) view.getTag();
        }

        final ReusableItem reusableItem = mItemList.get(a_position);

        viewHolder.listName.setText(reusableItem.getName());
        viewHolder.listLocation.setText(reusableItem.getLocation());
        viewHolder.listReason.setText(reusableItem.getReason());

        viewHolder.listName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                if(reusableItemClickListener != null){
                    reusableItemClickListener.onReusableItemClick(reusableItem.getName());
                }
            }
        });

     /*   view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(this, )
            }
        });*/

//        if(view == null){
//            view = inflater.inflate(R.layout.item_cafe,null);
//        }
//        TextView nameview = view.findViewById(R.id.list_name);
//        TextView locview = view.findViewById(R.id.list_location);
//        TextView catview = view.findViewById(R.id.list_category);
//
//        nameview.setText(name.get(i));
//        locview.setText(location.get(i));
//        catview.setText(reason.get(i));

        return view;
    }

    public void setReusableItemClickListener(ReusableActivity.ReusableItemClickListener a_listener){
        reusableItemClickListener = a_listener;
    }

}
