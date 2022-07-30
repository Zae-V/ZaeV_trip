package com.example.ZaeV_trip.Cafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Search.SearchFragment;

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
    ArrayList<String> x = new ArrayList<String>();
    ArrayList<String> y = new ArrayList<String>();
    ArrayList<String> numbers = new ArrayList<String>();
    ArrayList<String> menus = new ArrayList<String>();

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
                        ArrayList<String> filteredX = new ArrayList<String>();
                        ArrayList<String> filteredY = new ArrayList<String>();
                        ArrayList<String> filteredNumber = new ArrayList<String>();
                        ArrayList<String> filteredMenu = new ArrayList<String>();


                        for(int i = 0; i< names.size();i++){

                            if(categories.get(i) != null && (categories.get(i).equals("카페") || categories.equals("베이커리") || categories.equals("까페"))){
                                if(local.equals("전체 지역") || local.equals("전체")){
                                    filteredName.add(names.get(i));
                                    filteredLocation.add(locations.get(i));
                                    filteredCategory.add(categories.get(i));
                                    filteredX.add(x.get(i));
                                    filteredY.add(y.get(i));
                                    filteredNumber.add(numbers.get(i));
                                    filteredMenu.add(menus.get(i));
                                }
                                else{
                                    if(locations.get(i).split(" ")[1].equals(local)){
                                        filteredName.add(names.get(i));
                                        filteredLocation.add(locations.get(i));
                                        filteredCategory.add(categories.get(i));
                                        filteredX.add(x.get(i));
                                        filteredY.add(y.get(i));
                                        filteredNumber.add(numbers.get(i));
                                        filteredMenu.add(menus.get(i));
                                    }
                                }
                            }
                        }

                        CafeAdapter cafeAdapter = new CafeAdapter(CafeActivity.this ,filteredName, filteredLocation, filteredCategory);


                        gridView.setAdapter(cafeAdapter);

                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Bundle bundle = new Bundle();
                                bundle.putString("name", filteredName.get(i));
                                bundle.putString("location",filteredLocation.get(i));
                                bundle.putString("category", filteredCategory.get(i));
                                bundle.putString("x", filteredX.get(i));
                                bundle.putString("y",filteredY.get(i));
                                bundle.putString("number",filteredNumber.get(i));
                                bundle.putString("menu",filteredMenu.get(i));

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
                        else if(tag.equals("Y_DNTS")){
                            xpp.next();
                            String y_dnts = xpp.getText();
                            y.add(y_dnts);
                        }
                        else if(tag.equals("X_CNTS")){
                            xpp.next();
                            String x_cnts = xpp.getText();
                            x.add(x_cnts);
                        }
                        else if(tag.equals("FOOD_MENU")){
                            xpp.next();
                            String menu = xpp.getText();
                            menus.add(menu);
                        }
                        else if(tag.equals("TEL_NO")){
                            xpp.next();
                            String number = xpp.getText();
                            numbers.add(number);
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
