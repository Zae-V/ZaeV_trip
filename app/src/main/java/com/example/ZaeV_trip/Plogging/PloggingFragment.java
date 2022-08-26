package com.example.ZaeV_trip.Plogging;

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
        TextView plogging_detail_txt = v.findViewById(R.id.plogging_detail_txt);
        TextView plogging_point = v.findViewById(R.id.plogging_point);
        TextView plogging_point_txt = v.findViewById(R.id.plogging_point_txt);
        TextView plogging_traveler_info = v.findViewById(R.id.plogging_traveler_info);
        TextView plogging_traveler_info_txt = v.findViewById(R.id.plogging_traveler_info_txt);
        TextView plogging_gpx = v.findViewById(R.id.plogging_gpx);
        TextView plogging_gpx_txt = v.findViewById(R.id.plogging_gpx_txt);

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

        return v;
    }
}
