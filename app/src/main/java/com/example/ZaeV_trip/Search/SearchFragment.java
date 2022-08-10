package com.example.ZaeV_trip.Search;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.ZaeV_trip.Main.MainActivity;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Search.Adapter.CityAdapter;
import com.example.ZaeV_trip.Search.Adapter.VisitedAdapter;
import com.example.ZaeV_trip.util.SharedViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchFragment extends Fragment {
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BottomNavigationView navView = getActivity().findViewById(R.id.bottom_navigation);
        navView.setVisibility(View.VISIBLE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_search, container, false);

        BottomNavigationView navView = getActivity().findViewById(R.id.bottom_navigation);
        navView.setVisibility(View.GONE);

        ArrayList<String> CityName = new ArrayList<String>(Stream.of("전체", "강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구", "도봉구", "동대문구",
                "동작구", "마포구", "서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "종구", "중랑구").collect(Collectors.toList()));
        SharedViewModel searchViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        ArrayList<String> Visited = searchViewModel.getVisitedCities();

        GridView allList = v.findViewById(R.id.allList);

        CityAdapter cityAdapter = new CityAdapter((getContext()),CityName);
        allList.setAdapter(cityAdapter);
        allList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                searchViewModel.sendMessage(CityName.get(i));
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction().remove(SearchFragment.this).commit();
//                fragmentManager.popBackStack();
                searchViewModel.setVisitedCities(CityName.get(i));
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("current", CityName.get(i));
                startActivity(intent);
//                navView.setVisibility(View.VISIBLE);

            }
        });

        GridView recentVisit = v.findViewById(R.id.recentVisit);

        VisitedAdapter recentCityAdapter = new VisitedAdapter(getContext(),Visited);
        recentVisit.setAdapter(recentCityAdapter);
        recentVisit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                searchViewModel.sendMessage(Visited.get(i));
                searchViewModel.setVisitedCities(Visited.get(i));
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction().remove(SearchFragment.this).commit();
//                fragmentManager.popBackStack();
//                navView.setVisibility(View.VISIBLE);

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("current", Visited.get(i));
                startActivity(intent);
            }
        });

        return v;
    }
}