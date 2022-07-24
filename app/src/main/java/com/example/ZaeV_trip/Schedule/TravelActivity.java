package com.example.ZaeV_trip.Schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        //Initialize And Assign Variable
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.travel);


        TravelViewModel TravelViewModel =
                new ViewModelProvider(this).get(TravelViewModel.class);

        // fab 버튼
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // fab 버튼 누르면 calendar fragment 이동
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                CalendarFragment calendarFragment = new CalendarFragment();
                fragmentTransaction.add(R.id.container_travel, calendarFragment);
                fragmentTransaction.commit();
                bottomNavigationView.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
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
                // 액티비티-> schedule fragment 전환
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

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TAG!", "onStop()");
        // 작동안됨
        bottomNavigationView.setVisibility(View.GONE);
    }

    // 연결된 fragment 끼리 전환
    public void changeFragment(int index){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (index) {
            case 1:
                SetScheduleFragment setScheduleFragment = new SetScheduleFragment();
                transaction.add(R.id.container_travel, setScheduleFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                bottomNavigationView.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                break;
            case 2:
                AddScheduleFragment addScheduleFragment = new AddScheduleFragment();
                transaction.add(R.id.container_travel, addScheduleFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                bottomNavigationView.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                break;
        }
    }

}