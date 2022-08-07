package com.example.ZaeV_trip.Restaurant;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.Restaurant;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    String local;
    SearchView searchView;
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        list = (RecyclerView) findViewById(R.id.restaurantList);
        searchView = findViewById(R.id.restaurantSearchBar);
        Bundle extras = getIntent().getExtras();

        if(extras!=null){
            local = extras.getString("local");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                restaurants = getXmlData();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Restaurant> filteredRestaurant = new ArrayList<Restaurant>();

                        for(int i = 0; i< restaurants.size(); i++) {
                            if (local.equals("전체 지역") || local.equals("전체")) {
                                filteredRestaurant.add(restaurants.get(i));
                            }
                            else {
                                if (restaurants.get(i).getAddr1().contains(local)) {
                                    filteredRestaurant.add(restaurants.get(i));
                                }
                            }
                            RestaurantAdapter adapter = new RestaurantAdapter(RestaurantActivity.this, filteredRestaurant);
                            list.setLayoutManager(new LinearLayoutManager(RestaurantActivity.this, RecyclerView.VERTICAL, false));
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
                            adapter.setOnItemClickListener(new RestaurantAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int i) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("name", filteredRestaurant.get(i).getTitle());
                                    bundle.putString("id", filteredRestaurant.get(i).getContentID());
                                    bundle.putString("location", filteredRestaurant.get(i).getAddr1());
                                    bundle.putString("x", filteredRestaurant.get(i).getMapX());
                                    bundle.putString("y", filteredRestaurant.get(i).getMapY());
                                    bundle.putString("firstImg", filteredRestaurant.get(i).getFirstImage());
                                    bundle.putString("number",filteredRestaurant.get(i).getNumber());

                                    RestaurantFragment restaurantFragment = new RestaurantFragment();
                                    restaurantFragment.setArguments(bundle);

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container_restaurant, restaurantFragment);
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

    public ArrayList<Restaurant> getXmlData() {
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();

        String query = "%EC%A0%84%EB%A0%A5%EB%A1%9C";
        String key = getString(R.string.portal_key);
        String address = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/";
        String listType = "areaBasedList";
        String pageNo = "1";
        String numOfRows = "100";
        String mobileApp = "ZaeVTour";
        String mobileOS = "AND";
        String arrange = "A"; // (A=제목순, B=조회순, C=수정일순, D=생성일순) , 대표이미지가 반드시 있는 정렬 (O=제목순, P=조회순, Q=수정일순, R=생성일순)
        String contentTypeID = "39";
        String areaCode = "1"; // 서울시 = 1
        String listYN = "Y"; // (Y=목록, N=개수)

        String queryUrl = address + listType + "?"
                + "serviceKey=" + key
                + "&pageNo=" + pageNo
                + "&numOfRows=" + numOfRows
                + "&MobileApp=" + mobileApp
                + "&MobileOS=" + mobileOS
                + "&arrange=" + arrange
                + "&contentTypeId=" + contentTypeID
                + "&areaCode=" + areaCode
                + "&listYN=" + listYN;

        try {
            URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            Restaurant restaurant = null;
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) {
                            restaurant = new Restaurant(
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
                        else if(tag.equals("title")){
                            restaurant.setTitle(xpp.nextText());
                        }
                        else if(tag.equals("addr1")){
                            restaurant.setAddr1(xpp.nextText());
                        }
                        else if(tag.equals("addr2")){
                            restaurant.setAddr2(xpp.nextText());
                        }
                        else if(tag.equals("mapx")){
                            restaurant.setMapX(xpp.nextText());
                        }
                        else if(tag.equals("mapy")){
                            restaurant.setMapY(xpp.nextText());
                        }
                        else if(tag.equals("firstimage")){
                            restaurant.setFirstImage(xpp.nextText());
                        }
                        else if(tag.equals("firstimage2")){
                            restaurant.setFirstImage2(xpp.nextText());
                        }
                        else if(tag.equals("contentid")){
                            restaurant.setContentID(xpp.nextText());
                        }
                        else if(tag.equals("tel")){
                            restaurant.setNumber(xpp.nextText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName();

                        if(tag.equals("item")) {
                            restaurants.add(restaurant);
                        }
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
