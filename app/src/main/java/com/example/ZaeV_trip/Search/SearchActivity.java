package com.example.ZaeV_trip.Search;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.ZaeV_trip.BookmarkActivity;
import com.example.ZaeV_trip.MainActivity;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Schedule.TravelActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button spinnerBtn = findViewById(R.id.spinnerBtn);

        final Observer<String> selectObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String s) {
                spinnerBtn.setText(s);
            }
        };

        SharedViewModel searchViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        searchViewModel.getMessage().observe(this, selectObserver);


        spinnerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.search_container,new SearchFragment()).commit();
            }
        });

        SearchView searchBar = findViewById(R.id.searchBar);
        searchBar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                searchBar.setIconified(false);
            }
        });
//
//        Button spotBtn = findViewById(R.id.spotBtn);
//        spotBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(this, PlaceActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        Button ploggingBtn = findViewById(R.id.ploggingBtn);
//        ploggingBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(this, PloggingActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        Button restaurantBtn = findViewById(R.id.restaurantBtn);
//        restaurantBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent intent = new Intent(getActivity(), VeganActivity.class);
////                startActivity(intent);
//                activity.changeFragment(8);
//                //Go to Category_Restaurant
//
//            }
//        });
//
//        // 임시로 연결 설정, 후에 수정사항
//        Button allBtn = findViewById(R.id.allBtn);
//        allBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                activity.changeFragment(9);
//            }
//        });
////
//        // 임시로 연결 설정, 후에 수정사항
//        Button shopBtn = findViewById(R.id.shopBtn);
//        shopBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //activity.changeFragment();
//            }
//        });


        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.search);


        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.search:

                    return true;
                case R.id.bookmark:
                    startActivity(new Intent(getApplicationContext(), BookmarkActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.travel:
                    startActivity(new Intent(getApplicationContext(), TravelActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });
    }
}