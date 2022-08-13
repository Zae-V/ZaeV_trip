package com.example.ZaeV_trip.TouristSpot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ZaeV_trip.Festival.FestivalFragment;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Restaurant.RestaurantActivity;
import com.example.ZaeV_trip.Restaurant.RestaurantAdapter;
import com.example.ZaeV_trip.Restaurant.RestaurantFragment;
import com.example.ZaeV_trip.model.TouristSpot;
import com.example.ZaeV_trip.util.getXmlData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class TouristSpotActivity extends AppCompatActivity {

    ArrayList<TouristSpot> touristSpots = new ArrayList<>();
    String local;
    SearchView searchView;
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_spot);

        list = (RecyclerView) findViewById(R.id.touristSpotList);
        searchView = findViewById(R.id.touristSpotSearchBar);
        Bundle extras = getIntent().getExtras();

        if(extras!=null){
            local = extras.getString("local");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                touristSpots = getXmlData.getTourSpotData(TouristSpotActivity.this);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<TouristSpot> filteredTouristSpot = new ArrayList<TouristSpot>();

                        for(int i = 0; i< touristSpots.size(); i++) {
                            if (local.equals("전체 지역") || local.equals("전체")) {
                                filteredTouristSpot.add(touristSpots.get(i));
                            }
                            else {
                                if (touristSpots.get(i).getAddr1().contains(local)) {
                                    filteredTouristSpot.add(touristSpots.get(i));
                                }
                            }
                            TouristSpotAdapter adapter = new TouristSpotAdapter(TouristSpotActivity.this, filteredTouristSpot);
                            list.setLayoutManager(new LinearLayoutManager(TouristSpotActivity.this, RecyclerView.VERTICAL, false));
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

                            adapter.setOnItemClickListener(new TouristSpotAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int i) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("name", filteredTouristSpot.get(i).getTitle());
                                    bundle.putString("contentID", filteredTouristSpot.get(i).getContentID());
                                    bundle.putString("location", filteredTouristSpot.get(i).getAddr1());
                                    bundle.putString("x", filteredTouristSpot.get(i).getMapX());
                                    bundle.putString("y", filteredTouristSpot.get(i).getMapY());
                                    bundle.putString("firstImg", filteredTouristSpot.get(i).getFirstImage());

                                    TouristSpotFragment touristSpotFragment = new TouristSpotFragment();
                                    touristSpotFragment.setArguments(bundle);

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container_tourist_spot, touristSpotFragment);
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
