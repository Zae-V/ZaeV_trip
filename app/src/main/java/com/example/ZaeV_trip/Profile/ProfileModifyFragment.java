package com.example.ZaeV_trip.Profile;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.example.ZaeV_trip.Sign.SignInFragment;
import com.example.ZaeV_trip.Sign.SignUpFragment;
import com.example.ZaeV_trip.util.MySharedPreferences;

import de.hdodenhof.circleimageview.CircleImageView;

import com.example.ZaeV_trip.R;

public class ProfileModifyFragment extends Fragment {
    String userName;
    String userEmail;
    String userProfileImage;

    public static ProfileModifyFragment newInstance() {
        return new ProfileModifyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_modify_profile, container, false);

        userName = MySharedPreferences.getUserName(getActivity().getApplicationContext());
        userEmail = MySharedPreferences.getUserEmail(getActivity().getApplicationContext());
        userProfileImage = MySharedPreferences.getUserProfileImage(getActivity().getApplicationContext());

        // 텍스트 Gradient 적용
        TextView textView = v.findViewById(R.id.profileText);
        Tvg.change(textView, Color.parseColor("#6C92F4"),  Color.parseColor("#41E884"));

        TextView mainNameTextView = v.findViewById(R.id.mainNameTextView);
        mainNameTextView.setText(userName);

        TextView nameTextView = v.findViewById(R.id.nameTextView);
        nameTextView.setText(userName);

        TextView emailTextView = v.findViewById(R.id.emailTextView);
        emailTextView.setText(userEmail);

        CircleImageView userProfileImageView = v.findViewById(R.id.profileImageView);

        Glide.with(this)
                .load(userProfileImage)
                .placeholder(R.drawable.default_profile_image)
                .error(R.drawable.default_profile_image)
                .fallback(R.drawable.default_profile_image)
                .into(userProfileImageView);

        ModifyActivity activity = (ModifyActivity)getActivity();

        Button confirmButton = v.findViewById(R.id.confirmBtn);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 비밀번호 확인 하기 //
                activity.replaceFragment(ProfileModifyDetailFragment.newInstance());
            }
        });

        return v;
    }
}
