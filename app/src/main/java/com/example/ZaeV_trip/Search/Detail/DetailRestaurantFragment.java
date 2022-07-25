package com.example.ZaeV_trip.Search.Detail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Search.Category.CategoryAdapter;
import com.example.ZaeV_trip.Search.Category.RestaurantCategoryItem;

import java.util.ArrayList;

public class DetailRestaurantFragment extends Fragment {

    private ArrayList<RestaurantCategoryItem> restaurantsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;


    public static DetailRestaurantFragment newInstance() {
        return new DetailRestaurantFragment();
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