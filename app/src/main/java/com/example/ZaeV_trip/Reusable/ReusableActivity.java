package com.example.ZaeV_trip.Reusable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ZaeV_trip.Cafe.CafeActivity;
import com.example.ZaeV_trip.Cafe.CafeAdapter;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Schedule.AddScheduleFragment;
import com.example.ZaeV_trip.Schedule.CalendarFragment;
import com.example.ZaeV_trip.Schedule.OnTravelItemClickListener;
import com.example.ZaeV_trip.Schedule.ScheduleFragment;
import com.example.ZaeV_trip.Schedule.TravelActivity;
import com.example.ZaeV_trip.Schedule.TravelItem;
import com.example.ZaeV_trip.Schedule.TravelListAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ReusableActivity extends AppCompatActivity {
    GridView gridView;

    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> locations = new ArrayList<String>();
    ArrayList<String> x = new ArrayList<String>(); //경도
    ArrayList<String> y = new ArrayList<String >(); //위도
    ArrayList<String> reasons = new ArrayList<String>();


    String local;

    public interface ExtraKey {
        String IMAGE_RES_ID = "ImageResourceId";
    }


    public interface ReusableItemClickListener {
        void onReusableItemClick(String a_name) ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> filteredName = new ArrayList<String>();
        ArrayList<String> filteredLocation = new ArrayList<String>();
        ArrayList<String> filteredReasons = new ArrayList<String>();
        ArrayList<String> filteredX = new ArrayList<>();
        ArrayList<String> filteredY = new ArrayList<>();

        setContentView(R.layout.activity_cafe);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            local = extras.getString("local");
        }
        Log.i("test", "액티비티 생성");

        readExcel();

        List<ReusableItem> itemList = new ArrayList<>();

        for(int i = 0; i< names.size(); i++){
            filteredName.add(names.get(i));
            filteredLocation.add(locations.get(i));
            filteredReasons.add(reasons.get(i));
            filteredX.add(x.get(i));
            filteredY.add(y.get(i));
            itemList.add(new ReusableItem(filteredName.get(i), filteredLocation.get(i), filteredX.get(i),filteredY.get(i),filteredReasons.get(i)));
        }
        Log.i("test", String.valueOf(filteredName));
        Log.i("test", String.valueOf(filteredLocation));
        Log.i("test", String.valueOf(filteredReasons));

        gridView = (GridView) findViewById(R.id.cafeList);

        ReusableAdapter adapter = new ReusableAdapter(this, itemList);
        adapter.setReusableItemClickListener(new ReusableItemClickListener(){
            @Override
            public void onReusableItemClick(String a_name) {

            }

//            @Override
//            public void onReusableItemClick(int a_imageResId){
////                Intent intent = new Intent(ReusableActivity.this, DetailReusableFragement.class);
////                intent.putExtra(ExtraKey.IMAGE_RES_ID, a_imageResId);
////                startActivity(intent);
//            }
        });
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> a_parent, View a_view, int a_position, long a_id) {
                final ReusableItem item = (ReusableItem) adapter.getItem(a_position);
                Toast.makeText(ReusableActivity.this, item.getName() + " Click event", Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                DetailReusableFragment detailReusableFragment = new DetailReusableFragment();
                fragmentTransaction.replace(R.id.container_cafe, detailReusableFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



//        gridView.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> a_parent, View a_view, int a_position, long a_id) {
//                final ReusableItem item = (ReusableItem) ReusableAdapter.getItem(a_position);
//                Toast.makeText(ReusableActivity.this, item.getName() + " Click event", Toast.LENGTH_SHORT).show();
//            }
//        });
        TextView textView = findViewById(R.id.cafeText);
        textView.setText("다회용기");
        ImageView imageView = findViewById(R.id.cafeImageView);
        imageView.setImageResource(R.drawable.reuse_img);
    }

    public void readExcel() {

        try {
            Log.i("test", "트라이");
            InputStream is = getBaseContext().getResources().getAssets().open("noplasticstore.bom.xls");
            Workbook wb = Workbook.getWorkbook(is);

            if (wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if (sheet != null) {
                    Log.i("test", "불러오기");
                    int colTotal = sheet.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    Log.i("test", "전체 칼럼: " + colTotal);
                    int rowTotal = sheet.getColumn(colTotal - 2).length;
                    Log.i("test", "전체 로우: "+ rowTotal);

                    StringBuilder sb;
                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        sb = new StringBuilder();
                        for (int col = 0; col < colTotal; col++) {
                            String contents = sheet.getCell(col, row).getContents();
                            if(col == 0){
                                names.add(contents);
                            }
                            else if(col==1){
                                locations.add(contents);
                            }
                            else if(col==2){
                                y.add(contents);
                            }
                            else if(col==3){
                                x.add(contents);
                            }
                            else if(col==4){
                                reasons.add(contents);
                            }
                            sb.append("col" + col + " : " + contents + " , ");
                        }
//                        Log.i("test", sb.toString());
                    }
                }
            }
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
