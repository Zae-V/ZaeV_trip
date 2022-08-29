package com.example.ZaeV_trip.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ZaeV_trip.Main.MainActivity;
import com.example.ZaeV_trip.util.MyService;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.Festival;
import com.example.ZaeV_trip.model.Plogging;
import com.example.ZaeV_trip.model.Restaurant;
import com.example.ZaeV_trip.util.MySharedPreferences;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    ArrayList<Festival> eventLists = new ArrayList<>();
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    ArrayList<Plogging> bikes = new ArrayList<>();

    MyService myService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        View view = findViewById(R.id.container_splash);

        startService(view);


        ImageView gif_image = (ImageView) findViewById(R.id.gif_image);
        Glide.with(this).load(R.raw.animation_bird).into(gif_image);

        Bundle bundle = new Bundle();

        Thread eventThread = new Thread(new Runnable() {
            @Override
            public void run() {
                eventLists = getXmlEventData();
                bundle.putParcelableArrayList("festival", (ArrayList<? extends Parcelable>) eventLists);
                Log.d("테스트1", String.valueOf(eventLists.size()));
            }
        });

        eventThread.start();
        eventThread.isAlive();

        Thread restaurantThread = new Thread(new Runnable() {
            @Override
            public void run() {
                restaurants = getRestaurantData();
                bundle.putParcelableArrayList("restaurant", (ArrayList<? extends Parcelable>) restaurants);
                Log.d("테스트2", String.valueOf(restaurants.size()));
            }
        });

        restaurantThread.start();
        restaurantThread.isAlive();

        Thread bikeThread =  new Thread(new Runnable() {
            @Override
            public void run() {
                bikes = getXmlBikeData();
                bundle.putParcelableArrayList("bike", (ArrayList<? extends Parcelable>) bikes);
                Log.d("테스트3", String.valueOf(bikes.size()));
            }
        });

        bikeThread.start();
        bikeThread.isAlive();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MySharedPreferences.getUserEmail(SplashActivity.this).length() != 0) {
                    try {
                        restaurantThread.join();
                        bikeThread.join();
                        eventThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

//                    Toast.makeText(getApplicationContext(), "자동 로그인 되었습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtras(bundle);
                    intent.putExtra("req", "splashData");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 3000);

    }
    public void startService(View view) {   // 서비스 실행 버튼
        if(myService==null){
            Intent intent = new Intent(this, MyService.class);
            startService(intent);
        }
    }// startService()..

    public ArrayList<Festival> getXmlEventData(){
        ArrayList<Festival> eventLists = new ArrayList<Festival>();
        String key = getString(R.string.portal_key);
        String address = "https://api.visitkorea.or.kr/openapi/service/rest/KorService/";
        String listType = "searchFestival";
        String pageNo = "1";
        String numOfRows = "100";
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
                        else if(tag.equals("tel")){
                            festival.setTel(xpp.nextText());
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

    public ArrayList<Plogging> getXmlBikeData() {
        ArrayList<Plogging> bikes = new ArrayList<Plogging>();
        String key = getString(R.string.portal_key);
        String address = "http://api.visitkorea.or.kr/openapi/service/rest/Durunubi/courseList";
        String pageNo = "1";
        String numOfRows = "100";
        String mobileApp = "ZaeVTour";
        String mobileOS = "AND";
        String crsKorNm = "%EC%84%9C%EC%9A%B8"; // "서울" 인코딩
        String brdDiv = "DNBW"; // 걷기/자전거 구분(DNWW : 걷기길, DNBW : 자전거길)

        String queryUrl = address + "?"
                + "serviceKey=" + key
                + "&pageNo=" + pageNo
                + "&numOfRows=" + numOfRows
                + "&MobileOS=" + mobileOS
                + "&MobileApp=" + mobileApp
                + "&crsKorNm=" + crsKorNm
                + "&brdDiv=" + brdDiv;

        try {
            URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); //xml파싱을 위한
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            Plogging plogging = null;

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();//테그 이름 얻어오기

                        if (tag.equals("item")) {
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
                                    "",
                                    ""
                            );
                        } else if (tag.equals("crsContents")) {
                            plogging.setCrsContents(xpp.nextText());
                        } else if (tag.equals("crsDstnc")) {
                            plogging.setCrsDstnc(xpp.nextText());
                        } else if (tag.equals("crsKorNm")) {
                            plogging.setCrsKorNm(xpp.nextText());
                        } else if (tag.equals("crsLevel")) {
                            plogging.setCrsLevel(xpp.nextText());
                        } else if (tag.equals("crsSummary")) {
                            plogging.setCrsSummary(xpp.nextText());
                        } else if (tag.equals("crsTotlRqrmHour")) {
                            plogging.setCrsTotlRqrmHour(xpp.nextText());
                        } else if (tag.equals("crsTourInfo")) {
                            plogging.setCrsTourInfo(xpp.nextText());
                        } else if (tag.equals("sigun")) {
                            plogging.setSigun(xpp.nextText());
                        } else if (tag.equals("travelerinfo")) {
                            plogging.setTravelerinfo(xpp.nextText());
                        } else if (tag.equals("brdDiv")) {
                            plogging.setBrdDiv(xpp.nextText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();

                        if (tag.equals("item")) {
                            bikes.add(plogging);
                        }
                        break;

                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bikes;
    }

    public ArrayList<Restaurant> getRestaurantData(){
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
        String key= getString(R.string.vegan_key);
        String address = "http://openapi.seoul.go.kr:8088/";
        String listType = "CrtfcUpsoInfo";
        String startIndex = "860";
        String endIndex = "960";

        String queryUrl = address + key
                + "/xml/" + listType
                + "/" + startIndex
                + "/" + endIndex + "/";
//        Log.d("테스트", queryUrl);

        try{
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            Restaurant restaurant = null;
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기
                        if(tag.equals("row")){
                            restaurant = new Restaurant("","","","","","","","","");
                        }
                        else if(tag.equals("CRTFC_UPSO_MGT_SNO")){
                            restaurant.setId(xpp.nextText());
                        }
                        else if(tag.equals("UPSO_NM")){
                            restaurant.setName(xpp.nextText());
                        }
                        else if(tag.equals("RDN_CODE_NM")){
                            restaurant.setLocation(xpp.nextText());
                        }
                        else if(tag.equals("BIZCND_CODE_NM")){
                            restaurant.setCategory(xpp.nextText());
                        }
                        else if(tag.equals("CRTFC_GBN")){
                            restaurant.setAuthType(xpp.nextText());
                        }
                        else if(tag.equals("Y_DNTS")){
                            restaurant.setMapY(xpp.nextText());
                        }
                        else if(tag.equals("X_CNTS")){
                            restaurant.setMapX(xpp.nextText());
                        }
                        else if(tag.equals("FOOD_MENU")){
                            restaurant.setMenu(xpp.nextText());
                        }
                        else if(tag.equals("TEL_NO")){
                            restaurant.setNumber(xpp.nextText());
                        }


                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("row")) {
                            restaurants.add(restaurant);
//                            Log.d("테스트",restaurant.getName());
                        };// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return restaurants;
    }
}
