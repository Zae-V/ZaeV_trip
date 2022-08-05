package com.example.ZaeV_trip.Cafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Filter;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.Cafe;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class CafeActivity extends AppCompatActivity {
    RecyclerView cafeList;
    ArrayList<Cafe> cafes = new ArrayList<>();
    SearchView searchView;

    String local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cafe);
        cafeList = (RecyclerView) findViewById(R.id.cafeList);
        searchView = findViewById(R.id.searchBar);


        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            local = extras.getString("local");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                cafes = getXmlData();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        CafeAdapter cafeAdapter = new CafeAdapter(CafeActivity.this);
                        ArrayList<Cafe> filterdList = new ArrayList<Cafe>();

                        for(int i = 0; i< cafes.size();i++){
                            if(cafes.get(i).getCategory() != null && (cafes.get(i).getCategory().equals("카페") || cafes.get(i).getCategory().equals("베이커리") || cafes.get(i).getCategory().equals("까페"))){
                                if(local.equals("전체 지역") || local.equals("전체")){
                                    filterdList.add(cafes.get(i));
//                                    cafeAdapter.addItem(cafes.get(i));
                                }
                                else{
                                    if(cafes.get(i).getLocation().split(" ")[1].equals(local)){
                                        filterdList.add(cafes.get(i));
                                    }
                                }
                            }
                        }

                        CafeAdapter cafeAdapter = new CafeAdapter(CafeActivity.this ,filterdList);
                        cafeList.setLayoutManager(new LinearLayoutManager(CafeActivity.this, RecyclerView.VERTICAL,false));
                        cafeList.setAdapter(cafeAdapter);

                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                cafeAdapter.getFilter().filter(query);

                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {

                                return false;
                            }
                        });



                        cafeAdapter.setOnItemClickListener(new CafeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int i) {
                                Bundle bundle = new Bundle();
                                bundle.putString("id",filterdList.get(i).getId());
                                bundle.putString("name", filterdList.get(i).getName());
                                bundle.putString("location",filterdList.get(i).getLocation());
                                bundle.putString("category", filterdList.get(i).getCategory());
                                bundle.putString("x", filterdList.get(i).getMapX());
                                bundle.putString("y",filterdList.get(i).getMapY());
                                bundle.putString("number",filterdList.get(i).getNumber());
                                bundle.putString("menu",filterdList.get(i).getMenu());

                                CafeFragment cafeFragment = new CafeFragment();
                                cafeFragment.setArguments(bundle);

                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.container_cafe, cafeFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        });

                    }
                });

            }
        }).start();




    }

    public ArrayList<Cafe> getXmlData(){
        ArrayList<Cafe> cafes = new ArrayList<Cafe>();

        StringBuffer buffer=new StringBuffer();
        String key= getString(R.string.vegan_key);
        String address = "http://openapi.seoul.go.kr:8088/";
        String listType = "CrtfcUpsoInfo";
        String startIndex = "800";
        String endIndex = "1700";

        String queryUrl = address + key
                + "/xml/" + listType
                + "/" + startIndex
                + "/" + endIndex + "/";
        try{
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            Cafe cafe = null;
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기
                        if(tag.equals("row")){
                            cafe = new Cafe("","","","","","","","");
                        }
                        else if(tag.equals("CRTFC_UPSO_MGT_SNO")){
                            cafe.setId(xpp.nextText());
                        }
                        else if(tag.equals("UPSO_NM")){
                            cafe.setName(xpp.nextText());
                        }
                        else if(tag.equals("RDN_CODE_NM")){
                            cafe.setLocation(xpp.nextText());
                        }
                        else if(tag.equals("BIZCND_CODE_NM")){
                            cafe.setCategory(xpp.nextText());
                        }
                        else if(tag.equals("Y_DNTS")){
                            cafe.setMapY(xpp.nextText());
                        }
                        else if(tag.equals("X_CNTS")){
                            cafe.setMapX(xpp.nextText());
                        }
                        else if(tag.equals("FOOD_MENU")){
                            cafe.setMenu(xpp.nextText());
                        }
                        else if(tag.equals("TEL_NO")){
                            cafe.setNumber(xpp.nextText());
                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("row")) {
                            cafes.add(cafe);
                        };// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return cafes;
    }


}
