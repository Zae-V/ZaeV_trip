package com.example.ZaeV_trip.Plogging;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.TouristSpot.TouristSpotActivity;
import com.example.ZaeV_trip.TouristSpot.TouristSpotAdapter;
import com.example.ZaeV_trip.TouristSpot.TouristSpotFragment;
import com.example.ZaeV_trip.model.Plogging;
import com.example.ZaeV_trip.model.TouristSpot;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class PloggingActivity extends AppCompatActivity {
    ArrayList<Plogging> ploggings = new ArrayList<>();
    String local;
    SearchView searchView;
    RecyclerView list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plogging);

        list = (RecyclerView) findViewById(R.id.ploggingList);
        searchView = findViewById(R.id.ploggingSearchBar);
        Bundle extras = getIntent().getExtras();

        if(extras!=null){
            local = extras.getString("local");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                ploggings = getXmlData();

                runOnUiThread(new Runnable() {
                    ArrayList<Plogging> filteredPlogging = new ArrayList<Plogging>();

                    @Override
                    public void run() {
                        for(int i = 0; i< ploggings.size(); i++) {
                            if (local.equals("전체 지역") || local.equals("전체")) {
                                filteredPlogging.add(ploggings.get(i));
                            }
                            else {
                                if (ploggings.get(i).getSigun().contains(local)) {
                                    filteredPlogging.add(ploggings.get(i));
                                }
                            }
                            PloggingAdapter adapter = new PloggingAdapter(PloggingActivity.this, filteredPlogging);
                            list.setLayoutManager(new LinearLayoutManager(PloggingActivity.this, RecyclerView.VERTICAL, false));
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

                            adapter.setOnItemClickListener(new PloggingAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int i) {
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("plogging", (Serializable) filteredPlogging.get(i));

                                    PloggingFragment ploggingFragment = new PloggingFragment();
                                    ploggingFragment.setArguments(bundle);

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container_plogging, ploggingFragment);
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

    public ArrayList<Plogging> getXmlData(){
        ArrayList<Plogging> ploggings = new ArrayList<Plogging>();
        String key = getString(R.string.portal_key);
        String address = "http://api.visitkorea.or.kr/openapi/service/rest/Durunubi/courseList";
        String pageNo = "1";
        String numOfRows = "100";
        String mobileApp = "ZaeVTour";
        String mobileOS = "AND";
        String crsKorNm = "%EC%84%9C%EC%9A%B8"; // "서울" 인코딩

        String queryUrl = address + "?"
                + "serviceKey=" + key
                + "&pageNo=" + pageNo
                + "&numOfRows=" + numOfRows
                + "&MobileOS=" + mobileOS
                + "&MobileApp=" + mobileApp
                + "&crsKorNm=" + crsKorNm;

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
                            ploggings.add(plogging);
                        }
                        break;

                }
                eventType= xpp.next();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return ploggings;
    }
}
