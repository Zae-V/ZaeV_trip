package com.example.ZaeV_trip.Restaurant;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.Restaurant;
import com.example.ZaeV_trip.model.TouristSpotDetail;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class RestaurantFragment extends Fragment {
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    RecyclerView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_restaurant, container, false);
        list = (RecyclerView) v.findViewById(R.id.veganMenuList);

        //Bundle
        String name = getArguments().getString("name");
        String location = getArguments().getString("location");
        String category = getArguments().getString("category");
        String x = getArguments().getString("x");
        String y = getArguments().getString("y");
        String number = getArguments().getString("number");
        String menu = getArguments().getString("menu");


        //xml
        TextView list_name = v.findViewById(R.id.menuName);

//        TextView content = v.findViewById(R.id.summary_content_txt);

//        list_name.setText(name);

//        content.setText("☎ : " + number +"\n" + "제공 메뉴 : " + menu);
        Restaurant restaurant = null;
        restaurant = new Restaurant("","","","","","","","","");
        restaurant.setMenu(menu);
        restaurants.add(restaurant);

        RestaurantMenuAdapter adapter = new RestaurantMenuAdapter(getActivity(), restaurants);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        list.setAdapter(adapter);
        //Map View
//        MapView mapView = new MapView(getActivity());
//
//        Float coor_x = Float.valueOf(x);
//        Float coor_y = Float.valueOf(y);
//
//        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(coor_y,coor_x),2,true);
//
//        MapPOIItem marker = new MapPOIItem();
//        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(coor_y,coor_x));
//        marker.setItemName(name);
//        marker.setTag(0);
//        marker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin);
//        mapView.addPOIItem(marker);
//
//        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.mapView);
//        mapViewContainer.addView(mapView);


        return v;
    }
}
