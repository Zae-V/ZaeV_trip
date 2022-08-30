package com.example.ZaeV_trip.ZeroWaste;

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
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.ZeroWaste;
import com.example.ZaeV_trip.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.HashMap;
import java.util.Map;

public class ZeroWasteFragment extends Fragment {
    FirebaseFirestore mDatabase =FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    TextView titleTextView;
    TextView locationTextView;
    TextView telTextView;
    TextView detailLocationTextView;
    TextView restDateTextView;
    TextView overviewTextView;
    TextView homepageTextView;
    TextView activityTextView;
    TextView menuTextView;
    TextView timeTextView;
    ImageView content_img;
    ImageView bookmarkBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_zero_waste, container, false);

        //Bundle
        ZeroWaste zeroWaste = (ZeroWaste) getArguments().getSerializable("zeroWaste");
        String name = zeroWaste.getName();
        String id = zeroWaste.getContentID();
        String x = zeroWaste.getMapX();
        String y = zeroWaste.getMapY();
        String detail_location = zeroWaste.getAddr1();
        String location = "";
        String telephone = zeroWaste.getTelephone();
        String image = zeroWaste.getImage();
        String time = zeroWaste.getTime();
        String homepage = zeroWaste.getHomepage();
        String activity = zeroWaste.getActivity();
        String menu = zeroWaste.getMenu();

        Log.d("ZeroWasteDBTEST", name);
        Log.d("ZeroWasteDBTEST", id);
        Log.d("ZeroWasteDBTEST", x);
        Log.d("ZeroWasteDBTEST", y);
        Log.d("ZeroWasteDBTEST", detail_location);
        Log.d("ZeroWasteDBTEST", telephone);
        Log.d("ZeroWasteDBTEST", "나 이미지다" + image);
        Log.d("ZeroWasteDBTEST", time);

        float width = getArguments().getFloat("width");

        bookmarkBtn = (ImageView) v.findViewById(R.id.zeroWasteBookmarkBtn);
        titleTextView = v.findViewById(R.id.detail_txt);
        telTextView = v.findViewById(R.id.tel);
        activityTextView = v.findViewById(R.id.activity);
        menuTextView = v.findViewById(R.id.menu);

        detailLocationTextView = v.findViewById(R.id.detail_location);
        restDateTextView = v.findViewById(R.id.restDate);
        overviewTextView = v.findViewById(R.id.overview);
        homepageTextView = v.findViewById(R.id.homepage);
        timeTextView = v.findViewById(R.id.time);
        content_img = v.findViewById(R.id.content_img);

        titleTextView.setMaxWidth((int) width - Util.ConvertDPtoPX(getActivity().getApplicationContext(), 100));
        titleTextView.setText(name);

        if (!telephone.equals("")) {
            telTextView.setText(telephone);
        } else {
            telTextView.setVisibility(View.GONE);
        }

        if (!detail_location.equals("")) {
            detailLocationTextView.setText(Html.fromHtml("<b>상세 장소: </b>" + detail_location));
        } else {
            detailLocationTextView.setVisibility(View.GONE);
        }

        if (!time.equals("")) {
            timeTextView.setText(Html.fromHtml("<b>운영 시간: </b>" + time));
        } else {
            timeTextView.setVisibility(View.GONE);
        }

        if (!homepage.equals("")) {
            homepageTextView.setText(Html.fromHtml("<b>홈페이지: </b>" + homepage));
        } else {
            homepageTextView.setVisibility(View.GONE);
        }

        if (!menu.equals("")) {
            menuTextView.setText(Html.fromHtml("<b>취급 품목(메뉴): </b>" + menu));
        } else {
            menuTextView.setVisibility(View.GONE);
        }

        if (!activity.equals("")) {
            activityTextView.setText(Html.fromHtml("<b>제로웨이스트 활동내용: </b>" + activity));
        } else {
            activityTextView.setVisibility(View.GONE);
        }

        if (!image.equals("")) {
            Glide.with(v)
                    .load("https://map.seoul.go.kr" + image)
                    .placeholder(R.drawable.default_profile_image)
                    .error(R.drawable.default_profile_image)
                    .fallback(R.drawable.default_profile_image)
                    .into(content_img);
        } else {
            content_img.setVisibility(View.GONE);
        }

        MapView mapView = new MapView(getActivity());

        Float coor_x = Float.valueOf(zeroWaste.getMapX());
        Float coor_y = Float.valueOf(zeroWaste.getMapY());

        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(coor_y,coor_x),2,true);

        MapPOIItem marker = new MapPOIItem();
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(coor_y,coor_x));
        marker.setItemName(name);
        marker.setTag(0);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin);
        mapView.addPOIItem(marker);

        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.mapView);
        mapViewContainer.addView(mapView);

        mDatabase.collection("BookmarkItem").document(userId).collection("zeroWaste").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

        mDatabase.collection("BookmarkItem").document(userId).collection("zeroWaste").document(id).set(info);
    }
    
    private void deleteBookmark(String id){
        mDatabase.collection("BookmarkItem").document(userId).collection("zeroWaste").document(id).delete();
    }
}
