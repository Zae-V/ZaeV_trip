package com.example.ZaeV_trip.Schedule;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ZaeV_trip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SetScheduleFragment extends Fragment {

    EditText title;
    Integer Dday;
    Date startDate;
    Date endDate;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_set_schedule, container, false);

        TravelActivity travelActivity = (TravelActivity) getActivity();
        travelActivity.fab.setVisibility(View.GONE);
        travelActivity.bottomNavigationView.setVisibility(View.GONE);

        Intent intent = this.getActivity().getIntent();
        Dday = Math.abs(intent.getIntExtra("Dday",0));
        startDate = (Date) intent.getSerializableExtra("startDate");
        endDate = (Date) intent.getSerializableExtra("endDate");


        ImageView editTitleImageView = v.findViewById(R.id.editTitleImageView);
        title = v.findViewById(R.id.travel_title);
        editTitleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        ImageView travleImageView = v.findViewById(R.id.travelImageView);
        travleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ArrayList<Integer> Ddays = new ArrayList<Integer>();

        for(int i = 0 ; i <= Dday ; i ++){
            Ddays.add(i+1);
        }


        recyclerView = v.findViewById(R.id.scheduleList);
        ScheduleListAdpater adapter = new ScheduleListAdpater(getActivity(), Ddays);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        return v;
    }

    public void showDialog(){
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_travel_title);

        dialog.show();

        Button okBtn = dialog.findViewById(R.id.titleOKBtn);
        Button cancelBtn = dialog.findViewById(R.id.cancelBtn);
        EditText setTitle = dialog.findViewById(R.id.set_title);

        okBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                title.setText(setTitle.getText());
                dialog.hide();
                saveScheduleInDB(startDate, endDate, String.valueOf(setTitle.getText()));

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });
    }


    public void saveScheduleInDB(Date startDate, Date endDate, String name){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> schedule = new HashMap<>();
        schedule.put("name", name);
        schedule.put("startDate", startDate);
        schedule.put("endDate", endDate);

        db.collection("Schedule").document(uid).collection("schedule").document().set(schedule);
    }

}
