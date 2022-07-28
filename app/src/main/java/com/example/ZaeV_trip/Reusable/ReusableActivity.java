package com.example.ZaeV_trip.Reusable;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ZaeV_trip.Cafe.CafeActivity;
import com.example.ZaeV_trip.Cafe.CafeAdapter;
import com.example.ZaeV_trip.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> filteredName = new ArrayList<String>();
        ArrayList<String> filteredLocation = new ArrayList<String>();
        ArrayList<String> filteredReasons = new ArrayList<String>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            local = extras.getString("local");
        }
        Log.i("test", "액티비티 생성");

        readExcel();

        for(int i = 0; i< names.size(); i++){
            filteredName.add(names.get(i));
            filteredLocation.add(locations.get(i));
            filteredReasons.add(reasons.get(i));
        }
        Log.i("test", String.valueOf(filteredName));
        Log.i("test", String.valueOf(filteredLocation));
        Log.i("test", String.valueOf(filteredReasons));

        setContentView(R.layout.activity_cafe);
        gridView = (GridView) findViewById(R.id.cafeList);

        ReusableAdapter adapter = new ReusableAdapter(ReusableActivity.this ,filteredName, filteredLocation, filteredReasons);
        gridView.setAdapter(adapter);

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
