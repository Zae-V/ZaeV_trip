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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ZaeV_trip.Bookmark.BookmarkItem;

import com.example.ZaeV_trip.Cafe.CafeActivity;
import com.example.ZaeV_trip.Cafe.CafeAdapter;
import com.example.ZaeV_trip.Cafe.CafeFragment;
import com.example.ZaeV_trip.Festival.FestivalActivity;
import com.example.ZaeV_trip.Festival.FestivalAdapter;
import com.example.ZaeV_trip.Festival.FestivalFragment;
import com.example.ZaeV_trip.Lodging.LodgingActivity;
import com.example.ZaeV_trip.Lodging.LodgingAdapter;
import com.example.ZaeV_trip.Lodging.LodgingFragment;
import com.example.ZaeV_trip.Plogging.PloggingActivity;
import com.example.ZaeV_trip.Plogging.PloggingAdapter;
import com.example.ZaeV_trip.Plogging.PloggingFragment;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Restaurant.RestaurantActivity;
import com.example.ZaeV_trip.Restaurant.RestaurantAdapter;
import com.example.ZaeV_trip.Restaurant.RestaurantFragment;
import com.example.ZaeV_trip.Reusable.ReusableActivity;
import com.example.ZaeV_trip.Reusable.ReusableAdapter;
import com.example.ZaeV_trip.Reusable.ReusableFragment;
import com.example.ZaeV_trip.TouristSpot.TouristSpotActivity;
import com.example.ZaeV_trip.TouristSpot.TouristSpotAdapter;
import com.example.ZaeV_trip.TouristSpot.TouristSpotFragment;
import com.example.ZaeV_trip.ZeroWaste.ZeroWasteActivity;
import com.example.ZaeV_trip.ZeroWaste.ZeroWasteAdapter;
import com.example.ZaeV_trip.ZeroWaste.ZeroWasteFragment;
import com.example.ZaeV_trip.model.Cafe;
import com.example.ZaeV_trip.model.Festival;
import com.example.ZaeV_trip.model.Lodging;
import com.example.ZaeV_trip.model.Plogging;
import com.example.ZaeV_trip.model.Restaurant;
import com.example.ZaeV_trip.model.Reusable;
import com.example.ZaeV_trip.model.TouristSpot;
import com.example.ZaeV_trip.model.ZeroWaste;
import com.example.ZaeV_trip.util.getXmlData;

import java.io.Serializable;
import java.util.ArrayList;

