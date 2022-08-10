package com.example.ZaeV_trip.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ZaeV_trip.Bookmark.BookmarkActivity;
import com.example.ZaeV_trip.Cafe.CafeActivity;
import com.example.ZaeV_trip.Festival.FestivalActivity;
import com.example.ZaeV_trip.Lodging.LodgingActivity;
import com.example.ZaeV_trip.Main.Bike.BikeAdapter;
import com.example.ZaeV_trip.Plogging.PloggingActivity;
import com.example.ZaeV_trip.Plogging.PloggingAdapter;
import com.example.ZaeV_trip.Plogging.PloggingFragment;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Restaurant.RestaurantActivity;
import com.example.ZaeV_trip.Reusable.ReusableActivity;
import com.example.ZaeV_trip.Schedule.TravelActivity;
import com.example.ZaeV_trip.Search.SearchActivity;
import com.example.ZaeV_trip.Search.SearchFragment;
import com.example.ZaeV_trip.TouristSpot.TouristSpotActivity;
import com.example.ZaeV_trip.model.Festival;
import com.example.ZaeV_trip.model.Plogging;
import com.example.ZaeV_trip.model.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView current;
    String local;
    ArrayList<Festival> festivals = new ArrayList<>();
    ArrayList<Plogging> bikes = new ArrayList<>();
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        current = findViewById(R.id.currentPosition);
        list = (RecyclerView) findViewById(R.id.bike_course_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            local = extras.getString("current");
            current.setText(local);
        }
        local = "전체";

        Log.d("테스트", local);

        new Thread(new Runnable() {
            @Override
            public void run() {
                bikes = getXmlBikeData();

                runOnUiThread(new Runnable() {
                    ArrayList<Plogging> filteredBike = new ArrayList<Plogging>();
                    @Override
                    public void run() {
                        for(int i = 0; i< bikes.size();i++){
                            if(local.equals("전체 지역") || local.equals("전체")){
                                filteredBike.add(bikes.get(i));
                            }
                            else{
                                if(bikes.get(i).getSigun().contains(local)){
                                    filteredBike.add(bikes.get(i));
                                }
                            }
                        }
                        BikeAdapter adapter = new BikeAdapter(MainActivity.this, filteredBike);
                        list.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
                        list.setAdapter(adapter);

                        adapter.setOnItemClickListener(new BikeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int i) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("plogging", (Serializable) filteredBike.get(i));

                                PloggingFragment ploggingFragment = new PloggingFragment();
                                ploggingFragment.setArguments(bundle);

                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.main_container, ploggingFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                            }
                        });


                    }
                });

            }
        }).start();

        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SearchFragment searchFragment = new SearchFragment();
                fragmentTransaction.replace(R.id.main_container, searchFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Button cafeBtn = findViewById(R.id.cafeBtn);
        cafeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CafeActivity.class);
                intent.putExtra("local", current.getText());
                startActivity(intent);

            }
        });

        Button spotBtn = findViewById(R.id.spotBtn);
        spotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TouristSpotActivity.class);
                intent.putExtra("local", current.getText());
                startActivity(intent);
            }
        });

        Button reuseBtn = findViewById(R.id.reuseBtn);
        reuseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ReusableActivity.class);

                intent.putExtra("local", current.getText());
                startActivity(intent);

            }
        });

        Button ploggingBtn = findViewById(R.id.ploggingBtn);
        ploggingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PloggingActivity.class);

                intent.putExtra("local", current.getText());
                startActivity(intent);

            }
        });

        Button festivalBtn = findViewById(R.id.allBtn);
        festivalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FestivalActivity.class);
                intent.putExtra("local", current.getText());
                startActivity(intent);
            }
        });

        Button lodgingBtn = findViewById(R.id.hotelBtn);
        lodgingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LodgingActivity.class);
                intent.putExtra("local", current.getText());
                startActivity(intent);
            }
        });

        Button restaurantBtn = findViewById(R.id.restaurantBtn);
        restaurantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RestaurantActivity.class);
                intent.putExtra("local", current.getText());
                startActivity(intent);
            }
        });


        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);


        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.bookmark:
                    startActivity(new Intent(getApplicationContext(), BookmarkActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.travel:
                    startActivity(new Intent(getApplicationContext(), TravelActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.home:
                    return true;
            }
            return false;
        });

    }

    public ArrayList<Plogging> getXmlBikeData(){
        ArrayList<Plogging> bikes = new ArrayList<Plogging>();
//        String str= edit.getText().toString();//EditText에 작성된 Text얻어오기
//        String location = URLEncoder.encode(str);
        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";
        String key = getString(R.string.portal_key);
        String address = "http://api.visitkorea.or.kr/openapi/service/rest/Durunubi/courseList";
        String pageNo = "1";
        String numOfRows = "100";
        String mobileApp = "ZaeVTour";
        String mobileOS = "AND";

        String queryUrl = address + "?"
                + "serviceKey=" + key
                + "&pageNo=" + pageNo
                + "&numOfRows=" + numOfRows
                + "&MobileOS=" + mobileOS
                + "&MobileApp=" + mobileApp;

        try{
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance(); //xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            Plogging plogging = null;

            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) {
                            plogging = new Plogging(
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    ""
                            );
                        }
                        else if(tag.equals("crsContents")){
                            plogging.setCrsContents(xpp.nextText());
                        }
                        else if(tag.equals("crsDstnc")){
                            plogging.setCrsDstnc(xpp.nextText());
                        }
                        else if(tag.equals("crsKorNm")){
                            plogging.setCrsKorNm(xpp.nextText());
                        }
                        else if(tag.equals("crsLevel")){
                            plogging.setCrsLevel(xpp.nextText());
                        }
                        else if(tag.equals("crsSummary")){
                            plogging.setCrsSummary(xpp.nextText());
                        }
                        else if(tag.equals("crsTotlRqrmHour")){
                            plogging.setCrsTotlRqrmHour(xpp.nextText());
                        }
                        else if(tag.equals("crsTourInfo")){
                            plogging.setCrsTourInfo(xpp.nextText());
                        }
                        else if(tag.equals("sigun")){
                            plogging.setSigun(xpp.nextText());
                        }
                        else if(tag.equals("travelerinfo")){
                            plogging.setTravelerinfo(xpp.nextText());
                        }
                        else if(tag.equals("brdDiv")){
                            plogging.setBrdDiv(xpp.nextText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName();

                        if(tag.equals("item")) {
                            bikes.add(plogging);
                        }
                        break;

                }
                eventType= xpp.next();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return bikes;
    }


}