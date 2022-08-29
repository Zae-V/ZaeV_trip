package com.example.ZaeV_trip.ZeroWaste;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
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

import com.example.ZaeV_trip.Intro.IntroActivity;
import com.example.ZaeV_trip.Intro.SplashActivity;
import com.example.ZaeV_trip.Main.MainActivity;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.ZeroWaste;
import com.example.ZaeV_trip.util.MySharedPreferences;
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

    ArrayList<ZeroWaste> zeroWastes1 = new ArrayList<ZeroWaste>();
    ArrayList<ZeroWaste> zeroWastes2 = new ArrayList<ZeroWaste>();

    ArrayList<ZeroWaste> filteredZeroWasteContentsListALL = new ArrayList<ZeroWaste>();
    ArrayList<ZeroWaste> filteredZeroWasteMainDetail = new ArrayList<ZeroWaste>();

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

        if(extras!=null){
            local = extras.getString("local");
        }

        Thread contentsListAllThread = new Thread(new Runnable() {
            @Override
            public void run() {
                zeroWastes1 = getXmlData.getZeroWasteListAll(ZeroWasteActivity.this);
                for(int i = 0; i< zeroWastes1.size(); i++) {
                    filteredZeroWasteContentsListALL.add(zeroWastes1.get(i));
                }

            }
        });

        contentsListAllThread.start();
        contentsListAllThread.isAlive();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {

                    contentsListAllThread.join();
                    Thread contentsListDetailThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int k = 0; k < zeroWastes1.size(); k++) {
                                ZeroWaste item = getXmlData.getZeroWasteMainDetail(ZeroWasteActivity.this, String.valueOf(filteredZeroWasteContentsListALL.get(k).getContentID()));
                                filteredZeroWasteMainDetail.add(item);

                            }
                        }
                    });

                    contentsListDetailThread.start();
                    contentsListDetailThread.join();

                    Log.e("array", String.valueOf(filteredZeroWasteMainDetail));

                    DrawInAdapter(filteredZeroWasteMainDetail);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                finish();
            }
        }, 3000);




    }
    public void DrawInAdapter(ArrayList<ZeroWaste> items){
        ZeroWasteAdapter adapter = new ZeroWasteAdapter(ZeroWasteActivity.this, filteredZeroWasteMainDetail);
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

                bundle.putSerializable("zeroWaste", (Serializable) filteredZeroWasteMainDetail.get(i));

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
