package com.example.ZaeV_trip.Lodging;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.Lodging;
import com.example.ZaeV_trip.model.LodgingDetail;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class LodgingFragment extends Fragment {
    LodgingDetail lodging;
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
        String name = getArguments().getString("name");
        String contentID = getArguments().getString("contentID");
        String location = getArguments().getString("location");

        TextView list_name = v.findViewById(R.id.list_name);
        TextView list_location = v.findViewById(R.id.list_location);
        TextView overview = v.findViewById(R.id.overview);
        TextView homepage = v.findViewById(R.id.homepage);
        ImageView content_img = v.findViewById(R.id.content_img);
        ImageView content_img2 = v.findViewById(R.id.content_img2);

        list_name.setText(name);
        list_location.setText(location);

        new Thread(new Runnable() {
            @Override
            public void run() {
                lodging = getXmlData(contentID);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!lodging.getOverview().equals("")) {
                            overview.setText(Html.fromHtml(lodging.getOverview()));
                        }

                        if (!lodging.getOverview().equals("")) {
                            homepage.setText(Html.fromHtml(lodging.getHomepage()));
                        }

                        if (!lodging.getFirstImage().equals("")) {
                            Glide.with(v)
                                    .load(lodging.getFirstImage())
                                    .placeholder(R.drawable.default_profile_image)
                                    .error(R.drawable.default_profile_image)
                                    .fallback(R.drawable.default_profile_image)
                                    .into(content_img);
                        }

                        if (!lodging.getFirstImage2().equals("")) {
                            Glide.with(v)
                                    .load(lodging.getFirstImage2())
                                    .placeholder(R.drawable.default_profile_image)
                                    .error(R.drawable.default_profile_image)
                                    .fallback(R.drawable.default_profile_image)
                                    .into(content_img2);
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

                    }
                });
            }
        }).start();

        return v;
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
}
