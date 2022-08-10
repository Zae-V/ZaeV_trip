package com.example.ZaeV_trip;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.captaindroid.tvg.Tvg;
import com.example.ZaeV_trip.Bookmark.BookmarkActivity;
import com.example.ZaeV_trip.Cafe.CafeActivity;
import com.example.ZaeV_trip.Festival.FestivalActivity;
import com.example.ZaeV_trip.Lodging.LodgingActivity;
import com.example.ZaeV_trip.Plogging.PloggingActivity;
import com.example.ZaeV_trip.Profile.ProfileActivity;
import com.example.ZaeV_trip.Restaurant.RestaurantActivity;
import com.example.ZaeV_trip.Reusable.ReusableActivity;
import com.example.ZaeV_trip.Schedule.TravelActivity;
import com.example.ZaeV_trip.Search.SearchActivity;
import com.example.ZaeV_trip.Search.SearchFragment;
import com.example.ZaeV_trip.TouristSpot.TouristSpotActivity;
import com.example.ZaeV_trip.util.MySharedPreferences;
import com.example.ZaeV_trip.util.SharedViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    TextView current;
    String local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        current = findViewById(R.id.currentPosition);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            local = extras.getString("current");
            current.setText(local);
        }

        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SearchFragment searchFragment = new SearchFragment();
                fragmentTransaction.replace(R.id.main_container, searchFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Button cafeBtn = findViewById(R.id.cafeBtn);
        cafeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CafeActivity.class);
                intent.putExtra("local", current.getText());
                startActivity(intent);

            }
        });

        Button spotBtn = findViewById(R.id.spotBtn);
        spotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TouristSpotActivity.class);
                intent.putExtra("local", current.getText());
                startActivity(intent);
            }
        });

        Button reuseBtn = findViewById(R.id.reuseBtn);
        reuseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ReusableActivity.class);

                intent.putExtra("local", current.getText());
                startActivity(intent);

            }
        });

        Button ploggingBtn = findViewById(R.id.ploggingBtn);
        ploggingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PloggingActivity.class);

                intent.putExtra("local", current.getText());
                startActivity(intent);

            }
        });

        Button festivalBtn = findViewById(R.id.allBtn);
        festivalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FestivalActivity.class);
                intent.putExtra("local", current.getText());
                startActivity(intent);
            }
        });

        Button lodgingBtn = findViewById(R.id.hotelBtn);
        lodgingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LodgingActivity.class);
                intent.putExtra("local", current.getText());
                startActivity(intent);
            }
        });

        Button restaurantBtn = findViewById(R.id.restaurantBtn);
        restaurantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RestaurantActivity.class);
                intent.putExtra("local", current.getText());
                startActivity(intent);
            }
        });


        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);


        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.profile:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0, 0);
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
                    return true;
            }
            return false;
        });

    }

}