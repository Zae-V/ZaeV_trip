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
        TextView plogging_level = v.findViewById(R.id.plogging_level);
        TextView plogging_hour = v.findViewById(R.id.plogging_hour);
        TextView plogging_dist = v.findViewById(R.id.plogging_dist);
        TextView plogging_content = v.findViewById(R.id.plogging_content);
        TextView plogging_detail = v.findViewById(R.id.plogging_detail);
        TextView plogging_point = v.findViewById(R.id.plogging_point);
        TextView plogging_traveler_info = v.findViewById(R.id.plogging_traveler_info);
        TextView plogging_gpx = v.findViewById(R.id.plogging_gpx);

        String crsKorNm = plogging.getCrsKorNm();
        String crsLevel = plogging.getCrsLevel();
        String crsTotlRqrmHour = plogging.getCrsTotlRqrmHour();
        String crsDstnc = plogging.getCrsDstnc();
        String crsSummary = plogging.getCrsSummary();
        String crsContent = plogging.getCrsContents();
        String crsTourInfo = plogging.getCrsTourInfo();
        String travelerinfo = plogging.getTravelerinfo();
        String gpx_path = plogging.getGpxpath();

        if (!crsKorNm.equals("")) {
            plogging_name.setText(crsKorNm);
        } else {
            plogging_name.setVisibility(View.GONE);
        }

        if (!crsLevel.equals("")) {
            plogging_level.setText("난이도: " + crsLevel);
        } else {
            plogging_level.setVisibility(View.GONE);
        }

        if (!crsTotlRqrmHour.equals("")) {
            plogging_hour.setText("소요 시간(분): " + crsTotlRqrmHour);
        } else {
            plogging_hour.setVisibility(View.GONE);
        }

        if (!crsDstnc.equals("")) {
            plogging_dist.setText("코스 길이 (단위:km): " + crsDstnc);
        } else {
            plogging_dist.setVisibility(View.GONE);
        }

        if (!crsSummary.equals("")) {
            plogging_content.setText(Html.fromHtml(crsSummary));
        } else {
            plogging_content.setVisibility(View.GONE);
        }

        if (!crsContent.equals("")) {
            plogging_detail.setText("코스 개요" + "\n" +  Html.fromHtml(crsContent));
        } else {
            plogging_detail.setVisibility(View.GONE);
        }

        if (!crsTourInfo.equals("")) {
            plogging_point.setText("관광 포인트" + "\n" +  Html.fromHtml(crsTourInfo));
        } else {
            plogging_point.setVisibility(View.GONE);
        }

        if (!travelerinfo.equals("")) {
            plogging_traveler_info.setText("여행자 정보" + "\n" +  Html.fromHtml(travelerinfo));
        } else {
            plogging_traveler_info.setVisibility(View.GONE);
        }

        if (!gpx_path.equals("")) {
            plogging_gpx.setText("gpx 정보" + "\n" +  Html.fromHtml(gpx_path));
        } else {
            plogging_gpx.setVisibility(View.GONE);
        }

        return v;
    }
}
