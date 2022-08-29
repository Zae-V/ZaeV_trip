package com.example.ZaeV_trip.Reusable;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Restaurant.RestaurantAdapter;
import com.example.ZaeV_trip.Restaurant.RestaurantFragment;
import com.example.ZaeV_trip.model.Restaurant;
import com.example.ZaeV_trip.model.Reusable;
import com.example.ZaeV_trip.util.getXmlData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ReusableActivity extends AppCompatActivity {
    ArrayList<Reusable> reusables = new ArrayList<>();
    String local;
    SearchView searchView;
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reusable);

        list = (RecyclerView) findViewById(R.id.reusableList);
        searchView = findViewById(R.id.reusableSearchBar);
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
                reusables = getXmlData.getResusableData(ReusableActivity.this);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Reusable> filteredReusable = new ArrayList<Reusable>();

                        for(int i = 0; i< reusables.size(); i++) {
                            if (local.equals("전체 지역") || local.equals("전체")) {
                                filteredReusable.add(reusables.get(i));
                            }
                            else {
                                if (reusables.get(i).getLocation().contains(local)) {
                                    filteredReusable.add(reusables.get(i));
                                }
                            }
                            ReusableAdapter adapter = new ReusableAdapter(ReusableActivity.this, filteredReusable);
                            list.setLayoutManager(new LinearLayoutManager(ReusableActivity.this, RecyclerView.VERTICAL, false));
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
                            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                                @Override
                                public boolean onClose() {
                                    adapter.getFilter().filter(null);
                                    return false;
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

                                }
                            });

                            adapter.setOnItemClickListener(new ReusableAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int i) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("name", filteredReusable.get(i).getName());
                                    bundle.putString("location", filteredReusable.get(i).getLocation());
                                    bundle.putString("x", filteredReusable.get(i).getMapX());
                                    bundle.putString("y", filteredReusable.get(i).getMapY());
                                    bundle.putString("reason",filteredReusable.get(i).getReason());
                                    bundle.putFloat("width", dpWidth);

                                    ReusableFragment reusableFragment = new ReusableFragment();
                                    reusableFragment.setArguments(bundle);

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container_reusable, reusableFragment);
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
