package com.example.ZaeV_trip.Reusable;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Restaurant.RestaurantAdapter;
import com.example.ZaeV_trip.Restaurant.RestaurantFragment;
import com.example.ZaeV_trip.model.Restaurant;
import com.example.ZaeV_trip.model.Reusable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ReusableActivity extends AppCompatActivity {
    ArrayList<Reusable> reusables = new ArrayList<>();
    String local;
    SearchView searchView;
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reusable);

        list = (RecyclerView) findViewById(R.id.reusableList);
        searchView = findViewById(R.id.reusableSearchBar);
        Bundle extras = getIntent().getExtras();

        if(extras!=null){
            local = extras.getString("local");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                reusables = readExcel();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Reusable> filteredReusable = new ArrayList<Reusable>();

                        for(int i = 0; i< reusables.size(); i++) {
                            if (local.equals("전체 지역") || local.equals("전체")) {
                                filteredReusable.add(reusables.get(i));
                            }
                            else {
                                if (reusables.get(i).getLocation().contains(local)) {
                                    filteredReusable.add(reusables.get(i));
                                }
                            }
                            ReusableAdapter adapter = new ReusableAdapter(ReusableActivity.this, filteredReusable);
                            list.setLayoutManager(new LinearLayoutManager(ReusableActivity.this, RecyclerView.VERTICAL, false));
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
                            adapter.setOnItemClickListener(new ReusableAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int i) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("name", filteredReusable.get(i).getName());
                                    bundle.putString("location", filteredReusable.get(i).getLocation());
                                    bundle.putString("x", filteredReusable.get(i).getMapX());
                                    bundle.putString("y", filteredReusable.get(i).getMapY());
                                    bundle.putString("reason",filteredReusable.get(i).getReason());

                                    ReusableFragment reusableFragment = new ReusableFragment();
                                    reusableFragment.setArguments(bundle);

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container_reusable, reusableFragment);
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

    public ArrayList<Reusable> readExcel() {
        ArrayList<Reusable> reusables = new ArrayList<Reusable>();

        try {
            InputStream is = getBaseContext().getResources().getAssets().open("noplasticstore.bom.xls");
            Workbook wb = Workbook.getWorkbook(is);
            Reusable reusable = null;
            if (wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if (sheet != null) {
                    Log.i("test", "불러오기");
                    int colTotal = sheet.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    Log.i("test", "전체 칼럼: " + colTotal);
                    int rowTotal = sheet.getColumn(colTotal - 2).length;
                    Log.i("test", "전체 로우: " + rowTotal);

                    StringBuilder sb;
                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        sb = new StringBuilder();
                        reusable = new Reusable(
                                "",
                                "",
                                "",
                                "",
                                ""
                        );
                        for (int col = 0; col < colTotal; col++) {
                            String contents = sheet.getCell(col, row).getContents();
                            if (col == 0) {
                                reusable.setName(contents);
                            } else if (col == 1) {
                                reusable.setLocation(contents);
                            } else if (col == 2) {
                                reusable.setMapY(contents);
                            } else if (col == 3) {
                                reusable.setMapX(contents);
                            } else if (col == 4) {
                                reusable.setReason(contents);
                            }
                            sb.append("col" + col + " : " + contents + " , ");

                        }
                        reusables.add(reusable);
//                        Log.i("test", sb.toString());
                    }

                }
            }

        }
        catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reusables;
    }


}
