package com.example.ZaeV_trip.Lodging;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ZaeV_trip.Lodging.LodgingActivity;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.Lodging;

import java.util.ArrayList;

public class LodgingAdapter extends BaseAdapter implements Filterable {
    Context context;
    LayoutInflater inflater;

    ArrayList<Lodging> lodgings = new ArrayList<>();
    ArrayList<Lodging> filteredlodgingsList = lodgings ;

    Filter listFilter;

    public LodgingAdapter() {

    }

    public LodgingAdapter(LodgingActivity touristSpotActivity) {
        this.context = touristSpotActivity;
    }

    public LodgingAdapter(LodgingActivity touristSpotActivity, Lodging lodgings) {
        this.context = touristSpotActivity;
        this.lodgings.add(lodgings);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Lodging item) {
        lodgings.add(item);
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return filteredlodgingsList.size() ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return filteredlodgingsList.get(position) ;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null){
            view = inflater.inflate(R.layout.item_lodging,null);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView imageView = view.findViewById(R.id.list_image);
        TextView nameview = view.findViewById(R.id.list_name);
        TextView locview = view.findViewById(R.id.list_location);
        //TextView catview = view.findViewById(R.id.list_date);

        Glide.with(view)
                .load(lodgings.get(position).getFirstImage())
                .placeholder(R.drawable.default_profile_image)
                .error(R.drawable.default_profile_image)
                .fallback(R.drawable.default_profile_image)
                .into(imageView);

        // Data Set(filteredlodgingsList)에서 position에 위치한 데이터 참조 획득
        Lodging lodging = filteredlodgingsList.get(position);

        nameview.setText(lodgings.get(position).getTitle());
        locview.setText(lodgings.get(position).getAddr1());
        //catview.setText(lodgings.get(position).getStartDate()+"~"+lodgings.get(position).getEndDate());

        return view;
    }

    
    @Override
    public Filter getFilter() {
        if (listFilter == null) {
            listFilter = new ListFilter() ;
        }

        return listFilter ;
    }

    private class ListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults() ;

            if (constraint == null || constraint.length() == 0) {
                results.values = lodgings ;
                results.count = lodgings.size() ;
            } else {
                ArrayList<Lodging> itemList = new ArrayList<Lodging>() ;

                for (Lodging item : lodgings) {
                    if (item.getTitle().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                            item.getAddr1().toUpperCase().contains(constraint.toString().toUpperCase()))
                    {
                        itemList.add(item) ;
                    }
                }

                results.values = itemList ;
                results.count = itemList.size() ;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            // update listview by filtered data list.
            filteredlodgingsList = (ArrayList<Lodging>) results.values ;

            // notify
            if (results.count > 0) {
                notifyDataSetChanged() ;
            } else {
                notifyDataSetInvalidated() ;
            }
        }
    }
}

