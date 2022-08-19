package com.example.ZaeV_trip.Schedule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ZaeV_trip.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarFragment extends Fragment{

    Button searchDateBtn;
    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_calendar, container, false);

        TravelActivity travelActivity = (TravelActivity) getActivity();
        travelActivity.fab.setVisibility(View.GONE);

        searchDateBtn = v.findViewById(R.id.searchDate);

        searchDateBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
                builder.setTitleText("Date Picker");

                MaterialDatePicker materialDatePicker = builder.build();

                materialDatePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
                //dialog.show(getActivity().getSupportFragmentManager(), "Dialog");

                //확인버튼
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
                        Date date1 = new Date();
                        Date date2 = new Date();

                        date1.setTime(selection.first);
                        date2.setTime(selection.second);

                        long calculate = date1.getTime() - date2.getTime();
                        int Ddays = (int) (calculate / (24*60*60*1000));

                        // fragment -> setSchedule fragment 이동
                        TravelActivity activity = (TravelActivity) getActivity();
                        Intent intent = new Intent();
                        intent.putExtra("Dday", Ddays);
                        intent.putExtra("startDate",date1);
                        intent.putExtra("endDate", date2);
                        activity.setIntent(intent);
                        activity.changeFragment(1);
                    }
                });
            }
        });
        return v;
    }

}