package com.example.ZaeV_trip.Profile;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.ZaeV_trip.util.SignUtil;

import java.util.Objects;

public class ProfileModifyFragment extends Fragment {
    String userName;
    String userEmail;
    String userProfileImage;

    Button kakaoCertificationBtn;
    Button signInBtn;
    TextView errorTextView;
    TextView passwordTextView;
    EditText editPW;

    ModifyActivity activity;

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

        String signType = MySharedPreferences.getUserSignType(getActivity().getApplicationContext());

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

        activity = (ModifyActivity)getActivity();

        kakaoCertificationBtn = v.findViewById(R.id.kakaoCertificationBtn);
        passwordTextView = v.findViewById(R.id.passwordTextView);
        editPW = v.findViewById(R.id.editPW);
        errorTextView = v.findViewById(R.id.errorTextView);
        signInBtn = v.findViewById(R.id.signInBtn);

        if (Objects.equals(signType, "kakao")) {
            setVisibility(1);
            setVisibility(2);
            setVisibility(3);
            setVisibility(4);
        } else {
            setVisibility(5);
        }

        kakaoCertificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 비밀번호 확인 하기 //
                SignUtil.kakaoSign(getActivity(), 3);
//                activity.replaceFragment(ProfileModifyDetailFragment.newInstance());
            }
        });

        return v;
    }

    public void setVisibility(Integer idx) {
        switch (idx) {
            case 1:
                passwordTextView.setVisibility(View.GONE);
                break;
            case 2:
                editPW.setVisibility(View.GONE);
                break;
            case 3:
                signInBtn.setVisibility(View.GONE);
                break;
            case 4:
                errorTextView.setVisibility(View.GONE);
                break;
            case 5:
                kakaoCertificationBtn.setVisibility(View.GONE);
                break;
            default:
                Log.d("WithdrawalActivity", "default");
        }
    }

    public void changeFragment() {
        activity.replaceFragment(ProfileModifyDetailFragment.newInstance());
    }
    public void setErrorText(String message) {
        errorTextView.setText(message);
    }
}
