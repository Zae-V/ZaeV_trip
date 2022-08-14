package com.example.ZaeV_trip.Restaurant;

import android.os.Bundle;
import android.view.View;
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
    String local;
    SearchView searchView;
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        list = (RecyclerView) findViewById(R.id.restaurantList);
        searchView = findViewById(R.id.restaurantSearchBar);
        Bundle extras = getIntent().getExtras();

        if(extras!=null){
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

                        for(int i = 0; i< restaurants.size(); i++) {
                            if (local.equals("전체 지역") || local.equals("전체")) {
                                filteredRestaurant.add(restaurants.get(i));
                            }
                            else {
                                if (restaurants.get(i).getLocation().contains(local)) {
                                    filteredRestaurant.add(restaurants.get(i));
                                }
                            }
                            RestaurantAdapter adapter = new RestaurantAdapter(RestaurantActivity.this, filteredRestaurant);
                            list.setLayoutManager(new LinearLayoutManager(RestaurantActivity.this, RecyclerView.VERTICAL, false));
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
                            adapter.setOnItemClickListener(new RestaurantAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int i) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("name", filteredRestaurant.get(i).getName());
                                    bundle.putString("id", filteredRestaurant.get(i).getId());
                                    bundle.putString("location", filteredRestaurant.get(i).getLocation());
                                    bundle.putString("x", filteredRestaurant.get(i).getMapX());
                                    bundle.putString("y", filteredRestaurant.get(i).getMapY());
                                    bundle.putString("number",filteredRestaurant.get(i).getNumber());

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
                    }
                });
            }
        }).start();

    }

}
