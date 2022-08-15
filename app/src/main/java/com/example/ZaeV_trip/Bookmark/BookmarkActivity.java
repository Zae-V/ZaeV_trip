package com.example.ZaeV_trip.Bookmark;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.ZaeV_trip.Cafe.CafeActivity;
import com.example.ZaeV_trip.Cafe.CafeAdapter;
import com.example.ZaeV_trip.Festival.FestivalAdapter;
import com.example.ZaeV_trip.Profile.ProfileActivity;
import com.example.ZaeV_trip.Main.MainActivity;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Restaurant.RestaurantAdapter;
import com.example.ZaeV_trip.Schedule.TravelActivity;
import com.example.ZaeV_trip.Search.SearchActivity;
import com.example.ZaeV_trip.model.Cafe;
import com.example.ZaeV_trip.model.Festival;
import com.example.ZaeV_trip.model.Restaurant;
import com.example.ZaeV_trip.util.ItemTouchHelperCallback;
import com.example.ZaeV_trip.util.getXmlData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    RecyclerView bookmarkRecyclerView;

    // Adapter
    CafeAdapter listAdapter;
    FestivalAdapter listAdapter1;
    RestaurantAdapter listAdapter2;

    ItemTouchHelper helper;

    //ArrayList
    ArrayList<Cafe> bookmarkedItems = new ArrayList<Cafe>();
    ArrayList<Festival> bookmarkedItems1 = new ArrayList<Festival>();
    ArrayList<Restaurant> bookmarkedItems2 = new ArrayList<Restaurant>();

    FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    ConcatAdapter mConcatAdapter = new ConcatAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        //recyclerview
        bookmarkRecyclerView = (RecyclerView) findViewById(R.id.bookmarkRecycler);
        bookmarkRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(BookmarkActivity.this);
        bookmarkRecyclerView.setLayoutManager(mLayoutManager);
        bookmarkRecyclerView.setItemAnimator(new DefaultItemAnimator());


        CollectionReference collection = mDatabase.collection("BookmarkItem").document(uid).collection("cafe");
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            QuerySnapshot query = task.getResult();
                            for(QueryDocumentSnapshot document : query){
                                Cafe cafe = new Cafe("","","","","","","","");
                                cafe.setId(String.valueOf(document.getData().get("serialNumber")));
                                cafe.setLocation(String.valueOf(document.getData().get("address")));
                                cafe.setCategory(String.valueOf(document.getData().get("type")));
                                cafe.setName(String.valueOf(document.getData().get("name")));
                                cafe.setMapX(String.valueOf(document.getData().get("position_x")));
                                cafe.setMapY(String.valueOf(document.getData().get("position_y")));
                                cafe.setNumber(String.valueOf(document.getData().get("tel")));
                                cafe.setMenu(String.valueOf(document.getData().get("menu")));
                                bookmarkedItems.add(cafe);
                            }
                            Log.e("b", String.valueOf(bookmarkedItems));
                            listAdapter = new CafeAdapter(BookmarkActivity.this, bookmarkedItems);
                            mConcatAdapter.addAdapter(listAdapter);
                            bookmarkRecyclerView.setAdapter(mConcatAdapter);
                        }
                    }
                });

        CollectionReference collection1 = mDatabase.collection("BookmarkItem").document(uid).collection("festival");
        collection1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot query = task.getResult();
                    for(QueryDocumentSnapshot document : query){
                        Festival festival = new Festival("","","","","","","","");
                        festival.setId(String.valueOf(document.getData().get("serialNumber")));
                        festival.setAddr1(String.valueOf(document.getData().get("address")));
                        festival.setTitle(String.valueOf(document.getData().get("name")));
                        festival.setFirstImage(String.valueOf(document.getData().get("img")));
                        festival.setMapX(String.valueOf(document.getData().get("position_x")));
                        festival.setMapY(String.valueOf(document.getData().get("position_y")));
                        festival.setStartDate(String.valueOf(document.getData().get("start_date")));
                        festival.setEndDate(String.valueOf(document.getData().get("end_date")));
                        bookmarkedItems1.add(festival);
                    }

                    listAdapter1 = new FestivalAdapter(BookmarkActivity.this, bookmarkedItems1);
                    mConcatAdapter.addAdapter(listAdapter1);
                    bookmarkRecyclerView.setAdapter(mConcatAdapter);
                }
            }
        });

        CollectionReference collection2 = mDatabase.collection("BookmarkItem").document(uid).collection("restaurant");
        collection2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot query = task.getResult();
                    for(QueryDocumentSnapshot document : query){
                        Restaurant restaurant = new Restaurant("","","","","","","","","");
                        restaurant.setContentID(String.valueOf(document.getData().get("serialNumber")));
                        restaurant.setAddr1(String.valueOf(document.getData().get("address")));
                        restaurant.setTitle(String.valueOf(document.getData().get("name")));
                        restaurant.setFirstImage(String.valueOf(document.getData().get("image")));
                        restaurant.setMapX(String.valueOf(document.getData().get("position_x")));
                        restaurant.setMapY(String.valueOf(document.getData().get("position_y")));
                        restaurant.setNumber(String.valueOf(document.getData().get("tel")));
                        bookmarkedItems2.add(restaurant);
                    }

                    listAdapter2 = new RestaurantAdapter(BookmarkActivity.this, bookmarkedItems2);
                    mConcatAdapter.addAdapter(listAdapter2);
                    bookmarkRecyclerView.setAdapter(mConcatAdapter);
                }
            }
        });

//        //ItemTouchHelper 생성
//        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(listAdapter);
//        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//        touchHelper.attachToRecyclerView(bookmarkRecyclerView);


        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.bookmark);


        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.profile:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
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