package com.example.ZaeV_trip.ZeroWaste;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.ZeroWaste;
import com.example.ZaeV_trip.util.getXmlData;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

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

        if(extras!=null){
            local = extras.getString("local");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                zeroWastes = getXmlData.getZeroWasteData(ZeroWasteActivity.this);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<ZeroWaste> filteredZeroWaste = new ArrayList<ZeroWaste>();

                        for(int i = 0; i< zeroWastes.size(); i++) {
                            if (local.equals("전체 지역") || local.equals("전체")) {
                                filteredZeroWaste.add(zeroWastes.get(i));
                            }
                            else {
                                if (zeroWastes.get(i).getLocation().contains(local)) {
                                    filteredZeroWaste.add(zeroWastes.get(i));
                                }
                            }
                            ZeroWasteAdapter adapter = new com.example.ZaeV_trip.ZeroWaste.ZeroWasteAdapter(ZeroWasteActivity.this, filteredZeroWaste);
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
                            adapter.setOnItemClickListener(new com.example.ZaeV_trip.ZeroWaste.ZeroWasteAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int i) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("name", filteredZeroWaste.get(i).getName());
                                    bundle.putString("location", filteredZeroWaste.get(i).getLocation());
                                    bundle.putString("x", filteredZeroWaste.get(i).getMapX());
                                    bundle.putString("y", filteredZeroWaste.get(i).getMapY());
                                    bundle.putString("reason",filteredZeroWaste.get(i).getReason());

                                    ZeroWasteFragment zeroWasteFragment = new com.example.ZaeV_trip.ZeroWaste.ZeroWasteFragment();
                                    zeroWasteFragment.setArguments(bundle);

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container_zeroWaste, zeroWasteFragment);
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
