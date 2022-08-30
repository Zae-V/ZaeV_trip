package com.zaev.ZaeV_trip.Lodging;

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
import com.zaev.ZaeV_trip.model.Lodging;
import com.zaev.ZaeV_trip.model.LodgingDetail;
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

public class LodgingFragment extends Fragment {
    FirebaseFirestore mDatabase =FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    
    LodgingDetail lodgingDetail;

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
        View v = inflater.inflate(R.layout.fragment_lodging, container, false);

        //Bundle
        Lodging lodging = (Lodging) getArguments().getSerializable("lodging");
        String name = lodging.getTitle();
        String id = lodging.getContentID();
        String x = lodging.getMapX();
        String y = lodging.getMapY();
        String detail_location = lodging.getAddr1();
        String location = lodging.getAddr2();
        String telephone = lodging.getTel();
        float width = getArguments().getFloat("width");
        
        bookmarkBtn = (ImageView) v.findViewById(R.id.restaurantBookmarkBtn);
        titleTextView = v.findViewById(R.id.detail_txt);
        locationTextView = v.findViewById(R.id.location);
        telTextView = v.findViewById(R.id.tel);

        detailLocationTextView = v.findViewById(R.id.detail_location);
        restDateTextView = v.findViewById(R.id.restDate);
        overviewTextView = v.findViewById(R.id.overview);
        homepageTextView = v.findViewById(R.id.homepage);
        content_img = v.findViewById(R.id.content_img);
        content_img2 = v.findViewById(R.id.content_img2);

        titleTextView.setMaxWidth((int) width - Util.ConvertDPtoPX(getActivity().getApplicationContext(), 100));
        titleTextView.setText(name);


        if (!location.equals("")) {
            locationTextView.setText(location);
        } else {
            setVisibility(1);
        }

        if (!telephone.equals("")) {
            telTextView.setText(telephone);
        } else {
            setVisibility(2);
        }

        if (!detail_location.equals("")) {
            detailLocationTextView.setText(Html.fromHtml("<b>상세 주소: </b>" + detail_location));
        } else {
            setVisibility(3);
        }

        MapView mapView = new MapView(getActivity());

        Float coor_x = Float.valueOf(lodging.getMapX());
        Float coor_y = Float.valueOf(lodging.getMapY());

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
                lodgingDetail = getXmlData(id);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!lodgingDetail.getOverview().equals("")) {
                            overviewTextView.setText(Html.fromHtml(lodgingDetail.getOverview()));
                        } else {
                            setVisibility(5);
                        }

                        if (!lodgingDetail.getHomepage().equals("")) {
                            homepageTextView.setText(Html.fromHtml("<b>홈페이지: </b>" +lodgingDetail.getHomepage()));
                        } else {
                            setVisibility(6);
                        }

                        if (!lodgingDetail.getFirstImage().equals("")) {
                            Glide.with(v)
                                    .load(lodgingDetail.getFirstImage())
                                    .placeholder(R.drawable.default_profile_image)
                                    .error(R.drawable.default_profile_image)
                                    .fallback(R.drawable.default_profile_image)
                                    .into(content_img);
                        } else {
                            setVisibility(7);
                        }

                        if (!lodgingDetail.getFirstImage2().equals("")) {
                            Glide.with(v)
                                    .load(lodgingDetail.getFirstImage2())
                                    .placeholder(R.drawable.default_profile_image)
                                    .error(R.drawable.default_profile_image)
                                    .fallback(R.drawable.default_profile_image)
                                    .into(content_img2);
                        } else {
                            setVisibility(8);
                        }

                    }
                });
            }
        }).start();

        mDatabase.collection("BookmarkItem").document(userId).collection("restaurant").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                    deleteBookmark(id);
                }
                else if(bookmarkBtn.isActivated()){
                    // 선택 동작
                    writeBookmark(name, location, x, y, id, telephone);
                }

            }
        });

        return v;
    }

    private void writeBookmark(String name, String location, String x, String y, String id, String number){
        Map<String, Object> info = new HashMap<>();
        info.put("name", name);
        info.put("type", "식당");
        info.put("address", location);
        info.put("position_x", x);
        info.put("position_y", y);
        info.put("serialNumber", id);
        info.put("tel", number);

        mDatabase.collection("BookmarkItem").document(userId).collection("restaurant").document(id).set(info);
    }
    private void deleteBookmark(String id){
        mDatabase.collection("BookmarkItem").document(userId).collection("restaurant").document(id).delete();
    }

    public LodgingDetail getXmlData(String contentId){
        LodgingDetail lodging = new LodgingDetail(
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
            Log.d("LodgingInfori", String.valueOf(url));
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
                            lodging.setFirstImage(xpp.nextText());
                        }
                        else if(tag.equals("firstimage2")){
                            lodging.setFirstImage2(xpp.nextText());
                        }
                        else if(tag.equals("homepage")){
                            lodging.setHomepage(xpp.nextText());
                        }
                        else if(tag.equals("mapx")){
                            lodging.setMapX(xpp.nextText());
                        }
                        else if(tag.equals("mapy")){
                            lodging.setMapY(xpp.nextText());
                        }
                        else if(tag.equals("overview")){
                            lodging.setOverview(xpp.nextText());
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

        return lodging;
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
