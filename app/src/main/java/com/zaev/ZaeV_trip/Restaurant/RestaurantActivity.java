package com.zaev.ZaeV_trip.Restaurant;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaev.ZaeV_trip.ProgressDialog;
import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.model.Restaurant;
import com.zaev.ZaeV_trip.util.getXmlData;

import java.util.ArrayList;

public class RestaurantActivity extends AppCompatActivity {
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    String local;
    SearchView searchView;
    RecyclerView list;
    ProgressDialog customProgressDialog;
    public static TextView notDataText;
    public static ImageView notDataImage;

    RestaurantAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        list = (RecyclerView) findViewById(R.id.restaurantList);
        searchView = findViewById(R.id.restaurantSearchBar);
        notDataText = findViewById(R.id.notDataText);
        notDataImage = findViewById(R.id.notDataImage);
        Bundle extras = getIntent().getExtras();

        customProgressDialog = new ProgressDialog(this);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customProgressDialog.show();

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float dpWidth = outMetrics.widthPixels;

        if (extras != null) {
            local = extras.getString("local");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                restaurants = getXmlData.getRestaurantData(RestaurantActivity.this);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Restaurant> filteredRestaurant = new ArrayList<Restaurant>();
                        customProgressDialog.dismiss();

                        for (int i = 0; i < restaurants.size(); i++) {
                            if (restaurants.get(i).getCategory() != null
                                    && (!restaurants.get(i).getCategory().equals("카페")
                                    && !restaurants.get(i).getCategory().equals("베이커리")
                                    && !restaurants.get(i).getCategory().equals("까페"))
                                    && (restaurants.get(i).getAuthType().equals("14")
                                    || restaurants.get(i).getAuthType().equals("18"))) {
                                if (local.equals("전체 지역") || local.equals("전체")) {
                                    filteredRestaurant.add(restaurants.get(i));
                                } else {
                                    if (restaurants.get(i).getLocation().contains(local)) {
                                        filteredRestaurant.add(restaurants.get(i));
                                    }
                                }
                            }
                        }

                        adapter = new RestaurantAdapter(RestaurantActivity.this, filteredRestaurant);
                        list.setLayoutManager(new LinearLayoutManager(RestaurantActivity.this, RecyclerView.VERTICAL, false));
                        list.setAdapter(adapter);

                        if(adapter.getItemCount() == 0){
                            notDataImage.setVisibility(View.VISIBLE);
                            notDataText.setVisibility(View.VISIBLE);
                            Log.d("어댑터 테스트", String.valueOf(adapter.getItemCount()));
                        }
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String s) {
                                adapter.getFilter().filter(s);
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String s) {
                                return false;
                            }
                        });
                        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                            @Override
                            public boolean onClose() {

                                adapter.getFilter().filter("");
                                return true;
                            }
                        });


                        // 서치아이콘이 아닌 서치바 클릭시 검색 가능하게 하기
                        searchView.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                searchView.setIconified(false);
                            }
                        });

                        View closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
                        closeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //handle click
                                searchView.setQuery("", false);
                                adapter.getFilter().filter("");
                                notDataImage.setVisibility(View.GONE);
                                notDataText.setVisibility(View.GONE);

                            }
                        });

                        adapter.setOnItemClickListener(new RestaurantAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int i) {
                                Bundle bundle = new Bundle();
                                bundle.putString("name", filteredRestaurant.get(i).getName());
                                bundle.putString("id", filteredRestaurant.get(i).getId());
                                bundle.putString("location", filteredRestaurant.get(i).getLocation());
                                bundle.putString("x", filteredRestaurant.get(i).getMapX());
                                bundle.putString("y", filteredRestaurant.get(i).getMapY());
                                bundle.putString("number", filteredRestaurant.get(i).getNumber());
                                bundle.putString("menu", filteredRestaurant.get(i).getMenu());
                                bundle.putString("category", filteredRestaurant.get(i).getCategory());
                                bundle.putFloat("width", dpWidth);

                                RestaurantFragment restaurantFragment = new RestaurantFragment();
                                restaurantFragment.setArguments(bundle);

                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.container_restaurant, restaurantFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                            }
                        });
                    }

                });
            }
        }).start();

    }
}
