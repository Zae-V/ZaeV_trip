package com.zaev.ZaeV_trip.Bookmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.zaev.ZaeV_trip.Cafe.CafeAdapter;
import com.zaev.ZaeV_trip.Festival.FestivalAdapter;
import com.zaev.ZaeV_trip.Lodging.LodgingAdapter;
import com.zaev.ZaeV_trip.Plogging.PloggingAdapter;
import com.zaev.ZaeV_trip.Profile.ProfileActivity;
import com.zaev.ZaeV_trip.Main.MainActivity;
import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.Restaurant.RestaurantAdapter;
import com.zaev.ZaeV_trip.Reusable.ReusableAdapter;
import com.zaev.ZaeV_trip.Schedule.TravelActivity;
import com.zaev.ZaeV_trip.TouristSpot.TouristSpotAdapter;
import com.zaev.ZaeV_trip.ZeroWaste.ZeroWasteAdapter;
import com.zaev.ZaeV_trip.model.Cafe;
import com.zaev.ZaeV_trip.model.Festival;
import com.zaev.ZaeV_trip.model.Lodging;
import com.zaev.ZaeV_trip.model.Plogging;
import com.zaev.ZaeV_trip.model.Restaurant;
import com.zaev.ZaeV_trip.model.Reusable;
import com.zaev.ZaeV_trip.model.TouristSpot;
import com.zaev.ZaeV_trip.model.ZeroWaste;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {

    RecyclerView bookmarkRecyclerView;

    // Adapter
    CafeAdapter listAdapter;
    FestivalAdapter listAdapter1;
    RestaurantAdapter listAdapter2;
    TouristSpotAdapter listAdapter3;
    LodgingAdapter listAdapter4;
    ReusableAdapter listAdapter5;
    ZeroWasteAdapter listAdapter6;
    PloggingAdapter listAdapter7;

    ItemTouchHelper helper;

    //ArrayList
    ArrayList<Cafe> bookmarkedItems = new ArrayList<Cafe>();
    ArrayList<Festival> bookmarkedItems1 = new ArrayList<Festival>();
    ArrayList<Restaurant> bookmarkedItems2 = new ArrayList<Restaurant>();
    ArrayList<TouristSpot> bookmarkedItems3 = new ArrayList<>();
    ArrayList<Lodging> bookmarkedItems4 = new ArrayList<>();
    ArrayList<Reusable> bookmarkedItems5 = new ArrayList<>();
    ArrayList<ZeroWaste> bookmarkedItems6 = new ArrayList<>();
    ArrayList<Plogging> bookmarkedItems7 = new ArrayList<>();


    FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    ConcatAdapter mConcatAdapter = new ConcatAdapter();
    ImageView notifyImg;
    TextView notifyText1;
    TextView notifyText2;

    boolean isEmpty = false;
    boolean isEmpty1 = false;
    boolean isEmpty2 = false;
    boolean isEmpty3 = false;
    boolean isEmpty4 = false;
    boolean isEmpty5 = false;
    boolean isEmpty6 = false;
    boolean isEmpty7 = false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        BookmarkActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        // 북마크 없을 때의 view
        notifyImg = findViewById(R.id.notifyImage);
        notifyText1 = findViewById(R.id.notifyText1);
        notifyText2 = findViewById(R.id.notifyText2);

        //recyclerview
        bookmarkRecyclerView = (RecyclerView) findViewById(R.id.bookmarkRecycler);
        bookmarkRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(BookmarkActivity.this);
        bookmarkRecyclerView.setLayoutManager(mLayoutManager);
        bookmarkRecyclerView.setItemAnimator(new DefaultItemAnimator());


        String[] docs = {"cafe", "festival", "lodging", "plogging", "restaurant", "reusable", "touristSpot", "zeroWaste"};

        for (String doc : docs) {
            mDatabase.collection("BookmarkItem").document(uid).collection(doc).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.getResult().size() > 0) {
                        // 북마크 하나라도 있을 때 없애기
                        notifyImg.setVisibility(View.GONE);
                        notifyText1.setVisibility(View.GONE);
                        notifyText2.setVisibility(View.GONE);
                    }
                    else{

                    }
                }
            });
        }

        CollectionReference collection = mDatabase.collection("BookmarkItem").document(uid).collection("cafe");
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot query = task.getResult();
                    for (QueryDocumentSnapshot document : query) {
                        Cafe cafe = new Cafe("", "", "", "", "", "", "", "");
                        cafe.setId(String.valueOf(document.getData().get("serialNumber")));
                        cafe.setLocation(String.valueOf(document.getData().get("address")));
                        cafe.setCategory(String.valueOf(document.getData().get("type")));
                        cafe.setName(String.valueOf(document.getData().get("name")));
                        cafe.setMapX(String.valueOf(document.getData().get("position_x")));
                        cafe.setMapY(String.valueOf(document.getData().get("position_y")));
                        cafe.setNumber(String.valueOf(document.getData().get("tel")));
                        cafe.setMenu(String.valueOf(document.getData().get("menu")));
                        bookmarkedItems.add(cafe);
                        isEmpty = true;
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
                if (task.isSuccessful()) {
                    QuerySnapshot query = task.getResult();
                    for (QueryDocumentSnapshot document : query) {
                        Festival festival = new Festival("", "", "", "", "", "", "", "", "");
                        festival.setId(String.valueOf(document.getData().get("serialNumber")));
                        festival.setAddr1(String.valueOf(document.getData().get("address")));
                        festival.setTitle(String.valueOf(document.getData().get("name")));
                        festival.setFirstImage(String.valueOf(document.getData().get("img")));
                        festival.setMapX(String.valueOf(document.getData().get("position_x")));
                        festival.setMapY(String.valueOf(document.getData().get("position_y")));
                        festival.setStartDate(String.valueOf(document.getData().get("start_date")));
                        festival.setEndDate(String.valueOf(document.getData().get("end_date")));
                        festival.setTel(String.valueOf(document.getData().get("tel")));
                        bookmarkedItems1.add(festival);
                        isEmpty1 = true;
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
                if (task.isSuccessful()) {
                    QuerySnapshot query = task.getResult();
                    for (QueryDocumentSnapshot document : query) {
                        Restaurant restaurant = new Restaurant("", "", "", "", "", "", "", "", "");
                        restaurant.setId(String.valueOf(document.getData().get("serialNumber")));
                        restaurant.setLocation(String.valueOf(document.getData().get("address")));
                        restaurant.setName(String.valueOf(document.getData().get("name")));
                        restaurant.setMapX(String.valueOf(document.getData().get("position_x")));
                        restaurant.setMapY(String.valueOf(document.getData().get("position_y")));
                        restaurant.setNumber(String.valueOf(document.getData().get("tel")));
                        restaurant.setCategory(String.valueOf(document.getData().get("category")));
                        bookmarkedItems2.add(restaurant);
                        isEmpty2 = true;
                    }

                    listAdapter2 = new RestaurantAdapter(BookmarkActivity.this, bookmarkedItems2);
                    mConcatAdapter.addAdapter(listAdapter2);
                    bookmarkRecyclerView.setAdapter(mConcatAdapter);
                }
            }
        });

        CollectionReference collection3 = mDatabase.collection("BookmarkItem").document(uid).collection("touristSpot");
        collection3.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot query = task.getResult();
                    for (QueryDocumentSnapshot document : query) {
                        TouristSpot touristSpot = new TouristSpot("", "", "", "", "", "", "", "");
                        touristSpot.setContentID(String.valueOf(document.getData().get("serialNumber")));
                        touristSpot.setAddr1(String.valueOf(document.getData().get("address")));
                        touristSpot.setAddr2(String.valueOf(document.getData().get("address2")));
                        touristSpot.setTitle(String.valueOf(document.getData().get("name")));
                        touristSpot.setFirstImage(String.valueOf(document.getData().get("image")));
                        touristSpot.setMapX(String.valueOf(document.getData().get("position_x")));
                        touristSpot.setMapY(String.valueOf(document.getData().get("position_y")));
                        bookmarkedItems3.add(touristSpot);
                        isEmpty3 = true;
                    }

                    listAdapter3 = new TouristSpotAdapter(BookmarkActivity.this, bookmarkedItems3);
                    mConcatAdapter.addAdapter(listAdapter3);
                    bookmarkRecyclerView.setAdapter(mConcatAdapter);
                }
            }
        });

        CollectionReference collection4 = mDatabase.collection("BookmarkItem").document(uid).collection("lodging");
        collection4.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot query = task.getResult();
                    for (QueryDocumentSnapshot document : query) {
                        Lodging lodging = new Lodging("", "", "", "", "", "", "", "", "");
                        lodging.setContentID(String.valueOf(document.getData().get("serialNumber")));
                        lodging.setAddr1(String.valueOf(document.getData().get("address")));
                        lodging.setTitle(String.valueOf(document.getData().get("name")));
                        lodging.setFirstImage(String.valueOf(document.getData().get("image")));
                        lodging.setMapX(String.valueOf(document.getData().get("position_x")));
                        lodging.setMapY(String.valueOf(document.getData().get("position_y")));
                        bookmarkedItems4.add(lodging);
                        isEmpty4 = true;
                    }

                    listAdapter4 = new LodgingAdapter(BookmarkActivity.this, bookmarkedItems4);
                    mConcatAdapter.addAdapter(listAdapter4);
                    bookmarkRecyclerView.setAdapter(mConcatAdapter);
                }
            }
        });

        CollectionReference collection5 = mDatabase.collection("BookmarkItem").document(uid).collection("reusable");
        collection5.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot query = task.getResult();
                    for (QueryDocumentSnapshot document : query) {
                        Reusable reusable = new Reusable("", "", "", "", "");
                        reusable.setName(String.valueOf(document.getData().get("name")));
                        reusable.setMapX(String.valueOf(document.getData().get("position_x")));
                        reusable.setMapY(String.valueOf(document.getData().get("position_y")));
                        reusable.setLocation(String.valueOf(document.getData().get("address")));
                        reusable.setReason(String.valueOf(document.getData().get("reason")));
                        bookmarkedItems5.add(reusable);
                        isEmpty5 = true;
                    }

                    listAdapter5 = new ReusableAdapter(BookmarkActivity.this, bookmarkedItems5);
                    mConcatAdapter.addAdapter(listAdapter5);
                    bookmarkRecyclerView.setAdapter(mConcatAdapter);
                }
            }
        });

        CollectionReference collection6 = mDatabase.collection("BookmarkItem").document(uid).collection("zeroWaste");
        collection6.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot query = task.getResult();
                    for (QueryDocumentSnapshot document : query) {
                        ZeroWaste zeroWaste = new ZeroWaste("", "", "", "", "", "", "", "", "", "", "", "");
                        zeroWaste.setName(String.valueOf(document.getData().get("name")));
                        zeroWaste.setMapX(String.valueOf(document.getData().get("position_x")));
                        zeroWaste.setMapY(String.valueOf(document.getData().get("position_y")));
                        zeroWaste.setAddr1(String.valueOf(document.getData().get("address")));
                        zeroWaste.setKeyword(String.valueOf(document.getData().get("keyword")));
                        bookmarkedItems6.add(zeroWaste);
                        isEmpty6 = true;
                    }

                    listAdapter6 = new ZeroWasteAdapter(BookmarkActivity.this, bookmarkedItems6);
                    mConcatAdapter.addAdapter(listAdapter6);
                    bookmarkRecyclerView.setAdapter(mConcatAdapter);
                }
            }
        });

        CollectionReference collection7 = mDatabase.collection("BookmarkItem").document(uid).collection("plogging");
        collection7.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot query = task.getResult();
                    for (QueryDocumentSnapshot document : query) {
                        Plogging plogging = new Plogging();
                        plogging.setCrsKorNm(String.valueOf(document.getData().get("name")));
                        plogging.setCrsLevel(String.valueOf(document.getData().get("level")));
                        plogging.setSigun(String.valueOf(document.getData().get("sigun")));
                        bookmarkedItems7.add(plogging);
                        isEmpty7 = true;
                    }

                    listAdapter7 = new PloggingAdapter(BookmarkActivity.this, bookmarkedItems7);
                    mConcatAdapter.addAdapter(listAdapter7);
                    bookmarkRecyclerView.setAdapter(mConcatAdapter);
                }
            }
        });

