package com.example.ZaeV_trip.Schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ZaeV_trip.BookmarkActivity;
import com.example.ZaeV_trip.MainActivity;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.SearchActivity;
import com.example.ZaeV_trip.util.ItemTouchHelperCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TravelActivity extends AppCompatActivity {

    RecyclerView scheduleRecyclerView;
    TravelListAdapter listAdapter;
    ArrayList<TravelItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.travel);


        TravelViewModel TravelViewModel =
                new ViewModelProvider(this).get(TravelViewModel.class);


        // fab 버튼
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // fab 버튼 누르면 이동
            }
        });

        //recyclerview
        scheduleRecyclerView = (RecyclerView) findViewById(R.id.travelRecycler);
        scheduleRecyclerView.setHasFixedSize(true);
        listAdapter = new TravelListAdapter(items);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        scheduleRecyclerView.setLayoutManager(mLayoutManager);
        scheduleRecyclerView.setItemAnimator(new DefaultItemAnimator());
        scheduleRecyclerView.setAdapter(listAdapter);

        //ItemTouchHelper 생성
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(listAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(scheduleRecyclerView);



        //Adapter에 데이터 추가
        TravelItem item1 = new TravelItem(R.drawable.profile_img,"2020.04.14 ~ 2020.05.15", "ㅇㅇ여행1");
        TravelItem item2 = new TravelItem(R.drawable.profile_img,"2020.04.14 ~ 2020.05.15", "ㅇㅇ여행2");
        TravelItem item3 = new TravelItem(R.drawable.profile_img,"2020.04.14 ~ 2020.05.15", "ㅇㅇ여행3");
        TravelItem item4 = new TravelItem(R.drawable.profile_img,"2020.04.14 ~ 2020.05.15", "ㅇㅇ여행4");
        TravelItem item5 = new TravelItem(R.drawable.profile_img,"2020.04.14 ~ 2020.05.15", "ㅇㅇ여행5");
        TravelItem item6 = new TravelItem(R.drawable.profile_img,"2020.04.14 ~ 2020.05.15", "ㅇㅇ여행6");
        TravelItem item7 = new TravelItem(R.drawable.profile_img,"2020.04.14 ~ 2020.05.15", "ㅇㅇ여행7");
        TravelItem item8 = new TravelItem(R.drawable.profile_img,"2020.04.14 ~ 2020.05.15", "ㅇㅇ여행8");
        TravelItem item9 = new TravelItem(R.drawable.profile_img,"2020.04.14 ~ 2020.05.15", "ㅇㅇ여행9");



        listAdapter.addItem(item1);
        listAdapter.addItem(item2);
        listAdapter.addItem(item3);
        listAdapter.addItem(item4);
        listAdapter.addItem(item5);
        listAdapter.addItem(item6);
        listAdapter.addItem(item7);
        listAdapter.addItem(item8);
        listAdapter.addItem(item9);


        listAdapter.setOnItemClickListener(new OnTravelItemClickListener() {
            @Override
            public void onItemClick(TravelListAdapter.ItemViewHolder holder, View view, int position) {
                TravelItem item = listAdapter.getItem(position);
                Toast.makeText(getApplicationContext(),"아이템 선택 " + item.getName(),
                        Toast.LENGTH_SHORT).show();
                // 액티비티->프래그먼트 전환
            }

        });

        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.bookmark:
                    startActivity(new Intent(getApplicationContext(), BookmarkActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.travel:

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