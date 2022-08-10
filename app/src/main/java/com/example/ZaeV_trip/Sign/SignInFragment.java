package com.example.ZaeV_trip.Sign;

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

import com.captaindroid.tvg.Tvg;
import com.example.ZaeV_trip.MainActivity;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.util.SignUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import com.example.ZaeV_trip.model.Users;
import com.example.ZaeV_trip.util.MySharedPreferences;

public class SignInFragment extends Fragment {
    TextView userGreetingText;

    TextView findPWText;
    TextView signUpText;

    EditText editID;
    EditText editPW;

    Button signInBtn;
    public static TextView msg;

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);

        userGreetingText = v.findViewById(R.id.userGreetingText);
        findPWText = v.findViewById(R.id.findPwText);
        signUpText = v.findViewById(R.id.signUpText);

        editID = v.findViewById(R.id.editID);
        editPW = v.findViewById(R.id.editPW);

        signInBtn = v.findViewById(R.id.signInBtn);
        msg = v.findViewById(R.id.error_message);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        findPWText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editID.getText().toString().trim();
                String pwd = editPW.getText().toString().trim();
                SignUtil.emailSignIn(getActivity(), email, pwd, 1);
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SignActivity) getActivity()).replaceFragment(SignUpFragment.newInstance()); // 새로 불러올 Fragment의 Instance를 Main으로 전달
            }
        });

        // 텍스트 글자색 그라데이션 적용
        Tvg.change(userGreetingText, Color.parseColor("#6C92F4"), Color.parseColor("#41E884"));
        return v;
    }
}
