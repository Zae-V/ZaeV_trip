package com.example.ZaeV_trip.TouristSpot;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ZaeV_trip.Cafe.CafeActivity;
import com.example.ZaeV_trip.Cafe.CafeAdapter;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.TouristSpot;

import java.util.ArrayList;

public class TouristSpotActivity extends AppCompatActivity {

    ArrayList<TouristSpot> touristSpots = new ArrayList<>();
    String local;

    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_spot);

        gridView = findViewById(R.id.touristSpotList);

        TouristSpot newItem = new TouristSpot(
                "서울특별시 용산구 청파로 74",
                "(방화동)",
                "http://tong.visitkorea.or.kr/cms/resource/81/1894481_image2_1.jpg",
                "http://tong.visitkorea.or.kr/cms/resource/81/1894481_image3_1.jpg",
                126.8171490732,
                37.5860879769,
                "강서습지생태공원");

        TouristSpotAdapter adapter = new TouristSpotAdapter(TouristSpotActivity.this, newItem);


        gridView.setAdapter(adapter);


    }
}
