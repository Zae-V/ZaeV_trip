package com.example.ZaeV_trip.Lodging;

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
import com.example.ZaeV_trip.Lodging.LodgingAdapter;
import com.example.ZaeV_trip.Lodging.LodgingFragment;
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
    public static TextView notDataText;
    public static ImageView notDataImage;
    ProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lodging);

        list = (RecyclerView) findViewById(R.id.lodgingList);
        searchView = findViewById(R.id.lodgingSearchBar);
        notDataText = findViewById(R.id.notDataText);
        notDataImage = findViewById(R.id.notDataImage);

        Bundle extras = getIntent().getExtras();

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float dpWidth = outMetrics.widthPixels;

        customProgressDialog = new ProgressDialog(this);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        if (extras != null) {
            local = extras.getString("local");
        }

        customProgressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                lodgings = getXmlData.getLodgingData(com.example.ZaeV_trip.Lodging.LodgingActivity.this);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Lodging> filteredLodging = new ArrayList<Lodging>();
                        customProgressDialog.dismiss();

                        for (int i = 0; i < lodgings.size(); i++) {
                            if (local.equals("전체 지역") || local.equals("전체")) {
                                filteredLodging.add(lodgings.get(i));
                            } else {
                                if (lodgings.get(i).getAddr1().contains(local)) {
                                    filteredLodging.add(lodgings.get(i));
                                }
                            }
                        }
                        LodgingAdapter adapter = new LodgingAdapter(com.example.ZaeV_trip.Lodging.LodgingActivity.this, filteredLodging);
                        list.setLayoutManager(new LinearLayoutManager(com.example.ZaeV_trip.Lodging.LodgingActivity.this, RecyclerView.VERTICAL, false));
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

                        if (adapter.getItemCount() == 0) {
                            notDataImage.setVisibility(View.VISIBLE);
                            notDataText.setVisibility(View.VISIBLE);
                            Log.d("어댑터 테스트", String.valueOf(adapter.getItemCount()));
                        }

                        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                            @Override
                            public boolean onClose() {
                                adapter.getFilter().filter(null);
                                return false;
                            }
                        });

                        // 서치아이콘이 아닌 서치바 클릭시 검색 가능하게 하기
                        searchView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
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


                        adapter.setOnItemClickListener(new LodgingAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int i) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("lodging", (Serializable) filteredLodging.get(i));
                                bundle.putFloat("width", dpWidth);

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
                });
            }
        }).start();

    }


}
