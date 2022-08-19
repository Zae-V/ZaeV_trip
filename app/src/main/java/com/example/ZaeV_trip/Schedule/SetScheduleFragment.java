package com.example.ZaeV_trip.Schedule;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ZaeV_trip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SetScheduleFragment extends Fragment {
    private static int RESULT_LOAD_IMAGE = 1;

    TextView title;
    ImageView editTitleImageView;
    ImageView travleImageView;

    Integer Dday;
    Date startDate;
    Date endDate;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    String docId;
    int FLAG = 0;

    @Override
    public void onResume() {
        super.onResume();
        TravelActivity travelActivity = (TravelActivity) getActivity();
        travelActivity.fab.setVisibility(View.GONE);
        travelActivity.bottomNavigationView.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_set_schedule, container, false);

        Button Btn = v.findViewById(R.id.selectFab);

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),TravelActivity.class);
                startActivity(intent);
            }
        });

        TravelActivity travelActivity = (TravelActivity) getActivity();
        travelActivity.fab.setVisibility(View.GONE);
        travelActivity.bottomNavigationView.setVisibility(View.GONE);

        Intent intent = this.getActivity().getIntent();

        if(intent != null && FLAG == 0){
            Dday = Math.abs(intent.getIntExtra("Dday",0));
            startDate = (Date) intent.getSerializableExtra("startDate");
            endDate = (Date) intent.getSerializableExtra("endDate");
            saveScheduleInDB(startDate, endDate);
            FLAG = 1;
        }


        editTitleImageView = v.findViewById(R.id.editTitleImageView);
        title = v.findViewById(R.id.travel_title);
        editTitleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        travleImageView = v.findViewById(R.id.travelImageView);
        travleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                launcher.launch(intent);
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

        setTitle.setText(title.getText());

        okBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                title.setText(setTitle.getText());
                dialog.hide();
                updateName(String.valueOf(setTitle.getText()));

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });
    }


    public void saveScheduleInDB(Date startDate, Date endDate){
        Map<String, Object> schedule = new HashMap<>();
        schedule.put("Time",new Date());
        schedule.put("startDate", startDate);
        schedule.put("endDate", endDate);
        schedule.put("name","제목없음");

        DocumentReference doc = db.collection("Schedule").document(uid).collection("schedule").document();
        docId = doc.getId();
        doc.set(schedule);


    }

    public void updateName(String name){
        if(docId != null){
            db.collection("Schedule").document(uid).collection("schedule").document(docId).update("name",name);
        }

    }

    public void updateImg(String uri){
        if(docId != null){
            db.collection("Schedule").document(uid).collection("schedule").document(docId).update("img",uri);
        }

    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    if (result.getResultCode() == RESULT_OK)
                    {
                        Intent intent = result.getData();
                        Uri uri = intent.getData();
//                        updateImg(uri.toString());
                        Glide.with(getActivity())
                                .load(uri)
                                .into(travleImageView);
                    }
                }
            });

}
