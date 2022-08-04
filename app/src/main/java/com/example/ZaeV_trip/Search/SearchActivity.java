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


import com.example.ZaeV_trip.Bookmark.BookmarkActivity;
import com.example.ZaeV_trip.Cafe.CafeActivity;
import com.example.ZaeV_trip.Festival.FestivalActivity;
import com.example.ZaeV_trip.Lodging.LodgingActivity;
import com.example.ZaeV_trip.MainActivity;
import com.example.ZaeV_trip.Plogging.PloggingActivity;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Restaurant.RestaurantActivity;
import com.example.ZaeV_trip.Reusable.ReusableActivity;
import com.example.ZaeV_trip.Schedule.TravelActivity;
import com.example.ZaeV_trip.TouristSpot.TouristSpotActivity;
import com.example.ZaeV_trip.util.SharedViewModel;
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
                fragmentTransaction.replace(R.id.search_container,new SearchFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        SearchView searchBar = findViewById(R.id.searchBar);
        searchBar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                searchBar.setIconified(false);
            }
        });

        Button cafeBtn = findViewById(R.id.cafeBtn);
        cafeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, CafeActivity.class);
                intent.putExtra("local",spinnerBtn.getText());
                startActivity(intent);

            }
        });


        Button spotBtn = findViewById(R.id.spotBtn);
        spotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, TouristSpotActivity.class);
                intent.putExtra("local",spinnerBtn.getText());
                startActivity(intent);
            }
        });

        Button reuseBtn = findViewById(R.id.reuseBtn);
        reuseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, ReusableActivity.class);

                intent.putExtra("local",spinnerBtn.getText());
                startActivity(intent);

            }
        });

        Button ploggingBtn = findViewById(R.id.ploggingBtn);
        ploggingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, PloggingActivity.class);

                intent.putExtra("local",spinnerBtn.getText());
                startActivity(intent);

            }
        });

        Button festivalBtn = findViewById(R.id.allBtn);
        festivalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, FestivalActivity.class);
                intent.putExtra("local", spinnerBtn.getText());
                startActivity(intent);
            }
        });

        Button lodgingBtn = findViewById(R.id.hotelBtn);
        lodgingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, LodgingActivity.class);
                intent.putExtra("local", spinnerBtn.getText());
                startActivity(intent);
            }
        });

        Button restaurantBtn = findViewById(R.id.restaurantBtn);
        restaurantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, RestaurantActivity.class);
                intent.putExtra("local", spinnerBtn.getText());
                startActivity(intent);
            }
        });


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