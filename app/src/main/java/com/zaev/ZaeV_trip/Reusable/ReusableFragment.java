package com.zaev.ZaeV_trip.Reusable;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.util.Util;
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

public class ReusableFragment extends Fragment {
    FirebaseFirestore mDatabase =FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    TextView titleTextView;
    TextView locationTextView;
    TextView reasonTextView;

    ImageView bookmarkBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reusable, container, false);

        titleTextView = v.findViewById(R.id.detail_txt);
        locationTextView = v.findViewById(R.id.location);
        reasonTextView = v.findViewById(R.id.reason);

        bookmarkBtn = (ImageView) v.findViewById(R.id.restaurantBookmarkBtn);

        //Bundle
        String name = getArguments().getString("name");
        String location = getArguments().getString("location");
        String x = getArguments().getString("x");
        String y = getArguments().getString("y");
        String reason = getArguments().getString("reason");
        String reasonList = reason;
        StringBuilder reasons = new StringBuilder();
        String r;
        float width = getArguments().getFloat("width");
        boolean finish = false;

        int i = 0;
        int idx = 0;
        while(finish == false) {
            if(reasonList.contains("/")) {
                idx = reasonList.indexOf("/");
                r = "- " + reasonList.substring(i, idx) + System.getProperty("line.separator");
                reasons.append(r);
                Log.d("테스트", String.valueOf(reasons));
                reasonList = reasonList.substring(idx + 2);
            }
            else{
                r = "- " + reasonList;
                reasons.append(r);
                Log.d("테스트", String.valueOf(reasons));
                finish = true;
            }
        }


        titleTextView.setMaxWidth((int) width - Util.ConvertDPtoPX(getActivity().getApplicationContext(), 100));
        titleTextView.setText(name);
        locationTextView.setText(location);
        reasonTextView.setText(reasons);

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

        mDatabase.collection("BookmarkItem").document(userId).collection("reusable").document(name).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                    deleteBookmark(name);
                }
                else if(bookmarkBtn.isActivated()){
                    // 선택 동작
                    writeBookmark(name, location, x, y, reason);
                }
            }
        });

        return v;
    }

    private void writeBookmark(String name, String location, String x, String y, String reason){
        Map<String, Object> info = new HashMap<>();
        info.put("name", name);
        info.put("type", "다회용기");
        info.put("address", location);
        info.put("position_x", x);
        info.put("position_y", y);
        info.put("reason", reason);
        mDatabase.collection("BookmarkItem").document(userId).collection("reusable").document(name).set(info);
    }
    private void deleteBookmark(String name){
        mDatabase.collection("BookmarkItem").document(userId).collection("reusable").document(name).delete();
    }
}

