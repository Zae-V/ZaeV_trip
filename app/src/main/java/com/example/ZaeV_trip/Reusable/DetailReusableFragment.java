package com.example.ZaeV_trip.Reusable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ZaeV_trip.Search.CategoryAdapter;
import com.example.ZaeV_trip.Search.RestaurantCategoryItem;
import com.example.ZaeV_trip.R;


import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class DetailReusableFragment extends Fragment {
    private ArrayList<RestaurantCategoryItem> restaurantsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;

    public static DetailReusableFragment newInstance() {
        return new DetailReusableFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_detail_restaurant, container, false);

        //recyclerview
        recyclerView = (RecyclerView) v.findViewById(R.id.detailRecyclerView_restaurant);
        recyclerView.setHasFixedSize(true);
        categoryAdapter = new CategoryAdapter(restaurantsList);

        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(categoryAdapter);

        MapView mapView = new MapView(getActivity());

        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.mapView);
        mapViewContainer.addView(mapView);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData();
    }

    //데이터 준비(최종적으로는 동적으로 추가하거나 삭제할 수 있어야 한다. 이 데이터를 어디에 저장할지 정해야 한다.)
    private void prepareData() {
        restaurantsList.add(new RestaurantCategoryItem(R.drawable.vegan_burger,"비건 버거","버섯 버거, 콩 버거"));
        restaurantsList.add(new RestaurantCategoryItem(R.drawable.vegan_burger,"비건 버거","버섯 버거, 콩 버거"));
        restaurantsList.add(new RestaurantCategoryItem(R.drawable.vegan_burger,"비건 버거","버섯 버거, 콩 버거"));
        restaurantsList.add(new RestaurantCategoryItem(R.drawable.vegan_burger,"비건 버거","버섯 버거, 콩 버거"));
        restaurantsList.add(new RestaurantCategoryItem(R.drawable.vegan_burger,"비건 버거","버섯 버거, 콩 버거"));
        restaurantsList.add(new RestaurantCategoryItem(R.drawable.vegan_burger,"비건 버거","버섯 버거, 콩 버거"));
        restaurantsList.add(new RestaurantCategoryItem(R.drawable.vegan_burger,"비건 버거","버섯 버거, 콩 버거"));
        restaurantsList.add(new RestaurantCategoryItem(R.drawable.vegan_burger,"비건 버거","버섯 버거, 콩 버거"));
        restaurantsList.add(new RestaurantCategoryItem(R.drawable.vegan_burger,"비건 버거","버섯 버거, 콩 버거"));
        restaurantsList.add(new RestaurantCategoryItem(R.drawable.vegan_burger,"비건 버거","버섯 버거, 콩 버거"));
    }


}
