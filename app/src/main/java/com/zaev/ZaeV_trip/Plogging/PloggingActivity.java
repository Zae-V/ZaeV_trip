package com.zaev.ZaeV_trip.Plogging;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.zaev.ZaeV_trip.ProgressDialog;
import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.model.Plogging;
import com.zaev.ZaeV_trip.util.getXmlData;

import java.io.Serializable;
import java.util.ArrayList;

public class PloggingActivity extends AppCompatActivity {
    ArrayList<Plogging> ploggings = new ArrayList<>();
    String local;
    SearchView searchView;
    RecyclerView list;
    ProgressDialog customProgressDialog;
    public static TextView notDataText;
    public static ImageView notDataImage;


    PloggingAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plogging);

        list = (RecyclerView) findViewById(R.id.ploggingList);
        searchView = findViewById(R.id.ploggingSearchBar);
        notDataText = findViewById(R.id.notDataText);
        notDataImage = findViewById(R.id.notDataImage);

        Bundle extras = getIntent().getExtras();

        customProgressDialog = new ProgressDialog(this);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customProgressDialog.show();

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float dpWidth = outMetrics.widthPixels;

        if (extras != null) {
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
                        customProgressDialog.dismiss();

                        for (int i = 0; i < ploggings.size(); i++) {
                            if (local.equals("전체 지역") || local.equals("전체")) {
                                filteredPlogging.add(ploggings.get(i));
                            } else {
                                if (ploggings.get(i).getSigun().contains(local)) {
                                    filteredPlogging.add(ploggings.get(i));
                                }
                            }
                        }
                        adapter = new PloggingAdapter(PloggingActivity.this, filteredPlogging);
                        list.setLayoutManager(new LinearLayoutManager(PloggingActivity.this, RecyclerView.VERTICAL, false));
                        list.setAdapter(adapter);

                        if (adapter.getItemCount() == 0) {
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
                });
            }
        }).start();
    }

}
