package com.example.ZaeV_trip.Reusable;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.util.AddrSearchRepository;
import com.example.ZaeV_trip.util.Location;
import com.example.ZaeV_trip.util.ScrollWebView;
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

    ImageView bookmarkBtn;
    ScrollWebView webView = null;
    String kakaoId = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reusable, container, false);

        webView = (ScrollWebView) v.findViewById(R.id.webView);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient()); // 새 창 띄우지 않기
        webView.getSettings().setJavaScriptEnabled(true);

        bookmarkBtn = (ImageView) v.findViewById(R.id.restaurantBookmarkBtn);

        //Bundle
        String name = getArguments().getString("name");
        String location = getArguments().getString("location");
        String x = getArguments().getString("x");
        String y = getArguments().getString("y");
        String reason = getArguments().getString("reason");

        // 카카오 REST API로 장소 아이디 받아오기
        searchKeyword(name, location, x, y);

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

    public void searchKeyword(String name, String location, String x, String y){

        int idx = name.indexOf(" ");
        int idx2 = location.indexOf("로");
        if(name.contains(" ")) {
            name = name.substring(0, idx + 1);
        }
        location = location.substring(0, idx2 + 1);
        String query = name + location;
        Log.d("테스트", query);

        AddrSearchRepository.getINSTANCE(getActivity()).getAddressList(query, x, y, new AddrSearchRepository.AddressResponseListener() {
            @Override
            public void onSuccessResponse(Location locationData) {
                // 제일 상단의 검색 결과 ID 가져오기
                if(locationData.documentsList.size() != 0){
                    kakaoId = locationData.documentsList.get(0).getId();
                    Log.d("테스트", kakaoId);
                    webView.loadUrl("https://place.map.kakao.com/" + kakaoId);
                }
                else{
                    webView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailResponse() {
                Log.d("테스트", "실패");
            }
        });
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

