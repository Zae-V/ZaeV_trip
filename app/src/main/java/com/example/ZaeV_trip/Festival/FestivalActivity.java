package com.example.ZaeV_trip.Festival;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Festival.FestivalAdapter;
import com.example.ZaeV_trip.model.Festival;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class FestivalActivity extends AppCompatActivity {

    ArrayList<Festival> festivals = new ArrayList<>();
    String local;

    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival);

        gridView = findViewById(R.id.FestivalList);

        new Thread(new Runnable() {
            @Override
            public void run() {
                festivals = getXmlData();

                runOnUiThread(new Runnable() {
                    FestivalAdapter adapter = new FestivalAdapter(FestivalActivity.this);
                    @Override
                    public void run() {
                        for(int i = 0; i< festivals.size(); i++) {
                            adapter.addItem(festivals.get(i));
                            gridView.setAdapter(adapter);
                        }
                    }
                });
            }
        }).start();

    }
    public ArrayList<Festival> getXmlData(){
        ArrayList<Festival> festivals = new ArrayList<Festival>();
//        String str= edit.getText().toString();//EditText에 작성된 Text얻어오기
//        String location = URLEncoder.encode(str);
        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";
        String key = getString(R.string.portal_key);
        String address = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/";
        String listType = "searchFestival";
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
                                    0.0,
                                    0.0,
                                    "",
                                    "",
                                    ""
                            );
                        }
                        else if(tag.equals("title")){
                            festival.setTitle(xpp.nextText());
                        }
                        else if(tag.equals("addr1")){
                            festival.setAddr1(xpp.nextText());
                        }
                        else if(tag.equals("mapx")){
                            festival.setMapX(Double.parseDouble(xpp.nextText()));
                        }
                        else if(tag.equals("mapy")){
                            festival.setMapY(Double.parseDouble(xpp.nextText()));
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
                            festivals.add(festival);
                        }
                        break;

                }
                eventType= xpp.next();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return festivals;
    }
}