package com.zaev.ZaeV_trip.TouristSpot;

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
import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.model.TouristSpot;
import com.zaev.ZaeV_trip.model.TouristSpotDetail;
import com.zaev.ZaeV_trip.model.TouristSpotDetail2;
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

public class TouristSpotFragment extends Fragment {
    FirebaseFirestore mDatabase =FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    TouristSpotDetail touristSpotDetail;
    TouristSpotDetail2 touristSpotDetail2;

    TextView titleTextView;
    TextView locationTextView;
    TextView telTextView;
    TextView detailLocationTextView;
    TextView restDateTextView;
    TextView overviewTextView;
    TextView homepageTextView;
    ImageView content_img;
    ImageView content_img2;
    ImageView bookmarkBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tourist_spot, container, false);

        //Bundle
        TouristSpot touristSpot = (TouristSpot) getArguments().getSerializable("touristSpot");
        String name = touristSpot.getTitle();
        String contentID = touristSpot.getContentID();
        String detail_location = touristSpot.getAddr1();
        String location = touristSpot.getAddr2();
        float width = getArguments().getFloat("width");

        titleTextView = v.findViewById(R.id.detail_txt);
        locationTextView = v.findViewById(R.id.location);
        telTextView = v.findViewById(R.id.tel);

        detailLocationTextView = v.findViewById(R.id.detail_location);
        restDateTextView = v.findViewById(R.id.restDate);
        overviewTextView = v.findViewById(R.id.overview);
        homepageTextView = v.findViewById(R.id.homepage);
        content_img = v.findViewById(R.id.content_img);
        content_img2 = v.findViewById(R.id.content_img2);
        bookmarkBtn = (ImageView) v.findViewById(R.id.bookmarkBtn);

        titleTextView.setMaxWidth((int) width - Util.ConvertDPtoPX(getActivity().getApplicationContext(), 100));
        titleTextView.setText(name);

        if (!location.equals("")) {
            locationTextView.setText(location);
        } else {
            setVisibility(1);
        }

        if (!detail_location.equals("")) {
            detailLocationTextView.setText(Html.fromHtml("<b>상세 주소: </b>"+detail_location));
        } else {
            setVisibility(3);
        }

        MapView mapView = new MapView(getActivity());

        Float coor_x = Float.valueOf(touristSpot.getMapX());
        Float coor_y = Float.valueOf(touristSpot.getMapY());

        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(coor_y,coor_x),2,true);

        MapPOIItem marker = new MapPOIItem();
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(coor_y,coor_x));
        marker.setItemName(name);
        marker.setTag(0);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin);
        mapView.addPOIItem(marker);

        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.mapView);
        mapViewContainer.addView(mapView);

        new Thread(new Runnable() {
            @Override
            public void run() {
                touristSpotDetail = getXmlData(contentID);
                touristSpotDetail2 = getXmlData2(contentID);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!touristSpotDetail.getOverview().equals("")) {
                            overviewTextView.setText(Html.fromHtml(touristSpotDetail.getOverview()));
                        } else {
                            setVisibility(5);
                        }

                        if (!touristSpotDetail.getHomepage().equals("")) {
                            homepageTextView.setText(Html.fromHtml("<b>홈페이지: </b>" + touristSpotDetail.getHomepage()));
                        } else {
                            setVisibility(6);
                        }

                        if (!touristSpotDetail.getFirstImage().equals("")) {
                            Glide.with(v)
                                    .load(touristSpotDetail.getFirstImage())
                                    .placeholder(R.drawable.default_profile_image)
                                    .error(R.drawable.default_profile_image)
                                    .fallback(R.drawable.default_profile_image)
                                    .into(content_img);
                        } else {
                            setVisibility(7);
                        }

                        if (!touristSpotDetail.getFirstImage2().equals("")) {
                            Glide.with(v)
                                    .load(touristSpotDetail.getFirstImage2())
                                    .placeholder(R.drawable.default_profile_image)
                                    .error(R.drawable.default_profile_image)
                                    .fallback(R.drawable.default_profile_image)
                                    .into(content_img2);
                        } else {
                            setVisibility(8);
                        }

                        if (!touristSpotDetail2.getInfocenter().equals("")) {
                            telTextView.setText(touristSpotDetail2.getInfocenter());
                        } else {
                            setVisibility(2);
                        }

                        if (!touristSpotDetail2.getRestdate().equals("")) {
                            restDateTextView.setText(Html.fromHtml("<b>쉬는날: </b>"+ touristSpotDetail2.getRestdate()));
                        } else {
                            setVisibility(4);
                        }
                    }
                });
            }
        }).start();

        mDatabase.collection("BookmarkItem").document(userId).collection("touristSpot").document(contentID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                    deleteBookmark(contentID);
                }
                else if(bookmarkBtn.isActivated()){
                    // 선택 동작
                    writeBookmark(name, detail_location, location, touristSpot.getMapX(), touristSpot.getMapY(), contentID, touristSpot.getFirstImage());
                }

            }
        });

        return v;
    }

    private void writeBookmark(String title, String addr1, String addr2, String x, String y, String contentID, String image){
        Map<String, Object> info = new HashMap<>();
        info.put("name", title);
        info.put("type", "관광명소");
        info.put("address", addr1);
        info.put("address2", addr2);
        info.put("position_x", x);
        info.put("position_y", y);
        info.put("serialNumber", contentID);
        info.put("image", image);

        mDatabase.collection("BookmarkItem").document(userId).collection("touristSpot").document(contentID).set(info);
    }

    private void deleteBookmark(String contentID){
        mDatabase.collection("BookmarkItem").document(userId).collection("touristSpot").document(contentID).delete();
    }

    public TouristSpotDetail getXmlData(String contentId){
        TouristSpotDetail touristSpot = new TouristSpotDetail(
                "",
                "",
                "",
                "",
                "",
                ""
        );

        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";
        String key = getString(R.string.portal_key);
        String address = "https://api.visitkorea.or.kr/openapi/service/rest/KorService/";
        String listType = "detailCommon";
        String pageNo = "1";
        String numOfRows = "10";
        String mobileApp = "ZaeVTour";
        String mobileOS = "ETC";
        String defaultYN = "Y";
        String firstImageYN = "Y";
        String areacodeYN = "Y";
        String catcodeYN = "Y";
        String addrinfoYN = "Y";
        String mapinfoYN = "Y";
        String overviewYN = "Y";

        String queryUrl = address + listType + "?"
                + "serviceKey=" + key
                + "&numOfRows=" + numOfRows
                + "&pageNo=" + pageNo
                + "&MobileOS=" + mobileOS
                + "&MobileApp=" + mobileApp
                + "&contentId=" + contentId
                + "&defaultYN=" + defaultYN
                + "&firstImageYN=" + firstImageYN
                + "&areacodeYN=" + areacodeYN
                + "&catcodeYN=" + catcodeYN
                + "&addrinfoYN=" + addrinfoYN
                + "&mapinfoYN=" + mapinfoYN
                + "&overviewYN=" + overviewYN;

        try{
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            Log.d("TouristSpotInfori", String.valueOf(url));
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

                        if(tag.equals("firstimage")){
                            touristSpot.setFirstImage(xpp.nextText());
                        }
                        else if(tag.equals("firstimage2")){
                            touristSpot.setFirstImage2(xpp.nextText());
                        }
                        else if(tag.equals("homepage")){
                            touristSpot.setHomepage(xpp.nextText());
                        }
                        else if(tag.equals("mapx")){
                            touristSpot.setMapX(xpp.nextText());
                        }
                        else if(tag.equals("mapy")){
                            touristSpot.setMapY(xpp.nextText());
                        }
                        else if(tag.equals("overview")){
                            touristSpot.setOverview(xpp.nextText());
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

        return touristSpot;
    }

    public TouristSpotDetail2 getXmlData2(String contentId) {
        TouristSpotDetail2 touristSpot = new TouristSpotDetail2(
                "",
                ""
        );

        String key = getString(R.string.portal_key);
        String address = "https://api.visitkorea.or.kr/openapi/service/rest/KorService/";
        String listType = "detailIntro";
        String pageNo = "1";
        String numOfRows = "10";
        String mobileApp = "ZaeVTour";
        String mobileOS = "ETC";
        String contentTypeId = "12";

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
            Log.d("TouristSpotInfori", String.valueOf(url));
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

                        if(tag.equals("infocenter")){
                            touristSpot.setInfocenter(xpp.nextText());
                        }
                        else if(tag.equals("restdate")){
                            touristSpot.setRestdate(xpp.nextText());
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

        return touristSpot;
    }
    public void setVisibility(Integer idx) {
        switch (idx) {
            case 1:
                locationTextView.setVisibility(View.GONE);
                break;
            case 2:
                telTextView.setVisibility(View.GONE);
                break;
            case 3:
                detailLocationTextView.setVisibility(View.GONE);
                break;
            case 4:
                restDateTextView.setVisibility(View.GONE);
                break;
            case 5:
                overviewTextView.setVisibility(View.GONE);
                break;
            case 6:
                homepageTextView.setVisibility(View.GONE);
                break;
            case 7:
                content_img.setVisibility(View.GONE);
                break;
            case 8:
                content_img2.setVisibility(View.GONE);
                break;
            default:
                Log.d("WithdrawalActivity", "default");
        }
    }
}
