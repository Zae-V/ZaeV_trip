package com.example.ZaeV_trip.Profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.captaindroid.tvg.Tvg;

import de.hdodenhof.circleimageview.CircleImageView;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.util.MySharedPreferences;


public class ProfileModifyDetailFragment extends Fragment {
    String userName;
    String userProfileImage;

    public static ProfileModifyDetailFragment newInstance() {
        return new ProfileModifyDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_modify_profile_detail, container, false);

        userName = MySharedPreferences.getUserName(getActivity().getApplicationContext());
        userProfileImage = MySharedPreferences.getUserProfileImage(getActivity().getApplicationContext());

        // 텍스트 Gradient 적용
        TextView textView = v.findViewById(R.id.profileText);
        Tvg.change(textView, Color.parseColor("#6C92F4"),  Color.parseColor("#41E884"));

        TextView mainNameTextView = v.findViewById(R.id.mainNameTextView);
        mainNameTextView.setText(userName);

        CircleImageView userProfileImageView = v.findViewById(R.id.profileImageView);

        Glide.with(this)
                .load(userProfileImage)
                .placeholder(R.drawable.default_profile_image)
                .error(R.drawable.default_profile_image)
                .fallback(R.drawable.default_profile_image)
                .into(userProfileImageView);

        Button modifyButton = v.findViewById(R.id.modifyBtn);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 정보 수정 //
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        return v;
    }
}
