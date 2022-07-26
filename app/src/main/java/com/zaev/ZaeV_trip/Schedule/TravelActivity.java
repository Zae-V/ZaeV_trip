package com.zaev.ZaeV_trip.Schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zaev.ZaeV_trip.Bookmark.BookmarkActivity;
import com.zaev.ZaeV_trip.Profile.ProfileActivity;
import com.zaev.ZaeV_trip.Main.MainActivity;
import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.util.ItemTouchHelperCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TravelActivity extends AppCompatActivity {

    RecyclerView scheduleRecyclerView;
    TravelListAdapter listAdapter;
    ArrayList<TravelItem> items = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        //Initialize And Assign Variable
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.travel);


//        // fab 버튼
//        fab = (FloatingActionButton)findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                // fab 버튼 누르면 calendar fragment 이동
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                CalendarFragment calendarFragment = new CalendarFragment();
//                fragmentTransaction.add(R.id.container_travel, calendarFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//
//            }
//        });
//
//        bottomNavigationView.setVisibility(View.VISIBLE);
//        fab.setVisibility(View.GONE); // 일정 막기
//
//        //recyclerview
//        scheduleRecyclerView = (RecyclerView) findViewById(R.id.travelRecycler);
//
//        scheduleRecyclerView.setVisibility(View.GONE); // 비활성화
//        scheduleRecyclerView.setHasFixedSize(true);
//        listAdapter = new TravelListAdapter(items);
//
//
//
//        //ItemTouchHelper 생성
//        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(listAdapter);
//        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//        touchHelper.attachToRecyclerView(scheduleRecyclerView);
//
//        //Adapter에 데이터 추가
//        db.collection("Schedule").document(uid).collection("schedule").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    QuerySnapshot query = task.getResult();
//                    for(QueryDocumentSnapshot document : query){
//                        TravelItem item = new TravelItem("","","");
//                        item.setName(String.valueOf(document.getData().get("name")));
//
//                        Timestamp startStamp = (Timestamp) document.getData().get("startDate");
//                        Date startDate = startStamp.toDate();
//                        Calendar calendar1 = new GregorianCalendar();
//                        calendar1.setTime(startDate);
//
//                        Timestamp endStamp = (Timestamp) document.getData().get("endDate");
//                        Date endDate = endStamp.toDate();
//                        Calendar calendar2 = new GregorianCalendar();
//                        calendar2.setTime(endDate);
//
//                        item.setDate(calendar1.get(Calendar.YEAR) + "." + String.valueOf(calendar1.get(Calendar.MONTH)+1) + "." +calendar1.get(Calendar.DAY_OF_MONTH)
//                        + " ~ " + calendar2.get(Calendar.YEAR) + "." + String.valueOf(calendar2.get(Calendar.MONTH)+1) + "." +calendar2.get(Calendar.DAY_OF_MONTH)
//                        );
//
//                        item.setImage(String.valueOf(document.getData().get("img")));
//
//                        listAdapter.addItem(item);
//                    }
//                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TravelActivity.this);
//                    scheduleRecyclerView.setLayoutManager(mLayoutManager);
//                    scheduleRecyclerView.setItemAnimator(new DefaultItemAnimator());
//                    scheduleRecyclerView.setAdapter(listAdapter);
//                }
//            }
//        });
//
//        listAdapter.setOnItemClickListener(new OnTravelItemClickListener() {
//            @Override
//            public void onItemClick(TravelListAdapter.ItemViewHolder holder, View view, int position) {
//                TravelItem item = listAdapter.getItem(position);
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                ScheduleFragment scheduleFragment = new ScheduleFragment();
//
//                Bundle bundle = new Bundle();
//                bundle.putInt("position",position);
//                bundle.putString("name", item.getName());
//                bundle.putString("date", item.getDate());
//                bundle.putString("img", item.getImage());
//                scheduleFragment.setArguments(bundle);
//
//                fragmentTransaction.replace(R.id.container_travel, scheduleFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                bottomNavigationView.setVisibility(View.GONE);
//            }
//
//        });

        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.profile:
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    TravelActivity.this.finish();

                    return true;
                case R.id.bookmark:
                    Intent intent1 = new Intent(getApplicationContext(), BookmarkActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent1);
                    TravelActivity.this.finish();

                    return true;

                case R.id.travel:
                    return true;

                case R.id.home:
                    Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent2);
                    TravelActivity.this.finish();

            }
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if(count == 0){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            TravelActivity.this.finish();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setVisibility(View.VISIBLE);
//        fab.setVisibility(View.VISIBLE);
    }

    // 연결된 fragment 끼리 전환
    public void changeFragment(int index){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (index) {
            case 1:
                SetScheduleFragment setScheduleFragment = new SetScheduleFragment();
                transaction.replace(R.id.container_travel, setScheduleFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 2:
                AddScheduleFragment addScheduleFragment = new AddScheduleFragment();
                transaction.replace(R.id.container_travel, addScheduleFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 3:
                ScheduleFragment scheduleFragment = new ScheduleFragment();
                transaction.replace(R.id.container_travel, scheduleFragment);
                transaction.addToBackStack(null);
                transaction.commit();
        }
    }

}