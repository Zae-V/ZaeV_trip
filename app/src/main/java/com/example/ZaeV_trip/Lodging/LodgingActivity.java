package com.example.ZaeV_trip.Lodging;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.Lodging;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class LodgingActivity extends AppCompatActivity {

    ArrayList<Lodging> lodgings = new ArrayList<>();
    String local;

    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            local = extras.getString("local");
        }
        setContentView(R.layout.activity_lodging);

        gridView = findViewById(R.id.LodgingList);

        new Thread(new Runnable() {
            @Override
            public void run() {
                lodgings = getXmlData();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LodgingAdapter adapter = new LodgingAdapter(LodgingActivity.this);
                        for(int i = 0; i< lodgings.size(); i++) {
                            if(local.equals("전체 지역") || local.equals("전체")){
                                adapter.addItem(lodgings.get(i));
                            }else{
                                if(lodgings.get(i).getAddr1().split(" ").length > 1 && lodgings.get(i).getAddr1().split(" ")[1].equals(local)){
                                    adapter.addItem(lodgings.get(i));
                                }
                            }
                        }
                        gridView.setAdapter(adapter);
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Bundle bundle = new Bundle();
                                bundle.putString("name", lodgings.get(i).getTitle());
                                bundle.putString("location", lodgings.get(i).getAddr1());
                                bundle.putString("startDate", lodgings.get(i).getStartDate());
                                bundle.putString("endDate",lodgings.get(i).getEndDate());
                                bundle.putString("img",lodgings.get(i).getFirstImage());
                                bundle.putString("x", lodgings.get(i).getMapX());
                                bundle.putString("y",lodgings.get(i).getMapY());

                                LodgingFragment lodgingFragment = new LodgingFragment();
                                lodgingFragment.setArguments(bundle);

                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.container_lodging, lodgingFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                            }
                        });
                    }

                });
            }
        }).start();

    }
    public ArrayList<Lodging> getXmlData(){
        ArrayList<Lodging> lodgings = new ArrayList<Lodging>();
//        String str= edit.getText().toString();//EditText에 작성된 Text얻어오기
//        String location = URLEncoder.encode(str);
        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";
        String key = getString(R.string.portal_key);
        String address = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/";
        String listType = "searchLodging";
        String pageNo = "1";
        String numOfRows = "1000";
        String mobileApp = "ZaeVTour";
        String mobileOS = "AND";
        String arrange = "A"; // (A=제목순, B=조회순, C=수정일순, D=생성일순) , 대표이미지가 반드시 있는 정렬 (O=제목순, P=조회순, Q=수정일순, R=생성일순)
        String contentTypeID = "15";
        String areaCode = "1"; // 서울시 = 1
        String listYN = "Y"; // (Y=목록, N=개수)
        String sigunguCode = ""; //시군구 코드


        String queryUrl = address + listType + "?"
                + "serviceKey=" + key
                + "&pageNo=" + pageNo
                + "&numOfRows=" + numOfRows
                + "&MobileApp=" + mobileApp
                + "&MobileOS=" + mobileOS
                + "&arrange=" + arrange
//                + "&contentTypeId=" + contentTypeID
                + "&areaCode=" + areaCode
                + "&listYN=" + listYN;

        try{
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;
            Integer count = 0;

            xpp.next();
            Lodging lodging = null;

            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) {
                            lodging = new Lodging(
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
                            lodging.setTitle(xpp.nextText());
                        }
                        else if(tag.equals("addr1")){
                            lodging.setAddr1(xpp.nextText());
                        }
                        else if(tag.equals("mapx")){
                            lodging.setMapX(xpp.nextText());
                        }
                        else if(tag.equals("mapy")){
                            lodging.setMapY(xpp.nextText());
                        }
                        else if(tag.equals("firstimage")){
                            lodging.setFirstImage(xpp.nextText());
                        }
                        else if(tag.equals("eventenddate")){
                            lodging.setEndDate(xpp.nextText());
                        }
                        else if(tag.equals("eventstartdate")){
                            lodging.setStartDate(xpp.nextText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName();

                        if(tag.equals("item")) {
                            lodgings.add(lodging);
                        }
                        break;

                }
                eventType= xpp.next();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return lodgings;
    }
}
