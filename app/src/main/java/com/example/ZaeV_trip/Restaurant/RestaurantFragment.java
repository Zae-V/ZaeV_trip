package com.example.ZaeV_trip.Restaurant;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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
    ArrayList<Restaurant> vegans = new ArrayList<>();
    ArrayList<Restaurant> lactos = new ArrayList<>();
    ArrayList<Restaurant> ovos = new ArrayList<>();
    ArrayList<Restaurant> lactoOvos = new ArrayList<>();
    ArrayList<Restaurant> pescos = new ArrayList<>();

    RecyclerView veganList;
    RecyclerView lactoList;
    RecyclerView ovoList;
    RecyclerView lactoOvoList;
    RecyclerView pescoList;

    TextView veganText;
    TextView lactoText;
    TextView ovoText;
    TextView lactoOvoText;
    TextView pescoText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_restaurant, container, false);
        veganList = (RecyclerView) v.findViewById(R.id.veganMenuList);
        lactoList = (RecyclerView) v.findViewById(R.id.lactoMenuList);
        ovoList = (RecyclerView) v.findViewById(R.id.ovoMenuList);
        lactoOvoList = (RecyclerView) v.findViewById(R.id.lactoOvoMenuList);
        pescoList = (RecyclerView) v.findViewById(R.id.pescoMenuList);

        veganText = v.findViewById(R.id.veganMenuText);
        lactoText = v.findViewById(R.id.lactoMenuText);
        ovoText = v.findViewById(R.id.ovoMenuText);
        lactoOvoText = v.findViewById(R.id.lactoOvoMenuText);
        pescoText = v.findViewById(R.id.pescoMenuText);

        veganText.setVisibility(v.GONE);
        lactoText.setVisibility(v.GONE);
        ovoText.setVisibility(v.GONE);
        lactoOvoText.setVisibility(v.GONE);
        pescoText.setVisibility(v.GONE);

        Boolean isVegan = false;
        Boolean isLacto = false;
        Boolean isOvo = false;
        Boolean isLactoOvo = false;
        Boolean isPesco = false;

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

        String[] strArr = menu.split(", ");
        for(int i=0; i<strArr.length; i++){
            restaurant = new Restaurant("","","","","","","","","");
            if (strArr[i].contains("비건")){
                restaurant.setMenu(strArr[i]);
                Log.d("테스트", restaurant.getMenu());
                vegans.add(restaurant);
                isVegan = true;
            }
            else if(strArr[i].contains("락토") && !strArr[i].contains("오보")){
                restaurant.setMenu(strArr[i]);
                Log.d("테스트", restaurant.getMenu());
                lactos.add(restaurant);
                isLacto = true;
            }
            else if(strArr[i].contains("오보") && !strArr[i].contains("락토")){
                restaurant.setMenu(strArr[i]);
                Log.d("테스트", restaurant.getMenu());
                ovos.add(restaurant);
                isOvo = true;
            }
            else if (strArr[i].contains("락토오보")) {
                restaurant.setMenu(strArr[i]);
                Log.d("테스트", restaurant.getMenu());
                ovos.add(restaurant);
                isLactoOvo = true;
            }
            else if(strArr[i].contains("페스코")){
                restaurant.setMenu(strArr[i]);
                Log.d("테스트", restaurant.getMenu());
                pescos.add(restaurant);
                isPesco = true;
            }
        }
        if(isVegan) {
            RestaurantMenuAdapter adapter = new RestaurantMenuAdapter(getActivity(), vegans);
            veganList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            veganList.setAdapter(adapter);
            veganText.setVisibility(v.VISIBLE);
        }
        if(isLacto) {
            RestaurantMenuAdapter adapter = new RestaurantMenuAdapter(getActivity(), lactos);
            lactoList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            lactoList.setAdapter(adapter);
            lactoText.setVisibility(v.VISIBLE);
        }
        if(isOvo) {
            RestaurantMenuAdapter adapter = new RestaurantMenuAdapter(getActivity(), ovos);
            ovoList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            ovoList.setAdapter(adapter);
            ovoText.setVisibility(v.VISIBLE);
        }
        if(isLactoOvo) {
            RestaurantMenuAdapter adapter = new RestaurantMenuAdapter(getActivity(), lactoOvos);
            lactoOvoList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            lactoOvoList.setAdapter(adapter);
            lactoOvoText.setVisibility(v.VISIBLE);
        }
        if(isPesco) {
            RestaurantMenuAdapter adapter = new RestaurantMenuAdapter(getActivity(), pescos);
            pescoList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            pescoList.setAdapter(adapter);
            pescoText.setVisibility(v.VISIBLE);
        }


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
