package com.example.ZaeV_trip.ZeroWaste;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.ZeroWaste;
import com.example.ZaeV_trip.model.ZeroWasteDetail;
import com.example.ZaeV_trip.util.getXmlData;

import java.io.Serializable;
import java.util.ArrayList;

public class ZeroWasteActivity extends AppCompatActivity {

    ArrayList<ZeroWaste> zeroWastes = new ArrayList<ZeroWaste>();

    ArrayList<ZeroWaste> filteredZeroWasteContentsListALL = new ArrayList<ZeroWaste>();
    ArrayList<ZeroWasteDetail> filteredZeroWasteDetail = new ArrayList<ZeroWasteDetail>();

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
                zeroWastes = getXmlData.getZeroWasteListAll(ZeroWasteActivity.this);
                for(int i = 0; i< zeroWastes.size(); i++) {
                    filteredZeroWasteContentsListALL.add(zeroWastes.get(i));

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
                            for (int k = 0; k < zeroWastes.size(); k++) {
                                ZeroWasteDetail item = getXmlData.getZeroWasteDetail(ZeroWasteActivity.this, String.valueOf(filteredZeroWasteContentsListALL.get(k).getContentID()));
                                filteredZeroWasteDetail.add(item);
                                Log.d("sentence", String.valueOf(item.getContents_01()));
                                //Log.d("testImage", String.valueOf(filteredZeroWasteDetail.get(k).getImage()));
//                                Log.d("testImage", String.valueOf(filteredZeroWasteDetail.get(k).getThemeSubID()));
//                                Log.d("testImage", String.valueOf(filteredZeroWasteDetail.get(k).getMapX()));
                            }
                        }
                    });

                    contentsListDetailThread.start();
                    contentsListDetailThread.join();

                    DrawInAdapter(filteredZeroWasteDetail);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }, 3000);

    }
    public void DrawInAdapter(ArrayList<ZeroWasteDetail> items){
        ZeroWasteAdapter adapter = new ZeroWasteAdapter(ZeroWasteActivity.this, filteredZeroWasteDetail);
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

                bundle.putSerializable("zeroWaste", (Serializable) filteredZeroWasteDetail.get(i));

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
