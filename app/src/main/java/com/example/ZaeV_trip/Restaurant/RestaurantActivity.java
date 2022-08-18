package com.example.ZaeV_trip.Restaurant;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.Restaurant;
import com.example.ZaeV_trip.util.getXmlData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class RestaurantActivity extends AppCompatActivity {
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    ArrayList<Restaurant> filteredRestaurant = new ArrayList<Restaurant>();
    String local;
    SearchView searchView;
    RecyclerView list;
    RestaurantAdapter adapter;
    //    LinearLayoutManager linearLayoutManager;
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        list = (RecyclerView) findViewById(R.id.restaurantList);
        searchView = findViewById(R.id.restaurantSearchBar);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            local = extras.getString("local");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                populateData();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        initAdapter();
                        initScrollListener();


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

    private void populateData() {
        restaurants = getXmlData.getRestaurantData(RestaurantActivity.this, "860", "1060");
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

    }

    private void initAdapter() {
//        adapter = new RestaurantAdapter(RestaurantActivity.this, filteredRestaurant);
        adapter = new RestaurantAdapter(RestaurantActivity.this, filteredRestaurant);

//        linearLayoutManager = new LinearLayoutManager(RestaurantActivity.this, RecyclerView.VERTICAL, false);
//        list.setLayoutManager(linearLayoutManager);
        list.setAdapter(adapter);
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
    }

    private void initScrollListener() {
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == filteredRestaurant.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {
        filteredRestaurant.add(null);
        list.post(new Runnable() {
            public void run() {
                adapter.notifyItemInserted(filteredRestaurant.size() - 1);
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                filteredRestaurant.remove(filteredRestaurant.size() - 1);
                int scrollPosition = filteredRestaurant.size();
                adapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 200;
                int startIndex = 860 + currentSize + 1;
                int endIndex = 860 + currentSize + 200;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        restaurants = getXmlData.getRestaurantData(RestaurantActivity.this, String.valueOf(startIndex), String.valueOf(endIndex));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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
                                adapter.notifyDataSetChanged();
                                isLoading = false;
                            }
                        });
                    }
                }).start();
            }
        }, 1000);


    }
}
