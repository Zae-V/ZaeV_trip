package com.zaev.ZaeV_trip.ZeroWaste;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.ProgressDialog;
import com.zaev.ZaeV_trip.model.ZeroWaste;
import com.zaev.ZaeV_trip.util.getXmlData;

import java.io.Serializable;
import java.util.ArrayList;

public class ZeroWasteActivity extends AppCompatActivity {

    ArrayList<ZeroWaste> zeroWastes1 = new ArrayList<ZeroWaste>();

    ArrayList<ZeroWaste> filteredZeroWasteContentsListALL = new ArrayList<ZeroWaste>();
    ArrayList<ZeroWaste> filteredZeroWasteMainDetail = new ArrayList<ZeroWaste>();

    String local;
    SearchView searchView;
    RecyclerView list;
    public static TextView notDataText;
    public static ImageView notDataImage;

    float dpWidth;
    ProgressDialog customProgressDialog;

    ZeroWasteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zero_waste);

        list = (RecyclerView) findViewById(R.id.zeroWasteList);
        searchView = findViewById(R.id.zeroWasteSearchBar);
        notDataText = findViewById(R.id.notDataText);
        notDataImage = findViewById(R.id.notDataImage);
        Bundle extras = getIntent().getExtras();

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        dpWidth = outMetrics.widthPixels;

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

        customProgressDialog = new ProgressDialog(this);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customProgressDialog.show();

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
                                if(local.equals("전체 지역") || local.equals("전체")) {
                                    filteredZeroWasteMainDetail.add(item);
                                }
                                else{
                                    if(item.getAddr1().contains(local)){
                                        filteredZeroWasteMainDetail.add(item);
                                    }
                                }

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

            }
        }, 3000);




    }
    public void DrawInAdapter(ArrayList<ZeroWaste> items){
        adapter = new ZeroWasteAdapter(ZeroWasteActivity.this, items);
        list.setLayoutManager(new LinearLayoutManager(ZeroWasteActivity.this, RecyclerView.VERTICAL, false));
        list.setAdapter(adapter);

        if(adapter.getItemCount() == 0){
            notDataImage.setVisibility(View.VISIBLE);
            notDataText.setVisibility(View.VISIBLE);
            Log.d("어댑터 테스트", String.valueOf(adapter.getItemCount()));
        }

        customProgressDialog.dismiss();

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


        adapter.setOnItemClickListener(new ZeroWasteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int i) {
                Bundle bundle = new Bundle();

                bundle.putSerializable("zeroWaste", (Serializable) filteredZeroWasteMainDetail.get(i));
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