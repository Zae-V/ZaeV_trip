package com.zaev.ZaeV_trip.Cafe;

import static java.lang.Integer.parseInt;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.model.Cafe;
import com.zaev.ZaeV_trip.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CafeFragment extends Fragment {
    FirebaseFirestore mDatabase =FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    TextView titleTextView;
    TextView telTextView;
    TextView detailLocationTextView;
    TextView foodInfoTextView;

    ArrayList<Cafe> vegans = new ArrayList<>();
    ArrayList<Cafe> lactos = new ArrayList<>();
    ArrayList<Cafe> ovos = new ArrayList<>();
    ArrayList<Cafe> lactoOvos = new ArrayList<>();
    ArrayList<Cafe> pescos = new ArrayList<>();

    RecyclerView veganList;
    RecyclerView lactoList;
    RecyclerView ovoList;
    RecyclerView lactoOvoList;
    RecyclerView pescoList;

    TextView veganText;
    TextView lactoText;
    TextView ovoText;
    TextView lactoOvoText;
    TextView pescoText;

    ImageView bookmarkBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_restaurant, container, false);

        titleTextView = v.findViewById(R.id.detail_txt);
        telTextView = v.findViewById(R.id.tel);
        detailLocationTextView = v.findViewById(R.id.detail_location);
        foodInfoTextView = v.findViewById(R.id.food_info);

        veganList = (RecyclerView) v.findViewById(R.id.veganMenuList);
        lactoList = (RecyclerView) v.findViewById(R.id.lactoMenuList);
        ovoList = (RecyclerView) v.findViewById(R.id.ovoMenuList);
        lactoOvoList = (RecyclerView) v.findViewById(R.id.lactoOvoMenuList);
        pescoList = (RecyclerView) v.findViewById(R.id.pescoMenuList);

        veganText = v.findViewById(R.id.veganMenuText);
        lactoText = v.findViewById(R.id.lactoMenuText);
        ovoText = v.findViewById(R.id.ovoMenuText);
        lactoOvoText = v.findViewById(R.id.lactoOvoMenuText);
        pescoText = v.findViewById(R.id.pescoMenuText);

        veganText.setVisibility(v.GONE);
        lactoText.setVisibility(v.GONE);
        ovoText.setVisibility(v.GONE);
        lactoOvoText.setVisibility(v.GONE);
        pescoText.setVisibility(v.GONE);

        bookmarkBtn = (ImageView) v.findViewById(R.id.restaurantBookmarkBtn);

        Boolean isVegan = false;
        Boolean isLacto = false;
        Boolean isOvo = false;
        Boolean isLactoOvo = false;
        Boolean isPesco = false;
        
        //Bundle
        String name = getArguments().getString("name");
        String location = getArguments().getString("location");
        String id = getArguments().getString("id");
        String category = getArguments().getString("category");
        String x = getArguments().getString("x");
        String y = getArguments().getString("y");
        String number = getArguments().getString("number");
        String menu = getArguments().getString("menu");
        float width = getArguments().getFloat("width");

        titleTextView.setText(name);
        telTextView.setText(number);
        detailLocationTextView.setText(Html.fromHtml("<b>상세 주소: </b>" + location));
        foodInfoTextView.setText(category);

        titleTextView.setMaxWidth((int) width - Util.ConvertDPtoPX(getActivity().getApplicationContext(), 100));

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

        Cafe cafe = null;
        String[] strArr = menu.split(", ");
        for(int i=0; i<strArr.length; i++){
            cafe = new Cafe("","","","","","","","");
            if (strArr[i].contains("비건")){
                cafe.setMenu(strArr[i]);
                vegans.add(cafe);
                isVegan = true;
            }
            else if(strArr[i].contains("락토") && !strArr[i].contains("오보")){
                cafe.setMenu(strArr[i]);
                lactos.add(cafe);
                isLacto = true;
            }
            else if(strArr[i].contains("오보") && !strArr[i].contains("락토")){
                cafe.setMenu(strArr[i]);
                ovos.add(cafe);
                isOvo = true;
            }
            else if (strArr[i].contains("락토오보")) {
                cafe.setMenu(strArr[i]);
                ovos.add(cafe);
                isLactoOvo = true;
            }
            else if(strArr[i].contains("페스코")){
                cafe.setMenu(strArr[i]);
                pescos.add(cafe);
                isPesco = true;
            }
        }
        if(isVegan) {
            CafeMenuAdapter adapter = new CafeMenuAdapter(getActivity(), vegans);
            veganList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            veganList.setAdapter(adapter);
            veganText.setVisibility(v.VISIBLE);
        }
        if(isLacto) {
            CafeMenuAdapter adapter = new CafeMenuAdapter(getActivity(), lactos);
            lactoList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            lactoList.setAdapter(adapter);
            lactoText.setVisibility(v.VISIBLE);
        }
        if(isOvo) {
            CafeMenuAdapter adapter = new CafeMenuAdapter(getActivity(), ovos);
            ovoList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            ovoList.setAdapter(adapter);
            ovoText.setVisibility(v.VISIBLE);
        }
        if(isLactoOvo) {
            CafeMenuAdapter adapter = new CafeMenuAdapter(getActivity(), lactoOvos);
            lactoOvoList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            lactoOvoList.setAdapter(adapter);
            lactoOvoText.setVisibility(v.VISIBLE);
        }
        if(isPesco) {
            CafeMenuAdapter adapter = new CafeMenuAdapter(getActivity(), pescos);
            pescoList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            pescoList.setAdapter(adapter);
            pescoText.setVisibility(v.VISIBLE);
        }

        mDatabase.collection("BookmarkItem").document(userId).collection("cafe").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                    writeBookmark(name, location, x, y, id, number, menu);
                }

            }
        });

        return v;
    }

    private void writeBookmark(String name, String location, String x, String y, String id, String number, String menu){
        Map<String, Object> info = new HashMap<>();
        info.put("name", name);
        info.put("type", "카페");
        info.put("address", location);
        info.put("position_x", x);
        info.put("position_y", y);
        info.put("serialNumber", id);
        info.put("tel", number);
        info.put("menu", menu);

        mDatabase.collection("BookmarkItem").document(userId).collection("cafe").document(id).set(info);
    }
    private void deleteBookmark(String id){
        mDatabase.collection("BookmarkItem").document(userId).collection("cafe").document(id).delete();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        CafeActivity cafeActivity = (CafeActivity) getActivity();
        cafeActivity.cafeAdapter.notifyDataSetChanged();
    }

}