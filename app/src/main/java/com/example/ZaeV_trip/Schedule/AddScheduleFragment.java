package com.example.ZaeV_trip.Schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ZaeV_trip.Bookmark.BookmarkItem;

import com.example.ZaeV_trip.R;

import java.util.ArrayList;

public class AddScheduleFragment extends Fragment {
//    private FragmentAddScheduleBinding binding;
    RecyclerView bookmarkRecyclerView;
    SelectListAdapter listAdapter;
    View v;
    //ItemTouchHelperCallback helper;
    ArrayList<BookmarkItem> items = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        BookmarkViewModel bookmarkViewModel =
//                new ViewModelProvider(this).get(BookmarkViewModel.class);

//        binding = FragmentAddScheduleBinding.inflate(inflater, container, false);
        //final TextView textView = binding.bookmarkText;

        //bookmarkViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        v =  inflater.inflate(R.layout.fragment_add_schedule, container, false);

        // searchBar
        SearchView searchBar = v.findViewById(R.id.scheduleSearchBar);
        searchBar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                searchBar.setIconified(false);
            }
        });

        // 선택완료 버튼
        Button selectFab = v.findViewById(R.id.selectFab);
        selectFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TravelActivity activity = (TravelActivity) getActivity();
                activity.changeFragment(3);
            }
        });
        // spinner
        String[] cityName = {"전체","강남구","강동구","강북구","강서구","관악구","광진구","구로구","금천구","노원구","도봉구","동대문구",
                "동작구","마포구","서대문구","서초구","성동구","성북구","송파구","양천구","영등포구","용산구","은평구","종로구","종구","중랑구"};
        String[] category = {"전체보기", "관광명소", "숙소", "식당", "카페", "다회용기", "플로깅", "제로웨이스트 샵"};

        Spinner locationSpinner = v.findViewById(R.id.locationSpinner);
        Spinner categorySpinner = v.findViewById(R.id.categorySpinner);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(
                getActivity(), R.layout.dialog_spinner_item, cityName
        );
        adapter.setDropDownViewResource(R.layout.dialog_spinner_item);
        locationSpinner.setAdapter(adapter);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), cityName[i], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getContext(), "선택을 취소했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<String> adapter2;
        adapter2 = new ArrayAdapter<String>(
                getActivity(), R.layout.dialog_spinner_item, category
        );
        adapter2.setDropDownViewResource(R.layout.dialog_spinner_item);
        categorySpinner.setAdapter(adapter2);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), category[i], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getContext(), "선택을 취소했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //recyclerview
        bookmarkRecyclerView = (RecyclerView) v.findViewById(R.id.addRecycler);
        bookmarkRecyclerView.setHasFixedSize(true);
        listAdapter = new SelectListAdapter(items);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        bookmarkRecyclerView.setLayoutManager(mLayoutManager);
        bookmarkRecyclerView.setItemAnimator(new DefaultItemAnimator());
        bookmarkRecyclerView.setAdapter(listAdapter);

        //ItemTouchHelper 생성
//        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(listAdapter);
//        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//        touchHelper.attachToRecyclerView(bookmarkRecyclerView);



        //Adapter에 데이터 추가
        BookmarkItem bookmarkItem1 = new BookmarkItem(R.drawable.vegan_burger,"제비식당","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");
        BookmarkItem bookmarkItem2 = new BookmarkItem(R.drawable.vegan_burger,"야채꼬치","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");
        BookmarkItem bookmarkItem3 = new BookmarkItem(R.drawable.vegan_burger,"비건버거","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");
        BookmarkItem bookmarkItem4 = new BookmarkItem(R.drawable.vegan_burger,"초록숲상점","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");
        BookmarkItem bookmarkItem5 = new BookmarkItem(R.drawable.vegan_burger,"제로웨이스트샵","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");
        BookmarkItem bookmarkItem6 = new BookmarkItem(R.drawable.vegan_burger,"제비식당","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");
        BookmarkItem bookmarkItem7 = new BookmarkItem(R.drawable.vegan_burger,"야채꼬치","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");
        BookmarkItem bookmarkItem8 = new BookmarkItem(R.drawable.vegan_burger,"비건버거","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");
        BookmarkItem bookmarkItem9 = new BookmarkItem(R.drawable.vegan_burger,"초록숲상점","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");
        BookmarkItem bookmarkItem10 = new BookmarkItem(R.drawable.vegan_burger,"제로웨이스트샵","서울시 강서구 개화동", "AM 9:00 ~ PM 10:00");

        listAdapter.addItem(bookmarkItem1);
        listAdapter.addItem(bookmarkItem2);
        listAdapter.addItem(bookmarkItem3);
        listAdapter.addItem(bookmarkItem4);
        listAdapter.addItem(bookmarkItem5);
        listAdapter.addItem(bookmarkItem6);
        listAdapter.addItem(bookmarkItem7);
        listAdapter.addItem(bookmarkItem8);
        listAdapter.addItem(bookmarkItem9);
        listAdapter.addItem(bookmarkItem10);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }

    public boolean onBackPressed() {
        if(v == null){
            return false;
        }return true;
    }
}
