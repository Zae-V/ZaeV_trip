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
import com.example.ZaeV_trip.MainActivity;
import com.example.ZaeV_trip.R;
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