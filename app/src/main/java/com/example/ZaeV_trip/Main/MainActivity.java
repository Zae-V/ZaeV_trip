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
import com.example.ZaeV_trip.Festival.FestivalFragment;
import com.example.ZaeV_trip.Lodging.LodgingActivity;
import com.example.ZaeV_trip.Main.Bike.BikeAdapter;
import com.example.ZaeV_trip.Main.EventList.EventAdapter;
import com.example.ZaeV_trip.Plogging.PloggingActivity;
import com.example.ZaeV_trip.Plogging.PloggingAdapter;
import com.example.ZaeV_trip.Plogging.PloggingFragment;
import com.example.ZaeV_trip.Profile.ProfileActivity;
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
import com.google.type.DateTime;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView current;
    String local;
    ArrayList<Festival> eventLists = new ArrayList<>();
    ArrayList<Plogging> bikes = new ArrayList<>();
    RecyclerView bikeList;
    RecyclerView eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        current = findViewById(R.id.currentPosition);
        bikeList = (RecyclerView) findViewById(R.id.bike_course_list);
        eventList = (RecyclerView) findViewById(R.id.event_list);

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
//                eventLists = getXmlEventData();

                runOnUiThread(new Runnable() {
                    ArrayList<Plogging> filteredBike = new ArrayList<Plogging>();
//                    ArrayList<Festival> filteredEvent = new ArrayList<>();
                    @Override
                    public void run() {
                        for(int i = 0; i< bikes.size();i++){
                            if(local.equals("전체 지역") || local.equals("전체")){
                                filteredBike.add(bikes.get(i));
//                                filteredEvent.add(eventLists.get(i));
                            }
                            else{
                                if(bikes.get(i).getSigun().contains(local)){
                                    filteredBike.add(bikes.get(i));
//                                    filteredEvent.add(eventLists.get(i));
                                }
                            }
                        }
                        BikeAdapter adapter = new BikeAdapter(MainActivity.this, filteredBike);
                        bikeList.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
                        bikeList.setAdapter(adapter);

//                        EventAdapter adapterE = new EventAdapter(MainActivity.this, filteredEvent);
//                        eventList.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
//                        eventList.setAdapter(adapterE);

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

//                        adapterE.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View v, int i) {
//                                Bundle bundle = new Bundle();
//                                bundle.putString("name", filteredEvent.get(i).getTitle());
//                                bundle.putString("location", filteredEvent.get(i).getAddr1());
//                                bundle.putString("startDate", filteredEvent.get(i).getStartDate());
//                                bundle.putString("endDate",filteredEvent.get(i).getEndDate());
//                                bundle.putString("img",filteredEvent.get(i).getFirstImage());
//                                bundle.putString("x", filteredEvent.get(i).getMapX());
//                                bundle.putString("y",filteredEvent.get(i).getMapY());
//
//                                FestivalFragment festivalFragment = new FestivalFragment();
//                                festivalFragment.setArguments(bundle);
//
//                                FragmentManager fragmentManager = getSupportFragmentManager();
//                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                fragmentTransaction.replace(R.id.main_container, festivalFragment);
//                                fragmentTransaction.addToBackStack(null);
//                                fragmentTransaction.commit();
//
//                            }
//                        });



                    }
                });

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
//                bikes = getXmlBikeData();
                eventLists = getXmlEventData();

                runOnUiThread(new Runnable() {
//                    ArrayList<Plogging> filteredBike = new ArrayList<Plogging>();
                    ArrayList<Festival> filteredEvent = new ArrayList<>();
                    @Override
                    public void run() {
                        for(int i = 0; i< eventLists.size();i++){
                            if(local.equals("전체 지역") || local.equals("전체")){
//                                filteredBike.add(bikes.get(i));
                                filteredEvent.add(eventLists.get(i));
                            }
                            else{
                                if(eventLists.get(i).getAddr1().split(" ").length > 1 && eventLists.get(i).getAddr1().split(" ")[1].equals(local)){
//                                    filteredBike.add(bikes.get(i));
                                    filteredEvent.add(eventLists.get(i));
                                }
                            }
                        }
//                        BikeAdapter adapter = new BikeAdapter(MainActivity.this, filteredBike);
//                        bikeList.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
//                        bikeList.setAdapter(adapter);

                        EventAdapter adapterE = new EventAdapter(MainActivity.this, filteredEvent);
                        eventList.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
                        eventList.setAdapter(adapterE);

//                        adapter.setOnItemClickListener(new BikeAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View v, int i) {
//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable("plogging", (Serializable) filteredBike.get(i));
//
//                                PloggingFragment ploggingFragment = new PloggingFragment();
//                                ploggingFragment.setArguments(bundle);
//
//                                FragmentManager fragmentManager = getSupportFragmentManager();
//                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                fragmentTransaction.replace(R.id.main_container, ploggingFragment);
//                                fragmentTransaction.addToBackStack(null);
//                                fragmentTransaction.commit();
//
//                            }
//                        });

                        adapterE.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int i) {
                                Bundle bundle = new Bundle();
                                bundle.putString("name", filteredEvent.get(i).getTitle());
                                bundle.putString("location", filteredEvent.get(i).getAddr1());
                                bundle.putString("startDate", filteredEvent.get(i).getStartDate());
                                bundle.putString("endDate",filteredEvent.get(i).getEndDate());
                                bundle.putString("img",filteredEvent.get(i).getFirstImage());
                                bundle.putString("x", filteredEvent.get(i).getMapX());
                                bundle.putString("y",filteredEvent.get(i).getMapY());

                                FestivalFragment festivalFragment = new FestivalFragment();
                                festivalFragment.setArguments(bundle);

                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.main_container, festivalFragment);
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
                case R.id.profile:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
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

    public ArrayList<Festival> getXmlEventData(){
        ArrayList<Festival> eventLists = new ArrayList<Festival>();
        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";
        String key = getString(R.string.portal_key);
        String address = "https://api.visitkorea.or.kr/openapi/service/rest/KorService/";
        String listType = "searchFestival";
        String pageNo = "1";
        String numOfRows = "1000";
        String mobileApp = "ZaeVTour";
        String mobileOS = "AND";
        String arrange = "A"; // (A=제목순, B=조회순, C=수정일순, D=생성일순) , 대표이미지가 반드시 있는 정렬 (O=제목순, P=조회순, Q=수정일순, R=생성일순)
        String areaCode = "1"; // 서울시 = 1
        String listYN = "Y"; // (Y=목록, N=개수)
        String sigunguCode = ""; //시군구 코드
        // 현재 날짜 구하기
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String startDate = now.format(formatter);

        String queryUrl = address + listType + "?"
                + "serviceKey=" + key
                + "&numOfRows=" + numOfRows
                + "&pageNo=" + pageNo
                + "&MobileOS=" + mobileOS
                + "&MobileApp=" + mobileApp
                + "&arrange=" + arrange
                + "&listYN=" + listYN
                + "&areaCode=" + areaCode
                + "&eventStartDate=" + startDate;

        try{
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;
            Integer count = 0;

            xpp.next();
            Festival festival = null;

            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) {
                            festival = new Festival(
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
                        else if(tag.equals("contentid")){
                            festival.setId(xpp.nextText());
                        }
                        else if(tag.equals("title")){
                            festival.setTitle(xpp.nextText());
                        }
                        else if(tag.equals("addr1")){
                            festival.setAddr1(xpp.nextText());
                        }
                        else if(tag.equals("mapx")){
                            festival.setMapX(xpp.nextText());
                        }
                        else if(tag.equals("mapy")){
                            festival.setMapY(xpp.nextText());
                        }
                        else if(tag.equals("firstimage")){
                            festival.setFirstImage(xpp.nextText());
                        }
                        else if(tag.equals("eventenddate")){
                            festival.setEndDate(xpp.nextText());
                        }
                        else if(tag.equals("eventstartdate")){
                            festival.setStartDate(xpp.nextText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName();

                        if(tag.equals("item")) {
                            eventLists.add(festival);
                        }
                        break;

                }
                eventType= xpp.next();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return eventLists;
    }

}