package com.zaev.ZaeV_trip.Festival;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.zaev.ZaeV_trip.Main.MainActivity;
import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.model.Festival;
import com.zaev.ZaeV_trip.model.FestivalDetail;
import com.zaev.ZaeV_trip.model.FestivalDetail2;
import com.zaev.ZaeV_trip.util.CharacterWrapTextView;
import com.zaev.ZaeV_trip.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FestivalFragment extends Fragment {
    FirebaseFirestore mDatabase =FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    FestivalDetail festivalDetail;
    FestivalDetail2 festivalDetail2;

    boolean isMainData = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_festival, container, false);

        //Bundle
        Festival festival = (Festival) getArguments().getSerializable("festival");

        if(getArguments().getString("mainData") == "true"){
            isMainData = true;
        }

        String name = festival.getTitle();
        String location = festival.getAddr1();
        String startDate = festival.getStartDate();
        String endDate = festival.getEndDate();
        String x = festival.getMapX();
        String y = festival.getMapY();
        String img = festival.getFirstImage();
        String tel = festival.getTel();
        String contentID = festival.getId();
        float width = getArguments().getFloat("width");

        TextView festival_detail_txt = v.findViewById(R.id.festival_detail_txt);
        TextView festival_location = v.findViewById(R.id.festival_location);
        TextView festival_tel = v.findViewById(R.id.festival_tel);
        TextView festival_detail_location = v.findViewById(R.id.festival_detail_location);
        TextView festival_time = v.findViewById(R.id.festival_time);
        TextView festival_homepage = v.findViewById(R.id.festival_homepage);
        TextView festival_booking = v.findViewById(R.id.festival_booking);
        ImageView content_img = v.findViewById(R.id.content_img);
        CharacterWrapTextView overview = v.findViewById(R.id.overview);
        ImageView bookmarkBtn = (ImageView) v.findViewById(R.id.bookmarkBtn);

        festival_detail_txt.setMaxWidth((int) width - Util.ConvertDPtoPX(getActivity().getApplicationContext(), 100));

        festival_detail_txt.setText(name);
        festival_location.setText(location);
        festival_tel.setText(Html.fromHtml(tel));

        MapView mapView = new MapView(getActivity());

        Float coor_x = Float.valueOf(x);
        Float coor_y = Float.valueOf(y);

        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(coor_y,coor_x),2,true);

        MapPOIItem marker = new MapPOIItem();
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(coor_y,coor_x));
        marker.setItemName(name);
        marker.setTag(0);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin);
        mapView.addPOIItem(marker);

        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.mapView);
        mapViewContainer.addView(mapView);

        if (!img.equals("")) {
            Glide.with(v)
                    .load(img)
                    .placeholder(R.drawable.default_bird_img)
                    .error(R.drawable.default_bird_img)
                    .fallback(R.drawable.default_bird_img)
                    .into(content_img);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                festivalDetail = getXmlData(contentID);
                festivalDetail2 = getXmlData2(contentID);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!festivalDetail.getOverview().equals("")) {
                            overview.setText(Html.fromHtml(festivalDetail.getOverview()));
                        } else {
                            overview.setVisibility(View.GONE);
                        }

                        if (!festivalDetail2.getEventPlace().equals("")) {
                            festival_detail_location.setText(Html.fromHtml("<b>위치: </b>" + festivalDetail2.getEventPlace()));
                        } else {
                            festival_detail_location.setVisibility(View.GONE);
                        }

                        if (!festivalDetail2.getEventHomepage().equals("")) {
                            festival_homepage.setText(Html.fromHtml("<b>홈페이지: </b>" + festivalDetail2.getEventHomepage()));
                        } else {
                            festival_homepage.setVisibility(View.GONE);
                        }

                        if (!festivalDetail2.getPlaytime().equals("")) {
                            festival_time.setText(Html.fromHtml("<b>운영 시간: </b>" + festivalDetail2.getPlaytime()));
                        } else {
                            festival_time.setVisibility(View.GONE);
                        }

                        if (!festivalDetail2.getBookingPlace().equals("")) {
                            festival_booking.setText(Html.fromHtml("<b>예약: </b>" + festivalDetail2.getBookingPlace()));
                        } else {
                            festival_booking.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }).start();

        mDatabase.collection("BookmarkItem").document(userId).collection("festival").document(festival.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        bookmarkBtn.setActivated(true);
                    } else {
                        bookmarkBtn.setActivated(false);
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookmarkBtn.setActivated(!bookmarkBtn.isActivated());
                if (!bookmarkBtn.isActivated()){
                    // 취소 동작
                    deleteBookmark(festival);
                }
                else if(bookmarkBtn.isActivated()){
                    // 선택 동작
                    writeBookmark(festival);
                }

            }
        });

        return v;
    }

    private void writeBookmark(Festival festival){
        Map<String, Object> info = new HashMap<>();
        info.put("name", festival.getTitle());
        info.put("img", festival.getFirstImage());
        info.put("address", festival.getAddr1());
        info.put("position_x", festival.getMapX());
        info.put("position_y", festival.getMapY());
        info.put("serialNumber", festival.getId());
        info.put("start_date", festival.getStartDate());
        info.put("end_date", festival.getEndDate());
        info.put("tel", festival.getTel());

        mDatabase.collection("BookmarkItem").document(userId).collection("festival").document(festival.getId()).set(info);
    }

    private void deleteBookmark(Festival festival){
        mDatabase.collection("BookmarkItem").document(userId).collection("festival").document(festival.getId()).delete();
    }

    public FestivalDetail getXmlData(String contentId){
        FestivalDetail festivalDetail = new FestivalDetail(
                ""
        );

        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";
        String key = getString(R.string.portal_key);
        String address = "http://apis.data.go.kr/B551011/KorService/";
        String listType = "detailInfo";
        String pageNo = "1";
        String numOfRows = "10";
        String mobileApp = "ZaeVTour";
        String mobileOS = "ETC";
        String contentTypeId = "15";

        String queryUrl = address + listType + "?"
                + "serviceKey=" + key
                + "&numOfRows=" + numOfRows
                + "&pageNo=" + pageNo
                + "&MobileOS=" + mobileOS
                + "&MobileApp=" + mobileApp
                + "&contentId=" + contentId
                + "&contentTypeId=" + contentTypeId;

        try{
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            Log.d("festivalEDSSS", String.valueOf(url));
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

                        if(tag.equals("infotext")){
                            String text = xpp.nextText();
                            Log.d("festivalEDSSS", text);
                            festivalDetail.setOverview(text);
                            return festivalDetail;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        break;

                }
                eventType= xpp.next();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return festivalDetail;
    }

    public FestivalDetail2 getXmlData2(String contentId){
        FestivalDetail2 festivalDetail2 = new FestivalDetail2(
                "",
                "",
                "",
                ""
        );

        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";
        String key = getString(R.string.portal_key);
        String address = "http://apis.data.go.kr/B551011/KorService/";
        String listType = "detailIntro";
        String pageNo = "1";
        String numOfRows = "10";
        String mobileApp = "ZaeVTour";
        String mobileOS = "ETC";
        String contentTypeId = "15";

        String queryUrl = address + listType + "?"
                + "serviceKey=" + key
                + "&numOfRows=" + numOfRows
                + "&pageNo=" + pageNo
                + "&MobileOS=" + mobileOS
                + "&MobileApp=" + mobileApp
                + "&contentId=" + contentId
                + "&contentTypeId=" + contentTypeId;

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

                        if(tag.equals("playtime")){
                            festivalDetail2.setPlaytime(xpp.nextText());
                        }
                        else if(tag.equals("eventplace")){
                            festivalDetail2.setEventPlace(xpp.nextText());
                        }
                        else if(tag.equals("eventhomepage")){
                            festivalDetail2.setEventHomepage(xpp.nextText());
                        }
                        else if(tag.equals("bookingplace")){
                            festivalDetail2.setBookingPlace(xpp.nextText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        break;

                }
                eventType= xpp.next();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return festivalDetail2;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isMainData == true) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }
}