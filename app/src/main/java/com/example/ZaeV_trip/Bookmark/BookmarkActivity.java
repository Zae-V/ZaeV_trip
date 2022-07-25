package com.example.ZaeV_trip.Bookmark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.ZaeV_trip.MainActivity;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Schedule.TravelActivity;
import com.example.ZaeV_trip.Search.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {

    RecyclerView bookmarkRecyclerView;
    BookmarkListAdapter bookmarkListAdapter;
    //ItemTouchHelperCallback helper;
    ArrayList<BookmarkItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        bookmarkRecyclerView = (RecyclerView) findViewById(R.id.bookmarkRecycler);
        bookmarkRecyclerView.setHasFixedSize(true);
        bookmarkListAdapter = new BookmarkListAdapter(items);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        bookmarkRecyclerView.setLayoutManager(mLayoutManager);
        bookmarkRecyclerView.setItemAnimator(new DefaultItemAnimator());
        bookmarkRecyclerView.setAdapter(bookmarkListAdapter);

        //ItemTouchHelper 생성
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback((OnBookmarkItemClickListener) bookmarkListAdapter);
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

        bookmarkListAdapter.addItem(bookmarkItem1);
        bookmarkListAdapter.addItem(bookmarkItem2);
        bookmarkListAdapter.addItem(bookmarkItem3);
        bookmarkListAdapter.addItem(bookmarkItem4);
        bookmarkListAdapter.addItem(bookmarkItem5);
        bookmarkListAdapter.addItem(bookmarkItem6);
        bookmarkListAdapter.addItem(bookmarkItem7);
        bookmarkListAdapter.addItem(bookmarkItem8);
        bookmarkListAdapter.addItem(bookmarkItem9);
        bookmarkListAdapter.addItem(bookmarkItem10);

        //내비게이션 바 코드
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