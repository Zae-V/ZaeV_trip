package com.example.ZaeV_trip.Plogging;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.TouristSpot.TouristSpotActivity;
import com.example.ZaeV_trip.TouristSpot.TouristSpotAdapter;
import com.example.ZaeV_trip.TouristSpot.TouristSpotFragment;
import com.example.ZaeV_trip.model.Plogging;
import com.example.ZaeV_trip.model.TouristSpot;
import com.example.ZaeV_trip.util.getXmlData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class PloggingActivity extends AppCompatActivity {
    ArrayList<Plogging> ploggings = new ArrayList<>();
    String local;
    SearchView searchView;
    RecyclerView list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plogging);

        list = (RecyclerView) findViewById(R.id.ploggingList);
        searchView = findViewById(R.id.ploggingSearchBar);
        Bundle extras = getIntent().getExtras();

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float dpWidth = outMetrics.widthPixels;

        if(extras!=null){
            local = extras.getString("local");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                ploggings = getXmlData.getPloggingData(PloggingActivity.this);

                runOnUiThread(new Runnable() {
                    ArrayList<Plogging> filteredPlogging = new ArrayList<Plogging>();

                    @Override
                    public void run() {
                        for(int i = 0; i< ploggings.size(); i++) {
                            if (local.equals("전체 지역") || local.equals("전체")) {
                                filteredPlogging.add(ploggings.get(i));
                            }
                            else {
                                if (ploggings.get(i).getSigun().contains(local)) {
                                    filteredPlogging.add(ploggings.get(i));
                                }
                            }
                            PloggingAdapter adapter = new PloggingAdapter(PloggingActivity.this, filteredPlogging);
                            list.setLayoutManager(new LinearLayoutManager(PloggingActivity.this, RecyclerView.VERTICAL, false));
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

                            adapter.setOnItemClickListener(new PloggingAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int i) {
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("plogging", (Serializable) filteredPlogging.get(i));
                                    bundle.putFloat("width", dpWidth);

                                    PloggingFragment ploggingFragment = new PloggingFragment();
                                    ploggingFragment.setArguments(bundle);

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container_plogging, ploggingFragment);
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
