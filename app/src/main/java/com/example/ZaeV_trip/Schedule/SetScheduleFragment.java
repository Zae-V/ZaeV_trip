package com.example.ZaeV_trip.Schedule;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SetScheduleFragment extends Fragment {
    private static int RESULT_LOAD_IMAGE = 1;

    TextView title;
    ImageView editTitleImageView;
    ImageView travleImageView;
    Button confirmBtn;

    Integer Dday;
    Date startDate;
    Date endDate;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference riversRef;
    String docId;
    String fileName;
    int FLAG = 0;

    String travelTitle;

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
                Intent intent = new Intent(getActivity(), TravelActivity.class);
                startActivity(intent);
            }
        });

        TravelActivity travelActivity = (TravelActivity) getActivity();
        travelActivity.fab.setVisibility(View.GONE);
        travelActivity.bottomNavigationView.setVisibility(View.GONE);

        Intent intent = this.getActivity().getIntent();
        if (intent != null && FLAG == 0) {
            Dday = Math.abs(intent.getIntExtra("Dday", 0));
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

        confirmBtn = v.findViewById(R.id.selectFab);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (travelTitle != null) {
                    updateName();
                    updateImg();
                    Intent intent1 = new Intent(getContext(), TravelActivity.class);
                    startActivity(intent1);
                } else {
                    showDialog();
                }
            }
        });

        ArrayList<Integer> Ddays = new ArrayList<Integer>();

        for (int i = 0; i <= Dday; i++) {
            Ddays.add(i + 1);
        }

        recyclerView = v.findViewById(R.id.scheduleList);
        ScheduleListAdpater adapter = new ScheduleListAdpater(getActivity(), Ddays);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        return v;
    }

    public void showDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_travel_title);

        dialog.show();

        Button okBtn = dialog.findViewById(R.id.titleOKBtn);
        Button cancelBtn = dialog.findViewById(R.id.cancelBtn);
        EditText setTitle = dialog.findViewById(R.id.set_title);

        setTitle.setText(title.getText());

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setText(setTitle.getText());
                dialog.hide();
//                updateName(String.valueOf(setTitle.getText()));
                travelTitle = String.valueOf(setTitle.getText());
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });
    }


    public void saveScheduleInDB(Date startDate, Date endDate) {
        Map<String, Object> schedule = new HashMap<>();
        schedule.put("Time", new Date());
        schedule.put("startDate", startDate);
        schedule.put("endDate", endDate);

        long days = ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)) % 365;
        schedule.put("days", days);

        DocumentReference doc = db.collection("Schedule").document(uid).collection("schedule").document();
        docId = doc.getId();
        doc.set(schedule);

    }

    public void updateName() {
        if (docId != null) {
            db.collection("Schedule").document(uid).collection("schedule").document(docId).update("name", travelTitle);
        }
    }

    public void updateImg() {
        if (docId != null) {
            Log.d("테스트", "updateImg 함수");
            // 파이어베이스 스토리지에서 이미지 가져오기
            StorageReference photoRef = storageRef.child(fileName);
            photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    db.collection("Schedule").document(uid).collection("schedule").document(docId).update("img", uri);
                }
            });
        }

    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        Uri uri = intent.getData();
                        storageRef = storage.getReference();

                        // 스토리지에 uid + 날짜로 겹치지 않도록 저장
                        Date today = new Date();
                        SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd");
                        SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss a");
                        fileName = uid +"_"+ date.format(today) + "_" +time.format(today);
                        riversRef = storageRef.child(fileName);
                        UploadTask uploadTask = riversRef.putFile(uri);

                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "사진 업로드 실패", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getActivity(), "사진 업로드 성공", Toast.LENGTH_SHORT).show();
                            }
                        });


//                        updateImg(uri.toString());
                        Glide.with(getActivity())
                                .load(uri)
                                .into(travleImageView);

                    }
                }
            });

}