//        if ((isEmpty || isEmpty1 || isEmpty2 || isEmpty3 || isEmpty4 || isEmpty5 || isEmpty6 || isEmpty7)) {
//            notifyImg.setVisibility(View.VISIBLE);
//            notifyText1.setVisibility(View.VISIBLE);
//            notifyText2.setVisibility(View.VISIBLE);
//        }

//        if (listAdapter.getItemCount() == 0 &&
//                listAdapter1.getItemCount() == 0 &&
//                listAdapter2.getItemCount() == 0 &&
//                listAdapter3.getItemCount() == 0 &&
//                listAdapter4.getItemCount() == 0 &&
//                listAdapter5.getItemCount() == 0 &&
//                listAdapter6.getItemCount() == 0 &&
//                listAdapter7.getItemCount() == 0) {
//            Log.d("테스트", "찜목록이 없어요");
//            notifyImg.setVisibility(View.VISIBLE);
//            notifyText1.setVisibility(View.VISIBLE);
//            notifyText2.setVisibility(View.VISIBLE);
//        }

        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.bookmark);


        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.profile:
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    BookmarkActivity.this.finish();

                    return true;
                case R.id.bookmark:
                    return true;

                case R.id.travel:
                    Intent intent1 = new Intent(getApplicationContext(), TravelActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent1);
                    BookmarkActivity.this.finish();

                    return true;

                case R.id.home:
                    Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent2);
                    BookmarkActivity.this.finish();

            }
            return false;
        });
    }

}