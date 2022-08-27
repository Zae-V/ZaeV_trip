package com.example.ZaeV_trip.Festival;

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

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Festival.FestivalAdapter;
import com.example.ZaeV_trip.model.Festival;
import com.example.ZaeV_trip.util.getXmlData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FestivalActivity extends AppCompatActivity {

    ArrayList<Festival> festivals = new ArrayList<>();
    String local;

    RecyclerView festivalList;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            local = extras.getString("local");
        }
        setContentView(R.layout.activity_festival);


        festivalList = findViewById(R.id.festivalList);
        searchView = findViewById(R.id.searchBar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                festivals = getXmlData.getFestivalData(FestivalActivity.this);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Festival> filteredFestival = new ArrayList<Festival>();

                        for(int i = 0; i< festivals.size(); i++) {
                            if(local.equals("전체 지역") || local.equals("전체")){
//                                adapter.addItem(festivals.get(i));
                                filteredFestival.add(festivals.get(i));
                            }else{
                                if(festivals.get(i).getAddr1().split(" ").length > 1 && festivals.get(i).getAddr1().split(" ")[1].equals(local)){
//                                    adapter.addItem(festivals.get(i));
                                    filteredFestival.add(festivals.get(i));
                                }
                            }
                        }
                        FestivalAdapter adapter = new FestivalAdapter(FestivalActivity.this, filteredFestival);
                        festivalList.setLayoutManager(new LinearLayoutManager(FestivalActivity.this, RecyclerView.VERTICAL, false));
                        festivalList.setAdapter(adapter);

                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                adapter.getFilter().filter(query);

                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {

                                return false;
                            }
                        });

                        adapter.setOnItemClickListener(new FestivalAdapter.OnItemClickListener() {

                            @Override
                            public void onItemClick(View view, int i) {
                                Bundle bundle = new Bundle();
//                                bundle.putString("name", festivals.get(i).getTitle());
//                                bundle.putString("location", festivals.get(i).getAddr1());
//                                bundle.putString("startDate", festivals.get(i).getStartDate());
//                                bundle.putString("endDate",festivals.get(i).getEndDate());
//                                bundle.putString("img",festivals.get(i).getFirstImage());
//                                bundle.putString("x", festivals.get(i).getMapX());
//                                bundle.putString("y",festivals.get(i).getMapY());

                                bundle.putSerializable("festival", (Serializable) filteredFestival.get(i));

                                FestivalFragment festivalFragment = new FestivalFragment();
                                festivalFragment.setArguments(bundle);

                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.container_festival, festivalFragment);
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
