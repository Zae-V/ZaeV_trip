package com.example.ZaeV_trip.Schedule;

import android.app.Dialog;
import android.os.Bundle;
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

import com.example.ZaeV_trip.R;

public class SetScheduleFragment extends Fragment {

    EditText title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_set_schedule, container, false);

        TravelActivity travelActivity = (TravelActivity) getActivity();
        travelActivity.fab.setVisibility(View.GONE);

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

        Button addBtn = v.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TravelActivity activity = (TravelActivity) getActivity();
                activity.changeFragment(2);
            }
        });

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
                // addSchedule fragment 로 이동
//                TravelActivity activity = (TravelActivity) getActivity();
//                activity.changeFragment(2);
                title.setText(setTitle.getText());
                dialog.hide();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });
    }
}