public class AddScheduleFragment extends Fragment {
//    private FragmentAddScheduleBinding binding;
    RecyclerView list;
    View v;
    String local;
    String selectedCategory ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.fragment_add_schedule, container, false);

        TravelActivity travelActivity = (TravelActivity) getActivity();
        travelActivity.bottomNavigationView.setVisibility(View.GONE);

        //recyclerview
        list = (RecyclerView) v.findViewById(R.id.addRecycler);

        local = "전체";
        selectedCategory = "관광명소";

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
        String[] category = {"관광명소", "숙소", "식당", "카페", "다회용기", "플로깅", "제로웨이스트 샵","축제"};

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
                local = cityName[i];
                updateRecyclerView(cityName[i], selectedCategory);
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
                selectedCategory = category[i];
                updateRecyclerView(local, category[i]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getContext(), "선택을 취소했습니다.", Toast.LENGTH_SHORT).show();
            }
        });


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

    public void updateRecyclerView(String local, String selectedCategory){
        switch (selectedCategory){
            case "카페":
                getCafeList(local);
                break;
            case "다회용기":
                getReusableList(local);
                break;
            case "축제":
                getFestivalList(local);
                break;
            case "식당":
                getRestaurantList(local);
                break;
            case "플로깅":
                getPloggingList(local);
                break;
            case "제로웨이스트 샵":
                getZeroWasteList(local);
                break;
            case "숙소":
                getLodgingList(local);
                break;
            default:
                getTourSpotList(local);
                break;

        }
    }

    public void getCafeList(String local){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Cafe> cafes = getXmlData.getCafeData(getActivity());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<SelectItem> filterdList = new ArrayList<SelectItem>();

                        for(int i = 0; i< cafes.size();i++){
                            if(cafes.get(i).getCategory() != null && (cafes.get(i).getCategory().equals("카페") || cafes.get(i).getCategory().equals("베이커리") || cafes.get(i).getCategory().equals("까페"))){
                                if(local.equals("전체 지역") || local.equals("전체")){
                                    filterdList.add(
                                            new SelectItem(null, cafes.get(i).getName(), cafes.get(i).getLocation(), cafes.get(i).getCategory()));
                                }
                                else{
                                    if(cafes.get(i).getLocation().split(" ")[1].equals(local)){
                                        filterdList.add(new SelectItem(null, cafes.get(i).getName(), cafes.get(i).getLocation(), cafes.get(i).getCategory()));
                                    }
                                }
                            }
                        }
                        SelectListAdapter adapter = new SelectListAdapter(getActivity(),filterdList);
//                        CafeAdapter cafeAdapter = new CafeAdapter(getActivity() ,filterdList);
                        list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
                        list.setAdapter(adapter);

                    }
                });

            }
        }).start();
    }

    public void getFestivalList(String local){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Festival> festivals = getXmlData.getFestivalData(getActivity());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<SelectItem> filteredList = new ArrayList<SelectItem>();

                        for(int i = 0; i< festivals.size(); i++) {
                            if(local.equals("전체 지역") || local.equals("전체")){
//                                adapter.addItem(festivals.get(i));
                                filteredList.add(new SelectItem(festivals.get(i).getFirstImage(),festivals.get(i).getTitle(),festivals.get(i).getAddr1(), festivals.get(i).getStartDate() + " - " +festivals.get(i).getEndDate()));
                            }else{
                                if(festivals.get(i).getAddr1().split(" ").length > 1 && festivals.get(i).getAddr1().split(" ")[1].equals(local)){
//                                    adapter.addItem(festivals.get(i));
                                    filteredList.add(new SelectItem(festivals.get(i).getFirstImage(),festivals.get(i).getTitle(),festivals.get(i).getAddr1(), festivals.get(i).getStartDate() + " - " +festivals.get(i).getEndDate()));
                                }
                            }
                        }
                        SelectListAdapter adapter = new SelectListAdapter(getActivity(), filteredList);
                        list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                        list.setAdapter(adapter);

                    }

                });
            }
        }).start();

    }

    public void getReusableList(String local){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Reusable> reusables = getXmlData.getResusableData(getActivity());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<SelectItem> filteredReusable = new ArrayList<SelectItem>();

                        for(int i = 0; i< reusables.size(); i++) {
                            if (local.equals("전체 지역") || local.equals("전체")) {
                                filteredReusable.add(new SelectItem(null, reusables.get(i).getName(), reusables.get(i).getLocation(), reusables.get(i).getReason()));
                            }
                            else {
                                if (reusables.get(i).getLocation().contains(local)) {
                                    filteredReusable.add(new SelectItem(null, reusables.get(i).getName(), reusables.get(i).getLocation(), reusables.get(i).getReason()));
                                }
                            }
                            SelectListAdapter adapter = new SelectListAdapter(getActivity(), filteredReusable);
                            list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                            list.setAdapter(adapter);

                        }
                    }
                });
            }
        }).start();

    }

    public void getTourSpotList(String local){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<TouristSpot> touristSpots = getXmlData.getTourSpotData(getActivity());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<SelectItem> filteredTouristSpot = new ArrayList<SelectItem>();

                        for(int i = 0; i< touristSpots.size(); i++) {
                            if (local.equals("전체 지역") || local.equals("전체")) {
                                filteredTouristSpot.add(new SelectItem(touristSpots.get(i).getFirstImage(),touristSpots.get(i).getTitle(),touristSpots.get(i).getAddr1(),touristSpots.get(i).getAddr2()));
                            }
                            else {
                                if (touristSpots.get(i).getAddr1().contains(local)) {
                                    filteredTouristSpot.add(new SelectItem(touristSpots.get(i).getFirstImage(),touristSpots.get(i).getTitle(),touristSpots.get(i).getAddr1(),touristSpots.get(i).getAddr2()));
                                }
                            }
                            SelectListAdapter adapter = new SelectListAdapter(getActivity(), filteredTouristSpot);
                            list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                            list.setAdapter(adapter);


                        }
                    }
                });
            }
        }).start();

    }

    public void getRestaurantList(String local){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Restaurant> restaurants = getXmlData.getRestaurantData(getActivity(), "860", "1859");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<SelectItem> filteredRestaurant = new ArrayList<SelectItem>();

                        for(int i = 0; i< restaurants.size(); i++) {
                            if (local.equals("전체 지역") || local.equals("전체")) {
                                filteredRestaurant.add(new SelectItem(null, restaurants.get(i).getName(),restaurants.get(i).getLocation(),restaurants.get(i).getCategory()));
                            }
                            else {
                                if (restaurants.get(i).getLocation().contains(local)) {
                                    filteredRestaurant.add(new SelectItem(null, restaurants.get(i).getName(),restaurants.get(i).getLocation(),restaurants.get(i).getCategory()));
                                }
                            }
                            SelectListAdapter adapter = new SelectListAdapter(getActivity(), filteredRestaurant);
                            list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                            list.setAdapter(adapter);

                        }
                    }
                });
            }
        }).start();

    }

    public void getPloggingList(String local){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Plogging> ploggings = getXmlData.getPloggingData(getActivity());

                getActivity().runOnUiThread(new Runnable() {
                    ArrayList<SelectItem> filteredPlogging = new ArrayList<SelectItem>();

                    @Override
                    public void run() {
                        for(int i = 0; i< ploggings.size(); i++) {
                            if (local.equals("전체 지역") || local.equals("전체")) {
                                filteredPlogging.add(new SelectItem(null, ploggings.get(i).getCrsKorNm(), ploggings.get(i).getSigun(), ploggings.get(i).getCrsTourInfo()));
                            }
                            else {
                                filteredPlogging.add(new SelectItem(null, ploggings.get(i).getCrsKorNm(), ploggings.get(i).getSigun(), ploggings.get(i).getCrsLevel()));
                                }
                            SelectListAdapter adapter = new SelectListAdapter(getActivity(), filteredPlogging);
                            list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                            list.setAdapter(adapter);

                        }
                    }
                });
            }
        }).start();

    }

    public void getLodgingList(String local){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Lodging> lodgings = getXmlData.getLodgingData(getActivity());

                getActivity().runOnUiThread(new Runnable() {
                    ArrayList<SelectItem> filteredLodging = new ArrayList<SelectItem>();

                    @Override
                    public void run() {
                        for(int i = 0; i< lodgings.size(); i++) {
                            if (local.equals("전체 지역") || local.equals("전체")) {
                                filteredLodging.add(new SelectItem(lodgings.get(i).getFirstImage(),lodgings.get(i).getTitle(), lodgings.get(i).getAddr1(), lodgings.get(i).getAddr2()));
                            }
                            else {
                                if (lodgings.get(i).getAddr1().contains(local)) {
                                    filteredLodging.add(new SelectItem(lodgings.get(i).getFirstImage(),lodgings.get(i).getTitle(), lodgings.get(i).getAddr1(), lodgings.get(i).getAddr2()));
                                }
                            }
                            SelectListAdapter adapter = new SelectListAdapter(getActivity(), filteredLodging);
                            list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                            list.setAdapter(adapter);

                        }
                    }
                });
            }
        }).start();

    }

    public void getZeroWasteList(String local){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<ZeroWaste> zeroWastes = getXmlData.getZeroWasteData(getActivity());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<SelectItem> filteredZeroWaste = new ArrayList<SelectItem>();

                        for(int i = 0; i< zeroWastes.size(); i++) {
                            if (local.equals("전체 지역") || local.equals("전체")) {
                                filteredZeroWaste.add(new SelectItem(null, zeroWastes.get(i).getName(),zeroWastes.get(i).getLocation(),zeroWastes.get(i).getReason()));
                            }
                            else {
                                if (zeroWastes.get(i).getLocation().contains(local)) {
                                    filteredZeroWaste.add(new SelectItem(null, zeroWastes.get(i).getName(),zeroWastes.get(i).getLocation(),zeroWastes.get(i).getReason()));
                                }
                            }
                            SelectListAdapter adapter = new SelectListAdapter(getActivity(), filteredZeroWaste);
                            list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                            list.setAdapter(adapter);
                         }
                    }
                });
            }
        }).start();

    }


}
