package com.example.ZaeV_trip.Cafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.example.ZaeV_trip.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class CafeActivity extends AppCompatActivity {
    GridView gridView;

    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> locations = new ArrayList<String>();
    ArrayList<String> categories = new ArrayList<String>();

    String local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            local = extras.getString("local");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                getXmlData();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                                text.setText(data);

                        ArrayList<String> filteredName = new ArrayList<String>();
                        ArrayList<String> filteredLocation = new ArrayList<String>();
                        ArrayList<String> filteredCategory = new ArrayList<String>();

                        for(int i = 0; i< names.size();i++){

                            if(categories.get(i) != null && (categories.get(i).equals("카페") || categories.equals("베이커리") || categories.equals("까페"))){
                                if(local.equals("전체 지역") || local.equals("전체")){
                                    filteredName.add(names.get(i));
                                    filteredLocation.add(locations.get(i));
                                    filteredCategory.add(categories.get(i));
                                }
                                else{
                                    if(locations.get(i).split(" ")[1].equals(local)){
                                        filteredName.add(names.get(i));
                                        filteredLocation.add(locations.get(i));
                                        filteredCategory.add(categories.get(i));
                                    }
                                }
                            }
                        }

                        CafeAdapter cafeAdapter = new CafeAdapter(CafeActivity.this ,filteredName, filteredLocation, filteredCategory);


                        Log.e("view", String.valueOf(gridView));
                        gridView.setAdapter(cafeAdapter);

                    }
                });

            }
        }).start();


        setContentView(R.layout.activity_cafe);
        gridView = (GridView) findViewById(R.id.cafeList);

    }

    public void getXmlData(){
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
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기
                        if(tag.equals("row")) ;// 첫번째 검색결과
                        else if(tag.equals("UPSO_NM")){
                            xpp.next();
                            String name = xpp.getText();
                            names.add(name);
                        }
                        else if(tag.equals("RDN_CODE_NM")){
                            xpp.next();
                            String location = xpp.getText();
                            locations.add(location);
                        }
                        else if(tag.equals("BIZCND_CODE_NM")){
                            xpp.next();
                            String category = xpp.getText();
                            categories.add(category);
                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("row")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }


}
