package com.zaev.ZaeV_trip.Sign;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.captaindroid.tvg.Tvg;

import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.util.SignUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpFragment extends Fragment {

    TextView greetingText;
    EditText editUsernameEnter;
    EditText editEmailEnter;
    EditText editPWEnter;
    EditText editPWCheck;
    public static TextView msg;

    android.widget.Button signUpBtn;

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;


    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        greetingText = v.findViewById(R.id.greetingText);
        editUsernameEnter = v.findViewById(R.id.editUsernameEnter);
        editEmailEnter =v.findViewById(R.id.editEmailEnter);
        editPWEnter = v.findViewById(R.id.editPWEnter);
        editPWCheck =v.findViewById(R.id.editPWCheck);
        msg = v.findViewById(R.id.error_message);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        // 텍스트 글자색 그라데이션 적용
        Tvg.change(greetingText, Color.parseColor("#6C92F4"),  Color.parseColor("#41E884"));

        signUpBtn = v.findViewById(R.id.signUpBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = editPWEnter.getText().toString().trim();
                String pwCheck = editPWCheck.getText().toString().trim();
                String username = editUsernameEnter.getText().toString().trim();
                String email = editEmailEnter.getText().toString().trim();

                SignUtil.emailSignUp(getActivity(), password, pwCheck, username, email);
            }
        });
        return v;
    }
}
