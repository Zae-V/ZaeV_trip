package com.example.ZaeV_trip.Lodging;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.Lodging;
import com.example.ZaeV_trip.util.getXmlData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class LodgingActivity extends AppCompatActivity {
    ArrayList<Lodging> lodgings = new ArrayList<>();
    String local;

    SearchView searchView;
    RecyclerView list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lodging);

        list = (RecyclerView) findViewById(R.id.LodgingList);
        searchView = findViewById(R.id.lodgingSearchBar);
        Bundle extras = getIntent().getExtras();

        if(extras!=null){
            local = extras.getString("local");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                lodgings = getXmlData.getLodgingData(LodgingActivity.this);

                runOnUiThread(new Runnable() {
                    ArrayList<Lodging> filteredLodging = new ArrayList<Lodging>();

                    @Override
                    public void run() {
                        for(int i = 0; i< lodgings.size(); i++) {
                            if (local.equals("전체 지역") || local.equals("전체")) {
                                filteredLodging.add(lodgings.get(i));
                            }
                            else {
                                if (lodgings.get(i).getAddr1().contains(local)) {
                                    filteredLodging.add(lodgings.get(i));
                                }
                            }
                            LodgingAdapter adapter = new LodgingAdapter(LodgingActivity.this, filteredLodging);
                            list.setLayoutManager(new LinearLayoutManager(LodgingActivity.this, RecyclerView.VERTICAL, false));
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

                            adapter.setOnItemClickListener(new LodgingAdapter.OnItemClickListener() {
                                    @Override
                                        public void onItemClick(View v, int i) {
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("lodging", (Serializable) filteredLodging.get(i));

                                            LodgingFragment lodgingFragment = new LodgingFragment();
                                            lodgingFragment.setArguments(bundle);

                                            FragmentManager fragmentManager = getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.container_lodging, lodgingFragment);
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
