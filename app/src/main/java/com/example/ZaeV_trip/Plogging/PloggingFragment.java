package com.example.ZaeV_trip.Plogging;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.Plogging;

import org.w3c.dom.Text;

public class PloggingFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_plogging, container, false);

        Plogging plogging = (Plogging) getArguments().getSerializable("plogging");

        TextView plogging_name = v.findViewById(R.id.plogging_name);
        TextView plogging_summary = v.findViewById(R.id.plogging_summary);
        TextView plogging_content = v.findViewById(R.id.plogging_content);
        TextView plogging_tour_info = v.findViewById(R.id.plogging_tour_info);
        TextView plogging_tour_info2 = v.findViewById(R.id.plogging_tour_info2);
        TextView plogging_travel_info = v.findViewById(R.id.plogging_travel_info);


        plogging_name.setText(plogging.getCrsKorNm());
        plogging_summary.setText(Html.fromHtml(plogging.getCrsSummary()));
        plogging_content.setText("코스 개요" + "\n" + Html.fromHtml(plogging.getCrsContents()));
        plogging_tour_info.setText("관광 포인트" + "\n" + Html.fromHtml(plogging.getCrsTourInfo()));
        plogging_tour_info2.setText(
                "코스 정보" + "\n" +
                "난이도: " + plogging.getCrsLevel() + "\n" +
                        "소요 시간(분): " + plogging.getCrsTotlRqrmHour() + "\n" +
                        "코스 길이 (단위:km): " + plogging.getCrsDstnc());
        plogging_travel_info.setText("여행자 정보" + "\n" + Html.fromHtml(plogging.getTravelerinfo()));

        return v;
    }
}
