package com.example.ZaeV_trip.ZeroWaste;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
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
import com.example.ZaeV_trip.model.ZeroWaste;
import com.example.ZaeV_trip.util.getXmlData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ZeroWasteActivity extends AppCompatActivity {

    ArrayList<ZeroWaste> zeroWastes = new ArrayList<>();
    String local;
    SearchView searchView;
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zero_waste);

        list = (RecyclerView) findViewById(R.id.zeroWasteList);
        searchView = findViewById(R.id.zeroWasteSearchBar);
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
                zeroWastes = getXmlData.getZeroWasteData(ZeroWasteActivity.this, "4");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<ZeroWaste> filteredZeroWaste = new ArrayList<ZeroWaste>();

                        for(int i = 0; i< zeroWastes.size(); i++) {
//                            if (local.equals("전체 지역") || local.equals("전체")) {
//                                filteredZeroWaste.add(zeroWastes.get(i));
//                            }
//                            else {
//                                if (zeroWastes.get(i).getAddr1().contains(local)) {
//                                    filteredZeroWaste.add(zeroWastes.get(i));
//                                }
//                            }
                            filteredZeroWaste.add(zeroWastes.get(i));

                            ZeroWasteAdapter adapter = new ZeroWasteAdapter(ZeroWasteActivity.this, filteredZeroWaste);
                            list.setLayoutManager(new LinearLayoutManager(ZeroWasteActivity.this, RecyclerView.VERTICAL, false));
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

                            adapter.setOnItemClickListener(new ZeroWasteAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int i) {
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("zeroWaste", (Serializable) filteredZeroWaste.get(i));
                                    bundle.putFloat("width", dpWidth);

                                    ZeroWasteFragment zeroWasteFragment = new ZeroWasteFragment();
                                    zeroWasteFragment.setArguments(bundle);

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container_zero_waste, zeroWasteFragment);
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
