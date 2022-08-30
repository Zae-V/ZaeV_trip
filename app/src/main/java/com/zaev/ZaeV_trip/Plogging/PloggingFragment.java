package com.zaev.ZaeV_trip.Plogging;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.zaev.ZaeV_trip.Main.MainActivity;
import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.model.Plogging;
import com.zaev.ZaeV_trip.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PloggingFragment extends Fragment {
    FirebaseFirestore mDatabase =FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_plogging, container, false);

        float width = getArguments().getFloat("width");
        Log.d("DeviceDP","dpWidth : "+width);
        Plogging plogging = (Plogging) getArguments().getSerializable("plogging");

        TextView plogging_name = v.findViewById(R.id.plogging_name);
        TextView plogging_level = v.findViewById(R.id.plogging_level);
        TextView plogging_hour = v.findViewById(R.id.plogging_hour);
        TextView plogging_dist = v.findViewById(R.id.plogging_dist);
        TextView plogging_content = v.findViewById(R.id.plogging_content);
        TextView plogging_detail = v.findViewById(R.id.plogging_detail);
        TextView plogging_detail_txt = v.findViewById(R.id.plogging_detail_txt);
        TextView plogging_point = v.findViewById(R.id.plogging_point);
        TextView plogging_point_txt = v.findViewById(R.id.plogging_point_txt);
        TextView plogging_traveler_info = v.findViewById(R.id.plogging_traveler_info);
        TextView plogging_traveler_info_txt = v.findViewById(R.id.plogging_traveler_info_txt);
        TextView plogging_gpx = v.findViewById(R.id.plogging_gpx);
        TextView plogging_gpx_txt = v.findViewById(R.id.plogging_gpx_txt);
        ImageView bookmarkBtn = (ImageView) v.findViewById(R.id.bookmarkBtn);

        plogging_name.setMaxWidth((int) width - Util.ConvertDPtoPX(getActivity().getApplicationContext(), 100));

        String crsKorNm = plogging.getCrsKorNm();
        String crsLevel = plogging.getCrsLevel();
        String crsTotlRqrmHour = plogging.getCrsTotlRqrmHour();
        String crsDstnc = plogging.getCrsDstnc();
        String crsSummary = plogging.getCrsSummary();
        String crsContent = plogging.getCrsContents();
        String crsTourInfo = plogging.getCrsTourInfo();
        String travelerinfo = plogging.getTravelerinfo();
        String gpx_path = plogging.getGpxpath();
        String sigun = plogging.getSigun();

        if (!crsKorNm.equals("")) {
            plogging_name.setText(crsKorNm);
        } else {
            plogging_name.setVisibility(View.GONE);
        }

        if (!crsLevel.equals("")) {
            int level = Integer.parseInt(crsLevel);
            String lev="";
            String word = "";
            String color = "";
            int term = 2;
            switch (level){
                case 1:
                    lev = "난이도: 쉬움";
                    word = "쉬움";
                    color = "#418EE8";
                    break;
                case 2:
                    lev = "난이도: 보통";
                    word = "보통";
                    color = "#FF9C41";
                    break;
                case 3:
                    lev = "난이도: 어려움";
                    word = "어려움";
                    color = "#F255A0";
                    term = 3;
                    break;
            }
            SpannableString spannableString = new SpannableString(lev);
            int loc = lev.indexOf(word);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(color)),
                    loc, loc + term, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            plogging_level.setText(spannableString);

        } else {
            plogging_level.setVisibility(View.GONE);
        }

        if (!crsTotlRqrmHour.equals("")) {
            plogging_hour.setText("소요 시간: " + crsTotlRqrmHour + "분");
        } else {
            plogging_hour.setVisibility(View.GONE);
        }

        if (!crsDstnc.equals("")) {
            plogging_dist.setText("코스 길이: " + crsDstnc + "km");
        } else {
            plogging_dist.setVisibility(View.GONE);
        }

        if (!crsSummary.equals("")) {
            plogging_content.setText(Html.fromHtml(crsSummary));
        } else {
            plogging_content.setVisibility(View.GONE);
        }

        if (!crsContent.equals("")) {
            plogging_detail.setText(Html.fromHtml(crsContent));
        } else {
            plogging_detail.setVisibility(View.GONE);
            plogging_detail_txt.setVisibility(View.GONE);
        }

        if (!crsTourInfo.equals("")) {
            plogging_point.setText(Html.fromHtml(crsTourInfo));
        } else {
            plogging_point.setVisibility(View.GONE);
            plogging_point_txt.setVisibility(View.GONE);
        }

        if (!travelerinfo.equals("")) {
            plogging_traveler_info.setText(Html.fromHtml(travelerinfo));
        } else {
            plogging_traveler_info.setVisibility(View.GONE);
            plogging_traveler_info_txt.setVisibility(View.GONE);
        }

        if (!gpx_path.equals("")) {
            plogging_gpx.setText(Html.fromHtml(gpx_path));
        } else {
            plogging_gpx.setVisibility(View.GONE);
            plogging_gpx_txt.setVisibility(View.GONE);
        }

        mDatabase.collection("BookmarkItem").document(userId).collection("plogging").document(crsKorNm).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                    deleteBookmark(crsKorNm);
                }
                else if(bookmarkBtn.isActivated()){
                    // 선택 동작
                    writeBookmark(crsKorNm, sigun, crsLevel, crsContent);
                }

            }
        });

        return v;
    }

    private void writeBookmark(String crsKorNm, String sigun, String crsLevel, String crsContent){
        Map<String, Object> info = new HashMap<>();
        info.put("name", crsKorNm);
        info.put("sigun", sigun);
        info.put("level", crsLevel);
        info.put("info", crsContent);
        info.put("type", "플로깅");

        mDatabase.collection("BookmarkItem").document(userId).collection("plogging").document(crsKorNm).set(info);
    }

    private void deleteBookmark(String crsKorNm){
        mDatabase.collection("BookmarkItem").document(userId).collection("plogging").document(crsKorNm).delete();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
    }

}
