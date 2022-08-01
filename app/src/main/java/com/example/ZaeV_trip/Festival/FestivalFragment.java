package com.example.ZaeV_trip.Festival;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ZaeV_trip.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class FestivalFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_festival, container, false);

        //Bundle
        String name = getArguments().getString("name");
        String location = getArguments().getString("location");
        String startDate = getArguments().getString("startDate");
        String endDate = getArguments().getString("endDate");
        String x = getArguments().getString("x");
        String y = getArguments().getString("y");
        String img = getArguments().getString("img");


        //xml
        TextView list_name = v.findViewById(R.id.list_name);
        TextView list_location = v.findViewById(R.id.list_location);
        TextView list_category = v.findViewById(R.id.list_date);

        ImageView image = v.findViewById(R.id.list_image);


        list_name.setText(name);
        list_location.setText(location);
        list_category.setText(startDate + "~" + endDate);


        Glide.with(v)
                .load(img)
                .placeholder(R.drawable.default_profile_image)
                .error(R.drawable.default_profile_image)
                .fallback(R.drawable.default_profile_image)
                .into(image);


        //Map View
        MapView mapView = new MapView(getActivity());

        Float coor_x = Float.valueOf(x);
        Float coor_y = Float.valueOf(y);

        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(coor_y,coor_x),2,true);

        MapPOIItem marker = new MapPOIItem();
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(coor_y,coor_x));
        marker.setItemName(name);
        marker.setTag(0);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin);
        mapView.addPOIItem(marker);

        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.mapView);
        mapViewContainer.addView(mapView);



        return v;
    }
}