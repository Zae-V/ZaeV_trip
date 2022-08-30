package com.example.ZaeV_trip.TouristSpot;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ZaeV_trip.Festival.FestivalFragment;
import com.example.ZaeV_trip.ProgressDialog;
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
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class TouristSpotActivity extends AppCompatActivity {

    ArrayList<TouristSpot> touristSpots = new ArrayList<>();
    String local;
    SearchView searchView;
    RecyclerView list;
    public static TextView notDataText;
    public static ImageView notDataImage;
    ProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_spot);

        list = (RecyclerView) findViewById(R.id.touristSpotList);
        searchView = findViewById(R.id.touristSpotSearchBar);
        notDataText = findViewById(R.id.notDataText);
        notDataImage = findViewById(R.id.notDataImage);
        Bundle extras = getIntent().getExtras();

        //로딩창 객체 생성
        customProgressDialog = new ProgressDialog(this);
        //로딩창을 투명하게
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float dpWidth = outMetrics.widthPixels;

        if(extras!=null){
            local = extras.getString("local");
        }

        customProgressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                touristSpots = getXmlData.getTourSpotData(TouristSpotActivity.this);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        customProgressDialog.dismiss();
                        ArrayList<TouristSpot> filteredTouristSpot = new ArrayList<TouristSpot>();

                        for(int i = 0; i< touristSpots.size(); i++) {
                            if (local.equals("전체 지역") || local.equals("전체")) {
                                filteredTouristSpot.add(touristSpots.get(i));
                            } else {
                                if (touristSpots.get(i).getAddr1().contains(local)) {
                                    filteredTouristSpot.add(touristSpots.get(i));
                                }
                            }
                        }
                        TouristSpotAdapter adapter = new TouristSpotAdapter(TouristSpotActivity.this, filteredTouristSpot);
                        list.setLayoutManager(new LinearLayoutManager(TouristSpotActivity.this, RecyclerView.VERTICAL, false));
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
                                notDataImage.setVisibility(View.GONE);
                                notDataText.setVisibility(View.GONE);

                            }
                        });


                        adapter.setOnItemClickListener(new TouristSpotAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int i) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("touristSpot", (Serializable) filteredTouristSpot.get(i));
                                bundle.putFloat("width", dpWidth);

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
                });
            }
        }).start();

    }


}
