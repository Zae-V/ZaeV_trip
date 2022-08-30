package com.zaev.ZaeV_trip.ZeroWaste;

import android.os.Bundle;
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
import com.zaev.ZaeV_trip.model.ZeroWaste;
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
        float width = getArguments().getFloat("width");

        bookmarkBtn = (ImageView) v.findViewById(R.id.zeroWasteBookmarkBtn);
        titleTextView = v.findViewById(R.id.detail_txt);
        locationTextView = v.findViewById(R.id.location);
        telTextView = v.findViewById(R.id.tel);

        detailLocationTextView = v.findViewById(R.id.detail_location);
        restDateTextView = v.findViewById(R.id.restDate);
        overviewTextView = v.findViewById(R.id.overview);
        homepageTextView = v.findViewById(R.id.homepage);
        content_img = v.findViewById(R.id.content_img);

        titleTextView.setMaxWidth((int) width - Util.ConvertDPtoPX(getActivity().getApplicationContext(), 100));
        titleTextView.setText(name);

        if (!location.equals("")) {
            locationTextView.setText(location);
        } else {
            setVisibility(1);
        }

        if (!location.equals("")) {
            telTextView.setText(telephone);
        } else {
            setVisibility(2);
        }

        if (!detail_location.equals("")) {
            detailLocationTextView.setText(detail_location);
        } else {
            setVisibility(3);
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

        new Thread(new Runnable() {
            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        if (!zeroWasteDetail.getOverview().equals("")) {
//                            overviewTextView.setText(Html.fromHtml(zeroWasteDetail.getOverview()));
//                        } else {
//                            setVisibility(5);
//                        }
//
//                        if (!zeroWasteDetail.getOverview().equals("")) {
//                            homepageTextView.setText(Html.fromHtml(zeroWasteDetail.getHomepage()));
//                        } else {
//                            setVisibility(6);
//                        }

                        if (!image.equals("")) {
                            Glide.with(v)
                                    .load(image)
                                    .placeholder(R.drawable.default_profile_image)
                                    .error(R.drawable.default_profile_image)
                                    .fallback(R.drawable.default_profile_image)
                                    .into(content_img);
                        } else {
                            setVisibility(7);
                        }

                    }
                });
            }
        }).start();

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
            default:
                Log.d("WithdrawalActivity", "default");
        }
    }
}
