package com.example.ZaeV_trip.Profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import de.hdodenhof.circleimageview.CircleImageView;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.util.MySharedPreferences;

import org.w3c.dom.Text;

import java.util.Objects;


public class ProfileModifyDetailFragment extends Fragment {
    String userName;
    String userEmail;
    String userProfileImage;

    TextView nameTextView;
    TextView passwordTextView;
    TextView checkPWTextView;
    EditText editName;
    EditText editPW;
    EditText editPWCheck;
    Button modifyBtn;

    public static ProfileModifyDetailFragment newInstance() {
        return new ProfileModifyDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_modify_profile_detail, container, false);

        userName = MySharedPreferences.getUserName(getActivity().getApplicationContext());
        userEmail = MySharedPreferences.getUserEmail(getActivity().getApplicationContext());
        userProfileImage = MySharedPreferences.getUserProfileImage(getActivity().getApplicationContext());

        String signType = MySharedPreferences.getUserSignType(getActivity().getApplicationContext());

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

        nameTextView = v.findViewById(R.id.nameTextView);
        passwordTextView = v.findViewById(R.id.passwordTextView);
        checkPWTextView = v.findViewById(R.id.checkPWTextView);
        editName = v.findViewById(R.id.editName);
        editPW = v.findViewById(R.id.editPW);
        editPWCheck = v.findViewById(R.id.editPWCheck);
        modifyBtn = v.findViewById(R.id.modifyBtn);

        if (Objects.equals(signType, "kakao")) {
            setVisibility(3);
            setVisibility(4);
            setVisibility(5);
            setVisibility(6);

            editName.setText(userName);
        }

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 정보 수정 //
                String name = editName.getText().toString().trim();
                MySharedPreferences.modifyUserName(getActivity(), userEmail, name);

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        return v;
    }

    public void setVisibility(Integer idx) {
        switch (idx) {
            case 1:
                nameTextView.setVisibility(View.GONE);
                break;
            case 2:
                editName.setVisibility(View.GONE);
                break;
            case 3:
                passwordTextView.setVisibility(View.GONE);
                break;
            case 4:
                editPW.setVisibility(View.GONE);
                break;
            case 5:
                checkPWTextView.setVisibility(View.GONE);
                break;
            case 6:
                editPWCheck.setVisibility(View.GONE);
                break;
            default:
                Log.d("WithdrawalActivity", "default");
        }
    }
}
