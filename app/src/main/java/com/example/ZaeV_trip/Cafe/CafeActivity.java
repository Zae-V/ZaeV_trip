package com.example.ZaeV_trip.Cafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Filter;

import com.example.ZaeV_trip.ProgressDialog;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.Cafe;
import com.example.ZaeV_trip.util.getXmlData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class CafeActivity extends AppCompatActivity {
    RecyclerView cafeList;
    ArrayList<Cafe> cafes = new ArrayList<>();
    SearchView searchView;

    String local;
    ProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cafe);
        cafeList = (RecyclerView) findViewById(R.id.cafeList);
        searchView = findViewById(R.id.searchBar);

        customProgressDialog = new ProgressDialog(this);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customProgressDialog.show();

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float dpWidth = outMetrics.widthPixels;

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            local = extras.getString("local");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                cafes = getXmlData.getCafeData(CafeActivity.this);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        CafeAdapter cafeAdapter = new CafeAdapter(CafeActivity.this);
                        ArrayList<Cafe> filterdList = new ArrayList<Cafe>();
                        customProgressDialog.dismiss();

                        for(int i = 0; i< cafes.size();i++){
                            if(cafes.get(i).getCategory() != null && (cafes.get(i).getCategory().equals("카페") || cafes.get(i).getCategory().equals("베이커리") || cafes.get(i).getCategory().equals("까페"))){
                                if(local.equals("전체 지역") || local.equals("전체")){
                                    filterdList.add(cafes.get(i));
//                                    cafeAdapter.addItem(cafes.get(i));
                                }
                                else{
                                    if(cafes.get(i).getLocation().split(" ")[1].equals(local)){
                                        filterdList.add(cafes.get(i));
                                    }
                                }
                            }
                        }

                        CafeAdapter cafeAdapter = new CafeAdapter(CafeActivity.this ,filterdList);
                        cafeList.setLayoutManager(new LinearLayoutManager(CafeActivity.this, RecyclerView.VERTICAL,false));
                        cafeList.setAdapter(cafeAdapter);

                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                cafeAdapter.getFilter().filter(query);

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
                                cafeAdapter.getFilter().filter(null);
                                return false;
                            }
                        });



                        cafeAdapter.setOnItemClickListener(new CafeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int i) {
                                Bundle bundle = new Bundle();
                                bundle.putString("id",filterdList.get(i).getId());
                                bundle.putString("name", filterdList.get(i).getName());
                                bundle.putString("location",filterdList.get(i).getLocation());
                                bundle.putString("category", filterdList.get(i).getCategory());
                                bundle.putString("x", filterdList.get(i).getMapX());
                                bundle.putString("y",filterdList.get(i).getMapY());
                                bundle.putString("number",filterdList.get(i).getNumber());
                                bundle.putString("menu",filterdList.get(i).getMenu());
                                bundle.putFloat("width", dpWidth);

                                CafeFragment cafeFragment = new CafeFragment();
                                cafeFragment.setArguments(bundle);

                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.container_cafe, cafeFragment);
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
