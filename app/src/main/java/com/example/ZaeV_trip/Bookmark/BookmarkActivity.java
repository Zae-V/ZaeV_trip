package com.example.ZaeV_trip.Bookmark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.ZaeV_trip.Main.MainActivity;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Schedule.TravelActivity;
import com.example.ZaeV_trip.Search.SearchActivity;
import com.example.ZaeV_trip.util.ItemTouchHelperCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {

    RecyclerView bookmarkRecyclerView;
    BookmarkListAdapter listAdapter;
    ItemTouchHelper helper;

    ArrayList<BookmarkItem> items = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        //recyclerview
        bookmarkRecyclerView = (RecyclerView) findViewById(R.id.bookmarkRecycler);
        bookmarkRecyclerView.setHasFixedSize(true);
        listAdapter = new BookmarkListAdapter(items);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        bookmarkRecyclerView.setLayoutManager(mLayoutManager);
        bookmarkRecyclerView.setItemAnimator(new DefaultItemAnimator());
        bookmarkRecyclerView.setAdapter(listAdapter);

        //ItemTouchHelper 생성
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(listAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(bookmarkRecyclerView);


        //Adapter에 데이터 추가
        BookmarkItem bookmarkItem1 = new BookmarkItem(R.drawable.vegan_burger,"제비식당","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");
        BookmarkItem bookmarkItem2 = new BookmarkItem(R.drawable.vegan_burger,"야채꼬치","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");
        BookmarkItem bookmarkItem3 = new BookmarkItem(R.drawable.vegan_burger,"비건버거","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");
        BookmarkItem bookmarkItem4 = new BookmarkItem(R.drawable.vegan_burger,"초록숲상점","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");
        BookmarkItem bookmarkItem5 = new BookmarkItem(R.drawable.vegan_burger,"제로웨이스트샵","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");
        BookmarkItem bookmarkItem6 = new BookmarkItem(R.drawable.vegan_burger,"제비식당","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");
        BookmarkItem bookmarkItem7 = new BookmarkItem(R.drawable.vegan_burger,"야채꼬치","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");
        BookmarkItem bookmarkItem8 = new BookmarkItem(R.drawable.vegan_burger,"비건버거","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");
        BookmarkItem bookmarkItem9 = new BookmarkItem(R.drawable.vegan_burger,"초록숲상점","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");
        BookmarkItem bookmarkItem10 = new BookmarkItem(R.drawable.vegan_burger,"제로웨이스트샵","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");

        listAdapter.addItem(bookmarkItem1);
        listAdapter.addItem(bookmarkItem2);
        listAdapter.addItem(bookmarkItem3);
        listAdapter.addItem(bookmarkItem4);
        listAdapter.addItem(bookmarkItem5);
        listAdapter.addItem(bookmarkItem6);
        listAdapter.addItem(bookmarkItem7);
        listAdapter.addItem(bookmarkItem8);
        listAdapter.addItem(bookmarkItem9);
        listAdapter.addItem(bookmarkItem10);

        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.bookmark);


        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.bookmark:

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