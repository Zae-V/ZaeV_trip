package com.example.ZaeV_trip.Restaurant;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.Restaurant;
import com.example.ZaeV_trip.util.AddrSearchRepository;
import com.example.ZaeV_trip.util.Location;
import com.example.ZaeV_trip.util.ScrollWebView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestaurantFragment extends Fragment {
    FirebaseFirestore mDatabase =FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    ArrayList<Restaurant> vegans = new ArrayList<>();
    ArrayList<Restaurant> lactos = new ArrayList<>();
    ArrayList<Restaurant> ovos = new ArrayList<>();
    ArrayList<Restaurant> lactoOvos = new ArrayList<>();
    ArrayList<Restaurant> pescos = new ArrayList<>();

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
        View v = inflater.inflate(R.layout.fragment_restaurant, container, false);
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

        webView = (ScrollWebView) v.findViewById(R.id.webView);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient()); // 새 창 띄우지 않기
        webView.getSettings().setJavaScriptEnabled(true);

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

//        Log.d("테스트", "x좌표:"+ x + " y좌표:" + y);
//        Log.d("테스트", "주소:" + location);
//        Log.d("테스트", "주소 슬라이스:" + location.substring(0, 9));

        // 카카오 REST API로 장소 아이디 받아오기
        searchKeyword(name, location, x, y);

        Restaurant restaurant = null;

        String[] strArr = menu.split(", ");
        for(int i=0; i<strArr.length; i++){
            restaurant = new Restaurant("","","","","","","","","");
            if (strArr[i].contains("비건")){
                restaurant.setMenu(strArr[i]);
//                Log.d("테스트", restaurant.getMenu());
                vegans.add(restaurant);
                isVegan = true;
            }
            else if(strArr[i].contains("락토") && !strArr[i].contains("오보")){
                restaurant.setMenu(strArr[i]);
//                Log.d("테스트", restaurant.getMenu());
                lactos.add(restaurant);
                isLacto = true;
            }
            else if(strArr[i].contains("오보") && !strArr[i].contains("락토")){
                restaurant.setMenu(strArr[i]);
//                Log.d("테스트", restaurant.getMenu());
                ovos.add(restaurant);
                isOvo = true;
            }
            else if (strArr[i].contains("락토오보")) {
                restaurant.setMenu(strArr[i]);
//                Log.d("테스트", restaurant.getMenu());
                ovos.add(restaurant);
                isLactoOvo = true;
            }
            else if(strArr[i].contains("페스코")){
                restaurant.setMenu(strArr[i]);
//                Log.d("테스트", restaurant.getMenu());
                pescos.add(restaurant);
                isPesco = true;
            }
        }
        if(isVegan) {
            RestaurantMenuAdapter adapter = new RestaurantMenuAdapter(getActivity(), vegans);
            veganList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            veganList.setAdapter(adapter);
            veganText.setVisibility(v.VISIBLE);
        }
        if(isLacto) {
            RestaurantMenuAdapter adapter = new RestaurantMenuAdapter(getActivity(), lactos);
            lactoList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            lactoList.setAdapter(adapter);
            lactoText.setVisibility(v.VISIBLE);
        }
        if(isOvo) {
            RestaurantMenuAdapter adapter = new RestaurantMenuAdapter(getActivity(), ovos);
            ovoList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            ovoList.setAdapter(adapter);
            ovoText.setVisibility(v.VISIBLE);
        }
        if(isLactoOvo) {
            RestaurantMenuAdapter adapter = new RestaurantMenuAdapter(getActivity(), lactoOvos);
            lactoOvoList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            lactoOvoList.setAdapter(adapter);
            lactoOvoText.setVisibility(v.VISIBLE);
        }
        if(isPesco) {
            RestaurantMenuAdapter adapter = new RestaurantMenuAdapter(getActivity(), pescos);
            pescoList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            pescoList.setAdapter(adapter);
            pescoText.setVisibility(v.VISIBLE);
        }
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
                    writeBookmark(name, location, x, y, id, number, menu);
                }

            }
        });

        return v;
    }

    public void searchKeyword(String name, String location, String x, String y){

        int idx = name.indexOf("(");
        int idx2 = location.indexOf("로");
        if(name.contains("(")) {
            name = name.substring(0, idx);
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

    private void writeBookmark(String name, String location, String x, String y, String id, String number, String menu){
        Map<String, Object> info = new HashMap<>();
        info.put("name", name);
        info.put("type", "식당");
        info.put("address", location);
        info.put("position_x", x);
        info.put("position_y", y);
        info.put("serialNumber", id);
        info.put("tel", number);
        info.put("menu", menu);

        mDatabase.collection("BookmarkItem").document(userId).collection("restaurant").document(id).set(info);
    }
    private void deleteBookmark(String id){
        mDatabase.collection("BookmarkItem").document(userId).collection("restaurant").document(id).delete();
    }
}
