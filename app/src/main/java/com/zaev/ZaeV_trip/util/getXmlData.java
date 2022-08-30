package com.zaev.ZaeV_trip.util;

import android.content.Context;
import android.util.Log;

import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.model.Cafe;
import com.zaev.ZaeV_trip.model.Festival;
import com.zaev.ZaeV_trip.model.Lodging;
import com.zaev.ZaeV_trip.model.Plogging;
import com.zaev.ZaeV_trip.model.Restaurant;
import com.zaev.ZaeV_trip.model.Reusable;
import com.zaev.ZaeV_trip.model.TouristSpot;
import com.zaev.ZaeV_trip.model.ZeroWaste;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class getXmlData {

    public static ArrayList<Cafe> getCafeData(Context cnt){
        ArrayList<Cafe> cafes = new ArrayList<Cafe>();

        StringBuffer buffer=new StringBuffer();
        String key= cnt.getString(R.string.vegan_key);

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

    public static ArrayList<Festival> getFestivalData(Context cnt){
        ArrayList<Festival> festivals = new ArrayList<Festival>();
        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";
        String key = cnt.getString(R.string.portal_key);
        String address = "https://api.visitkorea.or.kr/openapi/service/rest/KorService/";
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
        // 현재 날짜 구하기
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String startDate = now.format(formatter);


        String queryUrl = address + listType + "?"
                + "serviceKey=" + key
                + "&numOfRows=" + numOfRows
                + "&pageNo=" + pageNo
                + "&MobileOS=" + mobileOS
                + "&MobileApp=" + mobileApp
                + "&arrange=" + arrange
                + "&listYN=" + listYN
                + "&areaCode=" + areaCode
                + "&eventStartDate=" + startDate;

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
                                    "",
                                    "",
                                    "",
                                    "",
                                    ""
                            );
                        }
                        else if(tag.equals("contentid")){
                            festival.setId(xpp.nextText());
                        }
                        else if(tag.equals("title")){
                            festival.setTitle(xpp.nextText());
                        }
                        else if(tag.equals("addr1")){
                            festival.setAddr1(xpp.nextText());
                        }
                        else if(tag.equals("mapx")){
                            festival.setMapX(xpp.nextText());
                        }
                        else if(tag.equals("mapy")){
                            festival.setMapY(xpp.nextText());
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
                        else if(tag.equals("tel")){
                            festival.setTel(xpp.nextText());
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


    public static ArrayList<Lodging> getLodgingData(Context cnt){
        ArrayList<Lodging> lodgings = new ArrayList<Lodging>();
        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";
        String key = cnt.getString(R.string.portal_key);
        String address = "https://api.visitkorea.or.kr/openapi/service/rest/KorService/";
        String listType = "searchStay";
        String pageNo = "1";
        String numOfRows = "1000";
        String mobileApp = "ZaeVTour";
        String mobileOS = "AND";
        String arrange = "A"; // (A=제목순, B=조회순, C=수정일순, D=생성일순) , 대표이미지가 반드시 있는 정렬 (O=제목순, P=조회순, Q=수정일순, R=생성일순)
        String contentTypeID = "32";
        String areaCode = "1"; // 서울시 = 1
        String listYN = "Y"; // (Y=목록, N=개수)
        String sigunguCode = "13"; //시군구 코드


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
                        else if(tag.equals("addr2")){
                            lodging.setAddr2(xpp.nextText());
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
                        else if(tag.equals("firstimage2")){
                            lodging.setFirstImage2(xpp.nextText());
                        }
                        else if(tag.equals("contentid")){
                            lodging.setContentID(xpp.nextText());
                        }
                        else if(tag.equals("tel")){
                            lodging.setTel(xpp.nextText());
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


    public static ArrayList<Plogging> getPloggingData(Context cnt) {
        ArrayList<Plogging> ploggings = new ArrayList<Plogging>();
        String key = cnt.getString(R.string.portal_key);
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
                        else if(tag.equals("gpxpath")){
                            plogging.setGpxpath(xpp.nextText());
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

    public static ArrayList<Restaurant> getRestaurantData(Context cnt){
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
        String key= cnt.getString(R.string.vegan_key);
        String address = "http://openapi.seoul.go.kr:8088/";
        String listType = "CrtfcUpsoInfo";
        String startIndex = "860";
        String endIndex = "1859";


        String queryUrl = address + key
                + "/xml/" + listType
                + "/" + startIndex
                + "/" + endIndex + "/";
        Log.d("테스트", queryUrl);

        try{
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            Restaurant restaurant = null;
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기
                        if(tag.equals("row")){
                            restaurant = new Restaurant("","","","","","","","","");
                        }
                        else if(tag.equals("CRTFC_UPSO_MGT_SNO")){
                            restaurant.setId(xpp.nextText());
                        }
                        else if(tag.equals("UPSO_NM")){
                            restaurant.setName(xpp.nextText());
                        }
                        else if(tag.equals("RDN_CODE_NM")){
                            restaurant.setLocation(xpp.nextText());
                        }
                        else if(tag.equals("BIZCND_CODE_NM")){
                            restaurant.setCategory(xpp.nextText());
                        }
                        else if(tag.equals("CRTFC_GBN")){
                            restaurant.setAuthType(xpp.nextText());
                        }
                        else if(tag.equals("Y_DNTS")){
                            restaurant.setMapY(xpp.nextText());
                        }
                        else if(tag.equals("X_CNTS")){
                            restaurant.setMapX(xpp.nextText());
                        }
                        else if(tag.equals("FOOD_MENU")){
                            restaurant.setMenu(xpp.nextText());
                        }
                        else if(tag.equals("TEL_NO")){
                            restaurant.setNumber(xpp.nextText());
                        }


                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("row")) {
                            restaurants.add(restaurant);
                            Log.d("테스트",restaurant.getName());
                        };// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return restaurants;
    }

    public static ArrayList<Reusable> getResusableData(Context cnt) {
        ArrayList<Reusable> reusables = new ArrayList<Reusable>();

        try {
            InputStream is = cnt.getResources().getAssets().open("noplasticstore.bom.xls");
            Workbook wb = Workbook.getWorkbook(is);
            Reusable reusable = null;
            if (wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if (sheet != null) {
                    Log.i("test", "불러오기");
                    int colTotal = sheet.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    Log.i("test", "전체 칼럼: " + colTotal);
                    int rowTotal = sheet.getColumn(colTotal - 2).length;
                    Log.i("test", "전체 로우: " + rowTotal);

                    StringBuilder sb;
                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        sb = new StringBuilder();
                        reusable = new Reusable(
                                "",
                                "",
                                "",
                                "",
                                ""
                        );
                        for (int col = 0; col < colTotal; col++) {
                            String contents = sheet.getCell(col, row).getContents();
                            if (col == 0) {
                                reusable.setName(contents);
                            } else if (col == 1) {
                                reusable.setLocation(contents);
                            } else if (col == 2) {
                                reusable.setMapY(contents);
                            } else if (col == 3) {
                                reusable.setMapX(contents);
                            } else if (col == 4) {
                                reusable.setReason(contents);
                            }
                            sb.append("col" + col + " : " + contents + " , ");

                        }
                        reusables.add(reusable);
//                        Log.i("test", sb.toString());
                    }

                }
            }

        }
        catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reusables;
    }

    public static ArrayList<TouristSpot> getTourSpotData(Context cnt){
        ArrayList<TouristSpot> touristSpots = new ArrayList<TouristSpot>();
        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";
        String key = cnt.getString(R.string.portal_key);
        String address = "https://api.visitkorea.or.kr/openapi/service/rest/KorService/";
        String listType = "areaBasedList";
        String pageNo = "1";
        String numOfRows = "100";
        String mobileApp = "ZaeVTour";
        String mobileOS = "AND";
        String arrange = "A"; // (A=제목순, B=조회순, C=수정일순, D=생성일순) , 대표이미지가 반드시 있는 정렬 (O=제목순, P=조회순, Q=수정일순, R=생성일순)
        String contentTypeID = "12";
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
                + "&contentTypeId=" + contentTypeID
                + "&areaCode=" + areaCode
                + "&listYN=" + listYN;

        try{
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            TouristSpot touristSpot = null;

            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) {
                            touristSpot = new TouristSpot(
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
                        else if(tag.equals("title")){
                            touristSpot.setTitle(xpp.nextText());
                        }
                        else if(tag.equals("addr1")){
                            touristSpot.setAddr1(xpp.nextText());
                        }
                        else if(tag.equals("addr2")){
                            touristSpot.setAddr2(xpp.nextText());
                        }
                        else if(tag.equals("mapx")){
                            touristSpot.setMapX(xpp.nextText());
                        }
                        else if(tag.equals("mapy")){
                            touristSpot.setMapY(xpp.nextText());
                        }
                        else if(tag.equals("firstimage")){
                            touristSpot.setFirstImage(xpp.nextText());
                        }
                        else if(tag.equals("firstimage2")){
                            touristSpot.setFirstImage2(xpp.nextText());
                        }
                        else if(tag.equals("contentid")){
                            touristSpot.setContentID(xpp.nextText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName();

                        if(tag.equals("item")) {
                            touristSpots.add(touristSpot);
                        }
                        break;

                }
                eventType= xpp.next();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return touristSpots;
    }

    public static ArrayList<ZeroWaste> getZeroWasteData(Context cnt, String subcate_id){
        ArrayList<ZeroWaste> zeroWastes = new ArrayList<ZeroWaste>();
        String BaseURL = "https://map.seoul.go.kr/smgis/apps/theme.do";
        String cmd = "getContentsList";
        String key = cnt.getString(R.string.zerowaste_key);
        String page_no = "1";
        String page_size = "10";
        String search_name = "";
        String search_type = "1";            //0:거리. 1:이름
        String coord_x = "126.9752884";                //default:"0"
        String coord_y = "37.5649732";                //default:"0"
        String distance = "3000";            //default:"2000"
        String theme_id = "11103395";        //여러 테마는 콤마로 구분; 11103395 : [착한소비] 제로웨이스트상점
        String content_id = "";
        //String subcate_id = "";               //1 : 카페, 2 : 식당, 3 : 리필샵, 4 : 친환경생필품점, 5 : 기타
        //String value_01 = "";                   //value_01 : 상세 첫 번째 컬럼 값 검색; 검색어 입력 (중간어 검색이 됨)

        String result = ""; //파싱한 데이터를 저장할 변수

        String queryUrl = BaseURL + "?"
                + "cmd=" + cmd
                + "&page_no=" + page_no
                + "&page_size=" + page_size
                + "&key=" + key
                + "&coord_x=" + coord_x
                + "&coord_y=" + coord_y
                + "&distance=" + distance
                + "&search_type=" + search_type
                + "&search_name=" + search_name
                + "&theme_id=" + theme_id
                + "&subcate_id=" + theme_id + "," + subcate_id;
        //+ "&=value_01" + value_01;

        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            Log.d("테스트Url", queryUrl);


            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();

            while (line != null) {
                buffer.append(line + "\n");
                line = reader.readLine();
            }

            String jsonData = buffer.toString();

            // jsonData를 먼저 JSONObject 형태로 바꾼다.
            JSONObject obj = new JSONObject(jsonData);

            // obj의 "head"의 JSONObject를 추출
            JSONObject head = (JSONObject)obj.get("head");

            // obj의 "body"의 JSONObject를 추출
            JSONArray body = (JSONArray)obj.get("body");

            try {
                // body의 length만큼 for문 반복
                for (int i = 0; i < body.length(); i++) {
                    // body를 각 JSONObject 형태로 객체를 생성한다.
                    JSONObject temp = body.getJSONObject(i);

                    ZeroWaste zeroWaste = new ZeroWaste(
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "");

                    // zeroWaste의 json 값들을 넣는다.
                    Log.d("테스트API",temp.getString("COT_IMG_MAIN_URL"));
                    zeroWaste.setMapX(temp.getString("COT_COORD_X"));
                    zeroWaste.setMapY(temp.getString("COT_COORD_Y"));
                    zeroWaste.setName(temp.getString("COT_CONTS_NAME"));
                    zeroWaste.setTelephone(temp.getString("COT_TEL_NO"));
                    zeroWaste.setContentID(temp.getString("COT_CONTS_ID"));
                    zeroWaste.setThemeSubID(temp.getString("COT_THEME_SUB_ID"));
                    zeroWaste.setImage("https://map.seoul.go.kr" + temp.getString("COT_IMG_MAIN_URL"));
                    zeroWaste.setAddr1(temp.getString("COT_ADDR_FULL_NEW"));
                    zeroWaste.setAddr2(temp.getString("COT_ADDR_FULL_OLD"));
                    zeroWaste.setKeyword(temp.getString("COT_KW"));

                    zeroWastes.add(zeroWaste);

                }
                // adapter에 적용
                //zeroWastes.add(zeroWaste);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return zeroWastes;
    }

    public static ArrayList<ZeroWaste> getZeroWasteListAll(Context cnt){
        ArrayList<ZeroWaste> zeroWastes = new ArrayList<ZeroWaste>();
        String BaseURL = "https://map.seoul.go.kr/smgis/apps/theme.do";
        String cmd = "getContentsListAll";
        String key = cnt.getString(R.string.zerowaste_key);
        String theme_id = "11103395";

        String result = ""; //파싱한 데이터를 저장할 변수

        String queryUrl = BaseURL + "?"
                + "cmd=" + cmd
                + "&key=" + key
                + "&theme_id=" + theme_id;

        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            Log.d("테스트Url", queryUrl);


            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();

            while (line != null) {
                buffer.append(line + "\n");
                line = reader.readLine();
            }

            String jsonData = buffer.toString();

            // jsonData를 먼저 JSONObject 형태로 바꾼다.
            JSONObject obj = new JSONObject(jsonData);

            // obj의 "head"의 JSONObject를 추출
            JSONObject head = (JSONObject)obj.get("head");

            // obj의 "body"의 JSONObject를 추출
            JSONArray body = (JSONArray)obj.get("body");

            try {
                // body의 length만큼 for문 반복
                for (int i = 0; i < body.length(); i++) {
                    // body를 각 JSONObject 형태로 객체를 생성한다.
                    JSONObject temp = body.getJSONObject(i);

                    ZeroWaste zeroWaste = new ZeroWaste(
                            "",
                            "");

                    // zeroWaste의 json 값들을 넣는다.
                    zeroWaste.setContentID(temp.getString("COT_CONTS_ID"));
                    zeroWaste.setThemeSubID(temp.getString("COT_THEME_SUB_ID"));

                    zeroWastes.add(zeroWaste);

                }
                // adapter에 적용
                //zeroWastes.add(zeroWaste);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return zeroWastes;
    }

    //For RecyclerView

    public static ZeroWaste getZeroWasteMainDetail(Context cnt,String content_id){
        ArrayList<ZeroWaste> zeroWastes = new ArrayList<ZeroWaste>();

        ZeroWaste zeroWaste = new ZeroWaste(
                "",
                "",
                "",
                "");
        String BaseURL = "https://map.seoul.go.kr/smgis/apps/poi.do";
        String cmd = "getNewContentsDetail";
        String key = cnt.getString(R.string.zerowaste_key);
        String theme_id = "11103395";        //여러 테마는 콤마로 구분; 11103395 : [착한소비] 제로웨이스트상점
        //String subcate_id = "";               //1 : 카페, 2 : 식당, 3 : 리필샵, 4 : 친환경생필품점, 5 : 기타

        String result = ""; //파싱한 데이터를 저장할 변수

        String queryUrl = BaseURL + "?"
                + "cmd=" + cmd
                + "&key=" + key
                + "&theme_id=" + theme_id
                + "&conts_id=" + content_id;

        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
//            Log.d("ZeroWasteDBTEST", String.valueOf(url));
            Log.d("테스트Url", queryUrl);


            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();

            while (line != null) {
                buffer.append(line + "\n");
                line = reader.readLine();
            }

            String jsonData = buffer.toString();

            // jsonData를 먼저 JSONObject 형태로 바꾼다.
            JSONObject obj = new JSONObject(jsonData);

            // obj의 "head"의 JSONObject를 추출
            JSONObject head = (JSONObject)obj.get("head");

            // obj의 "body"의 JSONObject를 추출
            JSONArray body = (JSONArray)obj.get("body");

            try {
                // body의 length만큼 for문 반복
                for (int i = 0; i < body.length(); i++) {
                    // body를 각 JSONObject 형태로 객체를 생성한다.
                    JSONObject temp = body.getJSONObject(i);


                    // zeroWaste의 json 값들을 넣는다
                    zeroWaste.setName(temp.getString("COT_CONTS_NAME"));
                    zeroWaste.setContentID(temp.getString("COT_CONTS_ID"));
                    zeroWaste.setThemeSubID(temp.getString("COT_THEME_SUB_ID"));
                    zeroWaste.setAddr1(temp.getString("COT_ADDR_FULL_NEW"));
                    zeroWaste.setMapX(temp.getString("COT_COORD_X"));
                    zeroWaste.setMapY(temp.getString("COT_COORD_Y"));
                    zeroWaste.setImage(temp.getString("COT_IMG_MAIN_URL"));
                    zeroWaste.setTelephone(temp.getString("COT_TEL_NO"));
                    zeroWaste.setTime(temp.getString("COT_VALUE_03"));
                    zeroWaste.setHomepage(temp.getString("COT_VALUE_05"));
                    zeroWaste.setActivity(temp.getString("COT_VALUE_02"));
                    zeroWaste.setMenu(temp.getString("COT_VALUE_04"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return zeroWaste;
    }

    //For Fragment
    public static ArrayList<ZeroWaste> getZeroWasteDetail(Context cnt,String content_id){
        ArrayList<ZeroWaste> zeroWastes = new ArrayList<ZeroWaste>();
        String BaseURL = "https://map.seoul.go.kr/smgis/apps/poi.do";
        String cmd = "getNewContentsDetail";
        String key = cnt.getString(R.string.zerowaste_key);
        String theme_id = "11103395";        //여러 테마는 콤마로 구분; 11103395 : [착한소비] 제로웨이스트상점
        //String subcate_id = "";               //1 : 카페, 2 : 식당, 3 : 리필샵, 4 : 친환경생필품점, 5 : 기타

        String result = ""; //파싱한 데이터를 저장할 변수

        String queryUrl = BaseURL + "?"
                + "cmd=" + cmd
                + "&key=" + key
                + "&theme_id=" + theme_id
                + "&conts_id=" + content_id;

        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            Log.d("테스트Url", queryUrl);


            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();

            while (line != null) {
                buffer.append(line + "\n");
                line = reader.readLine();
            }

            String jsonData = buffer.toString();

            // jsonData를 먼저 JSONObject 형태로 바꾼다.
            JSONObject obj = new JSONObject(jsonData);

            // obj의 "head"의 JSONObject를 추출
            JSONObject head = (JSONObject)obj.get("head");

            // obj의 "body"의 JSONObject를 추출
            JSONArray body = (JSONArray)obj.get("body");

            try {
                // body의 length만큼 for문 반복
                for (int i = 0; i < body.length(); i++) {
                    // body를 각 JSONObject 형태로 객체를 생성한다.
                    JSONObject temp = body.getJSONObject(i);

                    ZeroWaste zeroWaste = new ZeroWaste(
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "");

                    // zeroWaste의 json 값들을 넣는다.
                    Log.d("테스트API",temp.getString("COT_IMG_MAIN_URL"));
                    zeroWaste.setMapX(temp.getString("COT_COORD_X"));
                    zeroWaste.setMapY(temp.getString("COT_COORD_Y"));
                    zeroWaste.setName(temp.getString("COT_CONTS_NAME"));
                    zeroWaste.setTelephone(temp.getString("COT_TEL_NO"));
                    zeroWaste.setContentID(temp.getString("COT_CONTS_ID"));
                    zeroWaste.setThemeSubID(temp.getString("COT_THEME_SUB_ID"));
                    zeroWaste.setImage("https://map.seoul.go.kr" + temp.getString("COT_IMG_MAIN_URL"));
                    zeroWaste.setAddr1(temp.getString("COT_ADDR_FULL_NEW"));
                    zeroWaste.setAddr2(temp.getString("COT_ADDR_FULL_OLD"));
                    zeroWaste.setKeyword(temp.getString("COT_KW"));

                    zeroWastes.add(zeroWaste);

                }
                // adapter에 적용
                //zeroWastes.add(zeroWaste);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return zeroWastes;
    }
}