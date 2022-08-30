package com.example.ZaeV_trip.Festival;

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

import com.example.ZaeV_trip.ProgressDialog;
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
    public static TextView notDataText;
    public static ImageView notDataImage;

    ProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        customProgressDialog = new ProgressDialog(this);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customProgressDialog.show();

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float dpWidth = outMetrics.widthPixels;

        if(extras != null){
            local = extras.getString("local");
        }
        setContentView(R.layout.activity_festival);


        festivalList = findViewById(R.id.festivalList);
        searchView = findViewById(R.id.searchBar);
        notDataText = findViewById(R.id.notDataText);
        notDataImage = findViewById(R.id.notDataImage);

        new Thread(new Runnable() {
            @Override
            public void run() {
                festivals = getXmlData.getFestivalData(FestivalActivity.this);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Festival> filteredFestival = new ArrayList<Festival>();
                        customProgressDialog.dismiss();

                        for(int i = 0; i< festivals.size(); i++) {
                            if(local.equals("전체 지역") || local.equals("전체")){
                                filteredFestival.add(festivals.get(i));
                            }else{
                                if(festivals.get(i).getAddr1().split(" ").length > 1 && festivals.get(i).getAddr1().split(" ")[1].equals(local)){
                                    filteredFestival.add(festivals.get(i));
                                }
                            }
                        }
                        FestivalAdapter adapter = new FestivalAdapter(FestivalActivity.this, filteredFestival);
                        festivalList.setLayoutManager(new LinearLayoutManager(FestivalActivity.this, RecyclerView.VERTICAL, false));
                        festivalList.setAdapter(adapter);

                        if(adapter.getItemCount() == 0){
                            notDataImage.setVisibility(View.VISIBLE);
                            notDataText.setVisibility(View.VISIBLE);
                            Log.d("어댑터 테스트", String.valueOf(adapter.getItemCount()));
                        }

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


                        adapter.setOnItemClickListener(new FestivalAdapter.OnItemClickListener() {

                            @Override
                            public void onItemClick(View view, int i) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("festival", (Serializable) filteredFestival.get(i));
                                bundle.putFloat("width", dpWidth);

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
